package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.block.entity.DistilleryBlockEntity;
import moriyashiine.bewitchment.common.block.entity.util.BWCraftingBlockEntity;
import moriyashiine.bewitchment.common.screenhandler.DistilleryScreenHandler;
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

public class DistilleryBlock extends BlockWithEntity implements Waterloggable {
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.union(createCuboidShape(7.1, 3, 11.5, 0.1, 10.9, 4.5), createCuboidShape(1.2, 0, 11.4, 0.2, 3, 10.4), createCuboidShape(5, 0, 9.5, 2, 1, 6.5), createCuboidShape(13.8, 11, 8.5, 7.5, 14.5, 7.5), createCuboidShape(1.2, 0, 5.6, 0.2, 3, 4.6), createCuboidShape(13.8, 7.8, 8.5, 12.8, 11.8, 7.5), createCuboidShape(12.3, 0, 9.9, 11.3, 2, 8.9), createCuboidShape(7, 0, 11.4, 6, 3, 10.4), createCuboidShape(7.5, 12.8, 9, 2.5, 14.8, 7), createCuboidShape(15.1, 0, 7.1, 14.1, 2, 6.1), createCuboidShape(7, 0, 5.6, 6, 3, 4.6), createCuboidShape(12.3, 0, 7.1, 11.3, 2, 6.1), createCuboidShape(6.5, 3.5, 11, 0.5, 7.5, 5), createCuboidShape(14.5, 2.5, 9.5, 11.5, 4.5, 6.5), createCuboidShape(15.2, 2, 10, 11.2, 8, 6), createCuboidShape(6.5, 10.9, 11, 0.5, 12.9, 5), createCuboidShape(15.1, 0, 9.9, 14.1, 2, 8.9));
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.union(createCuboidShape(8.9, 3, 4.5, 15.9, 10.9, 11.5), createCuboidShape(14.8, 0, 4.6, 15.8, 3, 5.6), createCuboidShape(11, 0, 6.5, 14, 1, 9.5), createCuboidShape(2.2, 11, 7.5, 8.5, 14.5, 8.5), createCuboidShape(14.8, 0, 10.4, 15.8, 3, 11.4), createCuboidShape(2.2, 7.8, 7.5, 3.2, 11.8, 8.5), createCuboidShape(3.7, 0, 6.1, 4.7, 2, 7.1), createCuboidShape(9, 0, 4.6, 10, 3, 5.6), createCuboidShape(8.5, 12.8, 7, 13.5, 14.8, 9), createCuboidShape(0.9, 0, 8.9, 1.9, 2, 9.9), createCuboidShape(9, 0, 10.4, 10, 3, 11.4), createCuboidShape(3.7, 0, 8.9, 4.7, 2, 9.9), createCuboidShape(9.5, 3.5, 5, 15.5, 7.5, 11), createCuboidShape(1.5, 2.5, 6.5, 4.5, 4.5, 9.5), createCuboidShape(0.8, 2, 6, 4.8, 8, 10), createCuboidShape(9.5, 10.9, 5, 15.5, 12.9, 11), createCuboidShape(0.9, 0, 6.1, 1.9, 2, 7.1));
	private static final VoxelShape SHAPE_WEST = VoxelShapes.union(createCuboidShape(4.5, 3, 8.9, 11.5, 10.9, 15.9), createCuboidShape(4.6, 0, 14.8, 5.6, 3, 15.8), createCuboidShape(6.5, 0, 11, 9.5, 1, 14), createCuboidShape(7.5, 11, 2.2, 8.5, 14.5, 8.5), createCuboidShape(10.4, 0, 14.8, 11.4, 3, 15.8), createCuboidShape(7.5, 7.8, 2.2, 8.5, 11.8, 3.2), createCuboidShape(6.1, 0, 3.7, 7.1, 2, 4.7), createCuboidShape(4.6, 0, 9, 5.6, 3, 10), createCuboidShape(7, 12.8, 8.5, 9, 14.8, 13.5), createCuboidShape(8.9, 0, 0.9, 9.9, 2, 1.9), createCuboidShape(10.4, 0, 9, 11.4, 3, 10), createCuboidShape(8.9, 0, 3.7, 9.9, 2, 4.7), createCuboidShape(5, 3.5, 9.5, 11, 7.5, 15.5), createCuboidShape(6.5, 2.5, 1.5, 9.5, 4.5, 4.5), createCuboidShape(6, 2, 0.8, 10, 8, 4.8), createCuboidShape(5, 10.9, 9.5, 11, 12.9, 15.5), createCuboidShape(6.1, 0, 0.9, 7.1, 2, 1.9));
	private static final VoxelShape SHAPE_EAST = VoxelShapes.union(createCuboidShape(11.5, 3, 7.1, 4.5, 10.9, 0.1), createCuboidShape(11.4, 0, 1.2, 10.4, 3, 0.2), createCuboidShape(9.5, 0, 5, 6.5, 1, 2), createCuboidShape(8.5, 11, 13.8, 7.5, 14.5, 7.5), createCuboidShape(5.6, 0, 1.2, 4.6, 3, 0.2), createCuboidShape(8.5, 7.8, 13.8, 7.5, 11.8, 12.8), createCuboidShape(9.9, 0, 12.3, 8.9, 2, 11.3), createCuboidShape(11.4, 0, 7, 10.4, 3, 6), createCuboidShape(9, 12.8, 7.5, 7, 14.8, 2.5), createCuboidShape(7.1, 0, 15.1, 6.1, 2, 14.1), createCuboidShape(5.6, 0, 7, 4.6, 3, 6), createCuboidShape(7.1, 0, 12.3, 6.1, 2, 11.3), createCuboidShape(11, 3.5, 6.5, 5, 7.5, 0.5), createCuboidShape(9.5, 2.5, 14.5, 6.5, 4.5, 11.5), createCuboidShape(10, 2, 15.2, 6, 8, 11.2), createCuboidShape(11, 10.9, 6.5, 5, 12.9, 0.5), createCuboidShape(9.9, 0, 15.1, 8.9, 2, 14.1));
	
	public DistilleryBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(BWProperties.FILLED, false).with(Properties.WATERLOGGED, false));
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new DistilleryBlockEntity();
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
					return new DistilleryScreenHandler(syncId, inv, pos);
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
		builder.add(BWProperties.FILLED, Properties.HORIZONTAL_FACING, Properties.WATERLOGGED);
	}
}