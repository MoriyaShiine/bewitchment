/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.block;

import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CandelabraBlock extends Block implements Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(6, 0, 6, 10, 2, 10), createCuboidShape(6.5, 2, 6.5, 9.5, 3, 9.5), createCuboidShape(7, 3, 7, 9, 16, 9), createCuboidShape(1, 7.5, 7, 15, 8.5, 9), createCuboidShape(7, 7.5, 1, 9, 8.5, 15), createCuboidShape(7, 8.5, 1, 9, 14.5, 3), createCuboidShape(1, 8.5, 7, 3, 14.5, 9), createCuboidShape(13, 8.5, 7, 15, 14.5, 9), createCuboidShape(7, 8.5, 13, 9, 14.5, 15), createCuboidShape(6.5, 9.5, 0.5, 9.5, 10.5, 3.5), createCuboidShape(0.5, 9.5, 6.5, 3.5, 10.5, 9.5), createCuboidShape(12.5, 9.5, 6.5, 15.5, 10.5, 9.5), createCuboidShape(6.5, 9.5, 12.5, 9.5, 10.5, 15.5), createCuboidShape(6.5, 11, 6.5, 9.5, 12, 9.5));

	public final byte repellentRadius;

	public CandelabraBlock(Settings settings, byte repellentRadius) {
		super(settings);
		this.repellentRadius = repellentRadius;
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(Properties.LIT, true));
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
		boolean client = world.isClient;
		if (!state.get(Properties.WATERLOGGED)) {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() instanceof FlintAndSteelItem && !state.get(Properties.LIT)) {
				if (!client) {
					world.setBlockState(pos, state.with(Properties.LIT, true));
					world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1, 1);
					stack.damage(1, player, stackUser -> stackUser.sendToolBreakStatus(hand));
				}
				return ActionResult.success(client);
			} else if (stack.isEmpty() && state.get(Properties.LIT)) {
				if (!client) {
					world.setBlockState(pos, state.with(Properties.LIT, false));
					world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 2);
				}
				return ActionResult.success(client);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(Properties.LIT, false);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			if (state.get(Properties.LIT)) {
				world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 2);
				state = state.with(Properties.LIT, false);
			}
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient && state.getBlock() != oldState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			worldState.potentialCandelabras.add(pos.asLong());
			worldState.markDirty();
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClient && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			for (int i = worldState.potentialCandelabras.size() - 1; i >= 0; i--) {
				if (worldState.potentialCandelabras.get(i) == pos.asLong()) {
					worldState.potentialCandelabras.remove(i);
					worldState.markDirty();
				}
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(Properties.LIT)) {
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.175, pos.getZ() + 0.5, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.125, pos.getY() + 1.075, pos.getZ() + 0.5, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.875, pos.getY() + 1.075, pos.getZ() + 0.5, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.075, pos.getZ() + 0.875, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.075, pos.getZ() + 0.125, 0, 0, 0);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, Properties.LIT);
	}
}
