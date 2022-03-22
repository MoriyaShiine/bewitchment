/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.block;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWTags;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
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
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class WitchAltarBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 2, 16), createCuboidShape(1, 2, 1, 15, 5, 15), createCuboidShape(2, 5, 2, 14, 10, 14), createCuboidShape(1, 10, 1, 15, 12, 15), createCuboidShape(0, 12, 0, 16, 16, 16));

	private final Block unformed;
	private final boolean formed;

	public WitchAltarBlock(Settings settings, Block unformed, boolean formed) {
		super(settings);
		this.unformed = unformed;
		this.formed = formed;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return formed ? new WitchAltarBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world0, BlockState state0, BlockEntityType<T> type) {
		return (world, pos, state, blockEntity) -> WitchAltarBlockEntity.tick(world, pos, state, (WitchAltarBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean client = world.isClient;
		ItemStack stack = player.getStackInHand(hand);
		if (!formed) {
			AltarMapEntry entry = BewitchmentAPI.ALTAR_MAP_ENTRIES.stream().filter(e -> e.unformed == this && e.carpet == stack.getItem()).findFirst().orElse(null);
			if (entry != null) {
				if (!client) {
					if (!player.isCreative()) {
						stack.decrement(1);
					}
					Direction facing = world.getBlockState(pos).get(FACING);
					world.breakBlock(pos, false);
					world.setBlockState(pos, entry.formed.getPlacementState(new ItemPlacementContext(player, hand, stack, hit)).with(FACING, facing));
				}
				return ActionResult.success(client);
			}
		} else {
			WitchAltarBlockEntity blockEntity = (WitchAltarBlockEntity) world.getBlockEntity(pos);
			if (!client) {
				if (!stack.isEmpty()) {
					boolean sword = stack.isIn(BWTags.SWORDS);
					boolean pentacle = stack.isIn(BWTags.PENTACLES);
					boolean wand = stack.isIn(BWTags.WANDS);
					if (sword || pentacle || wand) {
						int slot = sword ? 0 : pentacle ? 1 : 2;
						ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, blockEntity.removeStack(slot, 1));
						blockEntity.setStack(slot, stack.split(1));
						world.setBlockState(pos, state.with(Properties.LEVEL_15, calculateLuminance(blockEntity)), 11);
						world.updateComparators(pos, this);
						blockEntity.markedForScan = true;
						blockEntity.markDirty();
						blockEntity.sync();
					}
				} else {
					if (player.isSneaking()) {
						ItemScatterer.spawn(world, pos.add(0, 1, 0), blockEntity);
						world.setBlockState(pos, state.with(Properties.LEVEL_15, 0), 11);
						world.updateComparators(pos, this);
						blockEntity.markedForScan = true;
						blockEntity.markDirty();
						blockEntity.sync();
					} else {
						player.sendMessage(new LiteralText(blockEntity.power + " / " + blockEntity.maxPower + " (" + blockEntity.gain + "x)"), true);
					}
				}
			}
			return ActionResult.success(client);
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		if (unformed != null) {
			return new ItemStack(unformed);
		}
		return super.getPickStack(world, pos, state);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return formed;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		if (formed) {
			WitchAltarBlockEntity blockEntity = (WitchAltarBlockEntity) world.getBlockEntity(pos);
			int items = 0;
			for (int i = 0; i < blockEntity.size(); i++) {
				if (!blockEntity.getStack(i).isEmpty()) {
					items++;
				}
			}
			return items;
		}
		return 0;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (formed) {
			if (!world.isClient && state.getBlock() != oldState.getBlock()) {
				BWWorldState worldState = BWWorldState.get(world);
				worldState.potentialCandelabras.add(pos.asLong());
				worldState.markDirty();
			}
			for (BlockPos foundPos : BWUtil.getBlockPoses(pos, Bewitchment.config.altarDistributionRadius, currentPos -> world.getBlockEntity(currentPos) instanceof UsesAltarPower)) {
				BlockEntity blockEntity = world.getBlockEntity(foundPos);
				((UsesAltarPower) blockEntity).setAltarPos(getClosestAltarPos(world, pos));
				blockEntity.markDirty();
			}
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
			if (world.getBlockEntity(pos) instanceof Inventory inventory) {
				ItemScatterer.spawn(world, pos, inventory);
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
		if (!world.isClient && state.getBlock() != newState.getBlock()) {
			for (BlockPos foundPos : BWUtil.getBlockPoses(pos, Bewitchment.config.altarDistributionRadius, currentPos -> world.getBlockEntity(currentPos) instanceof UsesAltarPower)) {
				BlockEntity blockEntity = world.getBlockEntity(foundPos);
				((UsesAltarPower) blockEntity).setAltarPos(getClosestAltarPos(world, foundPos));
				blockEntity.markDirty();
			}
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, FACING, Properties.LEVEL_15);
	}

	@Nullable
	public static BlockPos getClosestAltarPos(World world, BlockPos pos) {
		return BWUtil.getClosestBlockPos(pos, Bewitchment.config.altarDistributionRadius, currentPos -> world.getBlockEntity(currentPos) instanceof WitchAltarBlockEntity);
	}

	private static int calculateLuminance(WitchAltarBlockEntity blockEntity) {
		int luminance = 0;
		Item sword = blockEntity.getStack(0).getItem();
		Item pentacle = blockEntity.getStack(1).getItem();
		Item wand = blockEntity.getStack(2).getItem();
		if (sword instanceof BlockItem blockItem) {
			luminance = Math.max(luminance, blockItem.getBlock().getDefaultState().getLuminance());
		}
		if (pentacle instanceof BlockItem blockItem) {
			luminance = Math.max(luminance, blockItem.getBlock().getDefaultState().getLuminance());
		}
		if (wand instanceof BlockItem blockItem) {
			luminance = Math.max(luminance, blockItem.getBlock().getDefaultState().getLuminance());
		}
		return luminance;
	}
}
