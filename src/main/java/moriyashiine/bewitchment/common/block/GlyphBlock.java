/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
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
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class GlyphBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 0.125, 16);

	public GlyphBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.getBlock() == BWObjects.GOLDEN_GLYPH ? new GlyphBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return state.getBlock() == BWObjects.GOLDEN_GLYPH ? (tickerWorld, pos, tickerState, blockEntity) -> GlyphBlockEntity.tick(tickerWorld, pos, tickerState, (GlyphBlockEntity) blockEntity) : null;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (this == BWObjects.GOLDEN_GLYPH) {
			boolean client = world.isClient;
			if (!client) {
				((GlyphBlockEntity) world.getBlockEntity(pos)).onUse(world, pos, player, hand, null);
			}
			return ActionResult.success(client);
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing()).with(Properties.AGE_5, ctx.getWorld().random.nextInt(6));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return Blocks.TORCH.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(this == BWObjects.GLYPH ? BWObjects.CHALK : this == BWObjects.GOLDEN_GLYPH ? BWObjects.GOLDEN_CHALK : this == BWObjects.FIERY_GLYPH ? BWObjects.FIERY_CHALK : BWObjects.ELDRITCH_CHALK);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return BWObjects.SALT_LINE.canPlaceAt(state, world, pos);
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return this != BWObjects.GOLDEN_GLYPH;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient && state.getBlock() != oldState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof UsesAltarPower usesAltarPower) {
				usesAltarPower.setAltarPos(WitchAltarBlock.getClosestAltarPos(world, pos));
				blockEntity.markDirty();
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClient && state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof Inventory inventory) {
			ItemScatterer.spawn(world, pos, inventory);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		ParticleType<?> particleType = null;
		if (this == BWObjects.FIERY_GLYPH) {
			particleType = ParticleTypes.FLAME;
		} else if (this == BWObjects.ELDRITCH_GLYPH) {
			particleType = ParticleTypes.REVERSE_PORTAL;
		}
		if (particleType != null) {
			world.addParticle((ParticleEffect) particleType, pos.getX() + 0.5 + MathHelper.nextDouble(random, -0.5, 0.5), pos.getY() + 0.125, pos.getZ() + 0.5 + MathHelper.nextDouble(random, -0.5, 0.5), 0, 0, 0);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, Properties.AGE_5);
	}
}
