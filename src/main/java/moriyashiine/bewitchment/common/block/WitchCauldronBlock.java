package moriyashiine.bewitchment.common.block;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
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
import org.jetbrains.annotations.Nullable;

public class WitchCauldronBlock extends CauldronBlock implements Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(2, 1, 2, 14, 2, 14), createCuboidShape(14, 2, 1, 15, 6, 15), createCuboidShape(1, 2, 1, 2, 6, 15), createCuboidShape(2, 2, 14, 14, 6, 15), createCuboidShape(13, 5, 3, 14, 8.5, 13), createCuboidShape(2, 2, 1, 14, 6, 2), createCuboidShape(2, 5, 2, 14, 8.5, 3), createCuboidShape(1, 8.5, 14, 15, 11, 15), createCuboidShape(2, 5, 3, 3, 8.5, 13), createCuboidShape(2, 5, 13, 14, 8.5, 14), createCuboidShape(14, 8.5, 2, 15, 11, 14), createCuboidShape(1, 8.5, 1, 15, 11, 2), createCuboidShape(1, 8.5, 2, 2, 11, 14), createCuboidShape(11, 0, 3, 13, 1, 5), createCuboidShape(3, 0, 3, 5, 1, 5), createCuboidShape(3, 0, 11, 5, 1, 13), createCuboidShape(11, 0, 11, 13, 1, 13));
	
	public WitchCauldronBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(Properties.LEVEL_3, 0));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		boolean water = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, water).with(Properties.LEVEL_3, water ? 3 : 0);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			world.setBlockState(pos, state.with(Properties.LEVEL_3, 3), 3);
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, Properties.LEVEL_3);
	}
}
