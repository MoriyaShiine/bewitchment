package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.block.entity.FocalChalkBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class ChalkBlock extends BlockWithEntity {
	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 0.125, 16);
	
	public Item chalk_item;
	
	public ChalkBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new FocalChalkBlockEntity();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.getBlock() == BWObjects.focal_chalk_block) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FocalChalkBlockEntity) {
				((FocalChalkBlockEntity) blockEntity).onUse(player, hand);
				return ActionResult.success(world.isClient);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(chalk_item);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return this == BWObjects.focal_chalk_block ? PistonBehavior.BLOCK : super.getPistonBehavior(state);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state != null) {
			Random random = ctx.getWorld().random;
			state = state.with(BWProperties.CHALK_VARIANT, random.nextInt(BWProperties.CHALK_VARIANT.getValues().size())).with(Properties.HORIZONTAL_FACING, Direction.Type.HORIZONTAL.random(random));
		}
		return state;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return direction == Direction.DOWN && !canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return this != BWObjects.focal_chalk_block;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory) {
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateComparators(pos, this);
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		ParticleEffect particle = getParticle();
		if (particle != null) {
			world.addParticle(particle, pos.getX() + 0.5 + MathHelper.nextFloat(random, -0.4f, 0.4f), pos.getY() + 0.2, pos.getZ() + 0.5 + MathHelper.nextFloat(random, -0.4f, 0.4f), 0, 0, 0);
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BWProperties.CHALK_VARIANT, Properties.HORIZONTAL_FACING);
	}
	
	protected ParticleEffect getParticle() {
		return this == BWObjects.infernal_chalk_block ? ParticleTypes.FLAME : this == BWObjects.eldritch_chalk_block ? ParticleTypes.PORTAL : null;
	}
}