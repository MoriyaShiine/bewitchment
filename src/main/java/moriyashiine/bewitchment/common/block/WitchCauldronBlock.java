package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
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

public class WitchCauldronBlock extends CauldronBlock implements BlockEntityProvider, Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(2, 1, 2, 14, 2, 14), createCuboidShape(14, 2, 1, 15, 6, 15), createCuboidShape(1, 2, 1, 2, 6, 15), createCuboidShape(2, 2, 14, 14, 6, 15), createCuboidShape(13, 5, 3, 14, 8.5, 13), createCuboidShape(2, 2, 1, 14, 6, 2), createCuboidShape(2, 5, 2, 14, 8.5, 3), createCuboidShape(1, 8.5, 14, 15, 11, 15), createCuboidShape(2, 5, 3, 3, 8.5, 13), createCuboidShape(2, 5, 13, 14, 8.5, 14), createCuboidShape(14, 8.5, 2, 15, 11, 14), createCuboidShape(1, 8.5, 1, 15, 11, 2), createCuboidShape(1, 8.5, 2, 2, 11, 14), createCuboidShape(11, 0, 3, 13, 1, 5), createCuboidShape(3, 0, 3, 5, 1, 5), createCuboidShape(3, 0, 11, 5, 1, 13), createCuboidShape(11, 0, 11, 13, 1, 13));
	
	public WitchCauldronBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(Properties.LEVEL_3, 0));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new WitchCauldronBlockEntity();
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
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(Properties.LIT, BWTags.HEATS_CAULDRON.contains(ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock()));
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			state = state.with(Properties.LEVEL_3, 0);
			state = state.with(Properties.LIT, false);
		}
		else {
			state = state.with(Properties.LIT, BWTags.HEATS_CAULDRON.contains(world.getBlockState(pos.down()).getBlock()));
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof NameTagItem && stack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof WitchCauldronBlockEntity) {
				((WitchCauldronBlockEntity) blockEntity).customName = stack.getName();
				if (!player.isCreative()) {
					stack.decrement(1);
				}
				return ActionResult.success(world.isClient);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void onSteppedOn(World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof WitchCauldronBlockEntity && ((WitchCauldronBlockEntity) blockEntity).heatTimer >= 60) {
				entity.damage(DamageSource.HOT_FLOOR, 1);
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, Properties.LEVEL_3, Properties.LIT);
	}
}
