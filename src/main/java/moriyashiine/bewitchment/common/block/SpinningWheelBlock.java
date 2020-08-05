package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.block.entity.SpinningWheelBlockEntity;
import moriyashiine.bewitchment.common.block.entity.util.BWCraftingBlockEntity;
import moriyashiine.bewitchment.common.screenhandler.SpinningWheelScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Objects;

public class SpinningWheelBlock extends BlockWithEntity implements Waterloggable {
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.union(createCuboidShape(13, 0, 9.9, 12, 3, 8.9), createCuboidShape(13, 0, 7.1, 12, 3, 6.1), createCuboidShape(2, 0, 8.5, 1, 8, 7.5), createCuboidShape(14, 2, 10, 0, 9, 6), createCuboidShape(12.5, 9, 8, 9, 12.75, 7), createCuboidShape(15.9, 8, 8, 7.9, 16, 9), createCuboidShape(4, 7.7, 8.5, 3, 15.7, 7.5), createCuboidShape(4.5, 12, 9, 2.5, 15, 7));
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.union(createCuboidShape(3, 0, 6.1, 4, 3, 7.1), createCuboidShape(3, 0, 8.9, 4, 3, 9.9), createCuboidShape(14, 0, 7.5, 15, 8, 8.5), createCuboidShape(2, 2, 6, 16, 9, 10), createCuboidShape(3.5, 9, 8, 7, 12.75, 9), createCuboidShape(0.1, 8, 7, 8.1, 16, 8), createCuboidShape(12, 7.7, 7.5, 13, 15.7, 8.5), createCuboidShape(11.5, 12, 7, 13.5, 15, 9));
	private static final VoxelShape SHAPE_WEST = VoxelShapes.union(createCuboidShape(6.1, 0, 3, 7.1, 3, 4), createCuboidShape(8.9, 0, 3, 9.9, 3, 4), createCuboidShape(7.5, 0, 14, 8.5, 8, 15), createCuboidShape(6, 2, 2, 10, 9, 16), createCuboidShape(7, 9, 3.5, 8, 12.75, 7), createCuboidShape(8, 8, 0.1, 9, 16, 8.1), createCuboidShape(7.5, 7.7, 12, 8.5, 15.7, 13), createCuboidShape(7, 12, 11.5, 9, 15, 13.5));
	private static final VoxelShape SHAPE_EAST = VoxelShapes.union(createCuboidShape(9.9, 0, 13, 8.9, 3, 12), createCuboidShape(7.1, 0, 13, 6.1, 3, 12), createCuboidShape(8.5, 0, 2, 7.5, 8, 1), createCuboidShape(10, 2, 14, 6, 9, 0), createCuboidShape(9, 9, 12.5, 8.1, 12.75, 9), createCuboidShape(8, 8, 15.9, 7, 16, 7.9), createCuboidShape(8.5, 7.7, 4, 7.5, 15.7, 3), createCuboidShape(9, 12, 4.5, 7, 15, 2.5));
	
	public SpinningWheelBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new SpinningWheelBlockEntity();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		switch (direction) {
			case NORTH:
				return SHAPE_NORTH;
			case SOUTH:
				return SHAPE_SOUTH;
			case WEST:
				return SHAPE_WEST;
			default:
				return SHAPE_EAST;
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean client = world.isClient;
		if (!client) {
			player.openHandledScreen(new ExtendedScreenHandlerFactory() {
				@Override
				public Text getDisplayName() {
					return getName();
				}
				
				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
					return new SpinningWheelScreenHandler(syncId, inv, pos);
				}
				
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
					packetByteBuf.writeBlockPos(pos);
				}
			});
		}
		return ActionResult.success(client);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return Objects.requireNonNull(super.getPlacementState(ctx)).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing()).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
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
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
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
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BWCraftingBlockEntity) {
				((BWCraftingBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING, Properties.WATERLOGGED);
	}
}
