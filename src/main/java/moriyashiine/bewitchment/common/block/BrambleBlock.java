package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.misc.interfaces.EntityShapeContextAdditionAccessor;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class BrambleBlock extends SugarCaneBlock {
	
	public BrambleBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (this == BWObjects.THICK_BRAMBLE && context instanceof EntityShapeContextAdditionAccessor) {
			Entity entity = ((EntityShapeContextAdditionAccessor) context).bw_getEntity();
			if (entity instanceof LivingEntity && !entity.isSneaking()) {
				return VoxelShapes.fullCube();
			}
		}
		return super.getCollisionShape(state, world, pos, context);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState downState = world.getBlockState(pos.down());
		return downState.isOf(this) || downState.isOf(Blocks.GRASS_BLOCK) || downState.isOf(Blocks.DIRT) || downState.isOf(Blocks.COARSE_DIRT) || downState.isOf(Blocks.PODZOL) || downState.isOf(Blocks.SAND) || downState.isOf(Blocks.RED_SAND);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this == BWObjects.GLOWING_BRAMBLE && !world.isClient && state.getBlock() != oldState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			worldState.glowingBrambles.add(pos.asLong());
			worldState.markDirty();
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (this == BWObjects.GLOWING_BRAMBLE && !world.isClient && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			for (int i = worldState.glowingBrambles.size() - 1; i >= 0; i--) {
				if (worldState.glowingBrambles.get(i) == pos.asLong()) {
					worldState.glowingBrambles.remove(i);
					worldState.markDirty();
				}
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof LivingEntity livingEntity) {
			if (this == BWObjects.ENDER_BRAMBLE && !livingEntity.hasVehicle()) {
				BWUtil.attemptTeleport(livingEntity, entity.getBlockPos(), 64, true);
			}
			if (this == BWObjects.SCORCHED_BRAMBLE) {
				livingEntity.setOnFireFor(10);
			}
		}
		if (this == BWObjects.THICK_BRAMBLE && !entity.isSneaking()) {
			entity.slowMovement(state, new Vec3d(0.25, 0.5, 0.25));
		}
		if (this == BWObjects.FLEETING_BRAMBLE) {
			if (entity.isInSneakingPose()) {
				entity.setVelocity(entity.getVelocity().multiply(1, 0.5, 1));
				entity.fallDistance = 0;
			}
			else {
				entity.addVelocity(0, 0.4, 0);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		ParticleType<?> particleType = null;
		if (this == BWObjects.ENDER_BRAMBLE) {
			particleType = ParticleTypes.PORTAL;
		}
		else if (this == BWObjects.SCORCHED_BRAMBLE) {
			particleType = ParticleTypes.FLAME;
		}
		else if (this == BWObjects.FLEETING_BRAMBLE) {
			particleType = ParticleTypes.CLOUD;
		}
		if (particleType != null) {
			world.addParticle((ParticleEffect) particleType, pos.getX() + 0.5 + MathHelper.nextDouble(random, -0.5, 0.5), pos.getY() + 0.5 + MathHelper.nextDouble(random, -0.5, 0.5), pos.getZ() + 0.5 + MathHelper.nextDouble(random, -0.5, 0.5), 0, 0, 0);
		}
	}
	
	public static class Fruiting extends BrambleBlock {
		public Fruiting(Settings settings) {
			super(settings);
			setDefaultState(getDefaultState().with(BWProperties.HAS_FRUIT, false));
		}
		
		@Override
		public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
			boolean client = world.isClient;
			if (state.get(BWProperties.HAS_FRUIT)) {
				if (!client) {
					world.setBlockState(pos, state.with(Properties.LEVEL_15, 0).with(BWProperties.HAS_FRUIT, false));
					world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1, 1);
					ItemStack stack = new ItemStack(BWObjects.WITCHBERRY);
					if (!player.getInventory().insertStack(stack)) {
						player.dropStack(stack);
					}
				}
				return ActionResult.success(client);
			}
			else {
				ItemStack stack = player.getStackInHand(hand);
				if (stack.getItem() instanceof BoneMealItem) {
					if (!world.isClient) {
						if (!player.isCreative()) {
							stack.decrement(1);
						}
						int level = Math.min(8, state.get(Properties.LEVEL_15) + world.random.nextInt(3) + 2);
						world.setBlockState(pos, state.with(Properties.LEVEL_15, level).with(BWProperties.HAS_FRUIT, level == 8));
					}
					return ActionResult.success(client);
				}
			}
			return super.onUse(state, world, pos, player, hand, hit);
		}
		
		@Override
		public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
			super.randomTick(state, world, pos, random);
			state = world.getBlockState(pos);
			if (!state.get(BWProperties.HAS_FRUIT)) {
				int level = state.get(Properties.LEVEL_15);
				if (level == 15) {
					world.setBlockState(pos, state.with(Properties.LEVEL_15, 0).with(BWProperties.HAS_FRUIT, true));
				}
				else {
					world.setBlockState(pos, state.with(Properties.LEVEL_15, level + 1));
				}
			}
		}
		
		@Override
		protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
			super.appendProperties(builder.add(Properties.LEVEL_15, BWProperties.HAS_FRUIT));
		}
	}
}
