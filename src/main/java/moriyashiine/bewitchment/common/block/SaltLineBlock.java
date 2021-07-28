package moriyashiine.bewitchment.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.misc.interfaces.EntityShapeContextAdditionAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.block.RedstoneWireBlock.*;

public class SaltLineBlock extends Block {
	private static final Map<Direction, VoxelShape> SIDE_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, createCuboidShape(3, 0, 0, 13, 1, 13), Direction.SOUTH, createCuboidShape(3, 0, 3, 13, 1, 16), Direction.EAST, createCuboidShape(3, 0, 3, 16, 1, 13), Direction.WEST, createCuboidShape(0, 0, 3, 13, 1, 13)));
	private static final Map<Direction, VoxelShape> NON_SIDE_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, VoxelShapes.union(SIDE_SHAPES.get(Direction.NORTH), createCuboidShape(3, 0, 0, 13, 16, 1)), Direction.SOUTH, VoxelShapes.union(SIDE_SHAPES.get(Direction.SOUTH), createCuboidShape(3, 0, 15, 13, 16, 16)), Direction.EAST, VoxelShapes.union(SIDE_SHAPES.get(Direction.EAST), createCuboidShape(15, 0, 3, 16, 16, 13)), Direction.WEST, VoxelShapes.union(SIDE_SHAPES.get(Direction.WEST), createCuboidShape(0, 0, 3, 1, 16, 13))));
	private static final VoxelShape DOT_SHAPE = createCuboidShape(3, 0, 3, 13, 1, 13);
	private final Map<BlockState, VoxelShape> outlineShapes = new HashMap<>();
	
	public SaltLineBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(WIRE_CONNECTION_NORTH, WireConnection.NONE).with(WIRE_CONNECTION_EAST, WireConnection.NONE).with(WIRE_CONNECTION_SOUTH, WireConnection.NONE).with(WIRE_CONNECTION_WEST, WireConnection.NONE));
		for (BlockState state : getStateManager().getStates()) {
			VoxelShape voxelShape = DOT_SHAPE;
			for (Direction direction : Direction.Type.HORIZONTAL) {
				WireConnection wireConnection = state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction));
				if (wireConnection == WireConnection.SIDE) {
					voxelShape = VoxelShapes.union(voxelShape, SIDE_SHAPES.get(direction));
				}
				else if (wireConnection == WireConnection.UP) {
					voxelShape = VoxelShapes.union(voxelShape, NON_SIDE_SHAPES.get(direction));
				}
			}
			outlineShapes.put(state, voxelShape);
		}
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return outlineShapes.get(state);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContextAdditionAccessor) {
			Entity entity = ((EntityShapeContextAdditionAccessor) context).bw_getEntity();
			if (entity instanceof LivingEntity && BewitchmentAPI.isWeakToSilver((LivingEntity) entity)) {
				return VoxelShapes.fullCube();
			}
		}
		return super.getCollisionShape(state, world, pos, context);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getPlacementState(ctx.getWorld(), ctx.getBlockPos());
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN) {
			return state;
		}
		else if (direction == Direction.UP) {
			return getPlacementState(world, pos);
		}
		else {
			WireConnection wireConnection = getConnectionFromNeighbors(world, pos, direction, !world.getBlockState(pos.up()).isSolidBlock(world, pos));
			return wireConnection.isConnected() == state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction)).isConnected() && !isFullyConnected(state) ? state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), wireConnection) : getPlacementState(world, pos);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getAbilities().allowModifyWorld) {
			if (isFullyConnected(state) || isNotConnected(state)) {
				BlockState placementState = getPlacementState(world, pos);
				if (placementState != state) {
					world.setBlockState(pos, placementState, 3);
					for (Direction direction : Direction.Type.HORIZONTAL) {
						BlockPos offset = pos.offset(direction);
						if (state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction)).isConnected() != placementState.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction)).isConnected() && world.getBlockState(offset).isSolidBlock(world, offset)) {
							world.updateNeighborsExcept(offset, placementState.getBlock(), direction.getOpposite());
						}
					}
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		return canRunOnTop(world, blockPos, blockState);
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClient && !state.canPlaceAt(world, pos)) {
			dropStacks(state, world, pos);
			world.removeBlock(pos, false);
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.isOf(state.getBlock()) && !world.isClient) {
			for (Direction direction : Direction.Type.VERTICAL) {
				world.updateNeighborsAlways(pos.offset(direction), this);
			}
			updateNeighbors(world, pos);
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!moved && !state.isOf(newState.getBlock())) {
			super.onStateReplaced(state, world, pos, newState, moved);
			if (!world.isClient) {
				for (Direction direction : Direction.values()) {
					world.updateNeighborsAlways(pos.offset(direction), this);
				}
				updateNeighbors(world, pos);
			}
		}
	}
	
	@Override
	public void prepare(BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (Direction direction : Direction.Type.HORIZONTAL) {
			WireConnection wireConnection = state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction));
			if (wireConnection != WireConnection.NONE && !world.getBlockState(mutable.set(pos, direction)).isOf(this)) {
				mutable.move(Direction.DOWN);
				BlockState blockState = world.getBlockState(mutable);
				BlockPos offset;
				if (!blockState.isOf(Blocks.OBSERVER)) {
					offset = mutable.offset(direction.getOpposite());
					replace(blockState, blockState.getStateForNeighborUpdate(direction.getOpposite(), world.getBlockState(offset), world, mutable, offset), world, mutable, flags, maxUpdateDepth);
				}
				mutable.set(pos, direction).move(Direction.UP);
				blockState = world.getBlockState(mutable);
				if (!blockState.isOf(Blocks.OBSERVER)) {
					offset = mutable.offset(direction.getOpposite());
					replace(blockState, blockState.getStateForNeighborUpdate(direction.getOpposite(), world.getBlockState(offset), world, mutable, offset), world, mutable, flags, maxUpdateDepth);
				}
			}
		}
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_180 -> state.with(WIRE_CONNECTION_NORTH, state.get(WIRE_CONNECTION_SOUTH)).with(WIRE_CONNECTION_EAST, state.get(WIRE_CONNECTION_WEST)).with(WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_NORTH)).with(WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_EAST));
			case COUNTERCLOCKWISE_90 -> state.with(WIRE_CONNECTION_NORTH, state.get(WIRE_CONNECTION_EAST)).with(WIRE_CONNECTION_EAST, state.get(WIRE_CONNECTION_SOUTH)).with(WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_WEST)).with(WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_NORTH));
			case CLOCKWISE_90 -> state.with(WIRE_CONNECTION_NORTH, state.get(WIRE_CONNECTION_WEST)).with(WIRE_CONNECTION_EAST, state.get(WIRE_CONNECTION_NORTH)).with(WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_EAST)).with(WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_SOUTH));
			default -> state;
		};
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> state.with(WIRE_CONNECTION_NORTH, state.get(WIRE_CONNECTION_SOUTH)).with(WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_NORTH));
			case FRONT_BACK -> state.with(WIRE_CONNECTION_EAST, state.get(WIRE_CONNECTION_WEST)).with(WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_EAST));
			default -> super.mirror(state, mirror);
		};
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WIRE_CONNECTION_NORTH, WIRE_CONNECTION_SOUTH, WIRE_CONNECTION_EAST, WIRE_CONNECTION_WEST);
	}
	
	private static boolean isFullyConnected(BlockState state) {
		return state.get(WIRE_CONNECTION_NORTH).isConnected() && state.get(WIRE_CONNECTION_SOUTH).isConnected() && state.get(WIRE_CONNECTION_EAST).isConnected() && state.get(WIRE_CONNECTION_WEST).isConnected();
	}
	
	private static boolean isNotConnected(BlockState state) {
		return !state.get(WIRE_CONNECTION_NORTH).isConnected() && !state.get(WIRE_CONNECTION_SOUTH).isConnected() && !state.get(WIRE_CONNECTION_EAST).isConnected() && !state.get(WIRE_CONNECTION_WEST).isConnected();
	}
	
	private BlockState getPlacementState(BlockView world, BlockPos pos) {
		BlockState state = getDefaultState();
		boolean notSolid = !world.getBlockState(pos.up()).isSolidBlock(world, pos);
		for (Direction direction : Direction.Type.HORIZONTAL) {
			if (!state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction)).isConnected()) {
				WireConnection wireConnection = getConnectionFromNeighbors(world, pos, direction, notSolid);
				state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(direction), wireConnection);
			}
		}
		if (!isNotConnected(state) || !isNotConnected(state)) {
			boolean north = state.get(WIRE_CONNECTION_NORTH).isConnected();
			boolean south = state.get(WIRE_CONNECTION_SOUTH).isConnected();
			boolean east = state.get(WIRE_CONNECTION_EAST).isConnected();
			boolean west = state.get(WIRE_CONNECTION_WEST).isConnected();
			boolean eastOrWest = !north && !south;
			boolean northOrSouth = !east && !west;
			if (!west && eastOrWest) {
				state = state.with(WIRE_CONNECTION_WEST, WireConnection.SIDE);
			}
			if (!east && eastOrWest) {
				state = state.with(WIRE_CONNECTION_EAST, WireConnection.SIDE);
			}
			if (!north && northOrSouth) {
				state = state.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE);
			}
			if (!south && northOrSouth) {
				state = state.with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE);
			}
		}
		return state;
	}
	
	private WireConnection getConnectionFromNeighbors(BlockView blockView, BlockPos blockPos, Direction direction, boolean notSolid) {
		BlockPos offset = blockPos.offset(direction);
		BlockState state = blockView.getBlockState(offset);
		if (notSolid && canRunOnTop(blockView, offset, state) && blockView.getBlockState(offset.up()).isOf(this)) {
			return state.isSideSolidFullSquare(blockView, offset, direction.getOpposite()) ? WireConnection.UP : WireConnection.SIDE;
		}
		return !state.isOf(this) && (state.isSolidBlock(blockView, offset) || !blockView.getBlockState(offset.down()).isOf(this)) ? WireConnection.NONE : WireConnection.SIDE;
	}
	
	private boolean canRunOnTop(BlockView world, BlockPos pos, BlockState floor) {
		return floor.isSideSolidFullSquare(world, pos, Direction.UP) || floor.isOf(Blocks.HOPPER);
	}
	
	private void updateNeighbors(World world, BlockPos pos) {
		List<BlockPos> toUpdate = new ArrayList<>();
		for (Direction direction : Direction.Type.HORIZONTAL) {
			toUpdate.add(pos.offset(direction));
		}
		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockPos offset = pos.offset(direction);
			toUpdate.add(world.getBlockState(offset).isSolidBlock(world, offset) ? offset.up() : offset.down());
		}
		toUpdate.forEach(blockPos -> {
			if (world.getBlockState(blockPos).isOf(this)) {
				world.updateNeighborsAlways(blockPos, this);
				for (Direction direction : Direction.values()) {
					world.updateNeighborsAlways(blockPos.offset(direction), this);
				}
			}
		});
	}
}
