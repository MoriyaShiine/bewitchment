package moriyashiine.bewitchment.common.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Objects;

public class GobletBlock extends Block implements Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(7, 0, 7, 9, 0.25, 9), createCuboidShape(7.75, 0.25, 7.75, 8.25, 3, 8.25), createCuboidShape(6.125, 3, 6.125, 9.875, 3.125, 9.875), createCuboidShape(6.125, 3.125, 6, 9.875, 5.625, 6.125), createCuboidShape(6.125, 3.125, 9.875, 9.875, 5.625, 10), createCuboidShape(9.875, 3.125, 6.125, 10, 5.625, 9.875), createCuboidShape(6, 3.125, 6.125, 6.125, 5.625, 9.875));
	
	public GobletBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return Objects.requireNonNull(super.getPlacementState(ctx)).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return direction == Direction.DOWN && !canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return 15;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED);
	}
}