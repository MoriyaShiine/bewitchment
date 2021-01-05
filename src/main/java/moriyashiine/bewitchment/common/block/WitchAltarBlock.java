package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.client.network.packet.SyncWitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
public class WitchAltarBlock extends Block implements BlockEntityProvider, Waterloggable {
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
	public BlockEntity createBlockEntity(BlockView world) {
		return formed ? new WitchAltarBlockEntity() : null;
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
					Direction facing = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING);
					world.breakBlock(pos, false);
					world.setBlockState(pos, entry.formed.getPlacementState(new ItemPlacementContext(player, hand, stack, hit)).with(Properties.HORIZONTAL_FACING, facing));
				}
				return ActionResult.success(client);
			}
		}
		else {
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof WitchAltarBlockEntity) {
				WitchAltarBlockEntity altar = (WitchAltarBlockEntity) entity;
				if (!client) {
					if (!stack.isEmpty()) {
						Item item = stack.getItem();
						boolean sword = BWTags.SWORDS.contains(item);
						boolean pentacle = BWTags.PENTACLES.contains(item);
						boolean wand = BWTags.WANDS.contains(item);
						if (sword || pentacle || wand) {
							int slot = sword ? 0 : pentacle ? 1 : 2;
							ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, altar.removeStack(slot, 1));
							altar.setStack(slot, stack.split(1));
							world.setBlockState(pos, state.with(Properties.LEVEL_15, calculateLuminance(altar)), 11);
							world.updateComparators(pos, this);
							altar.markedForScan = true;
							altar.sync();
						}
					}
					else {
						if (player.isSneaking()) {
							ItemScatterer.spawn(world, pos.add(0, 0.5, 0), altar);
							world.setBlockState(pos, state.with(Properties.LEVEL_15, 0), 11);
							world.updateComparators(pos, this);
							altar.markedForScan = true;
							altar.sync();
							PlayerLookup.tracking(altar).forEach(playerEntity -> SyncWitchAltarBlockEntity.send(player, altar));
						}
						else {
							player.sendMessage(new LiteralText(altar.power + " / " + altar.maxPower + " (" + altar.gain + "x)"), true);
						}
					}
				}
				return ActionResult.success(client);
			}
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
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing());
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof WitchAltarBlockEntity) {
			WitchAltarBlockEntity altar = (WitchAltarBlockEntity) blockEntity;
			int items = 0;
			for (int i = 0; i < altar.size(); i++) {
				if (!altar.getStack(i).isEmpty()) {
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
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int x = -24; x <= 24; x++) {
				for (int y = -24; y <= 24; y++) {
					for (int z = -24; z <= 24; z++) {
						BlockEntity blockEntity = world.getBlockEntity(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
						if (blockEntity instanceof UsesAltarPower) {
							((UsesAltarPower) blockEntity).setAltarPos(getClosestAltarPos(world, mutable.toImmutable()));
							blockEntity.markDirty();
						}
					}
				}
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
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof WitchAltarBlockEntity) {
				ItemScatterer.spawn(world, pos, (WitchAltarBlockEntity) blockEntity);
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
		if (!world.isClient && state.getBlock() != newState.getBlock()) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int x = -24; x <= 24; x++) {
				for (int y = -24; y <= 24; y++) {
					for (int z = -24; z <= 24; z++) {
						BlockEntity blockEntity = world.getBlockEntity(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
						if (blockEntity instanceof UsesAltarPower) {
							((UsesAltarPower) blockEntity).setAltarPos(getClosestAltarPos(world, mutable.toImmutable()));
							blockEntity.markDirty();
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, Properties.HORIZONTAL_FACING, Properties.LEVEL_15);
	}
	
	@Nullable
	public static BlockPos getClosestAltarPos(World world, BlockPos pos) {
		BlockPos closest = null;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = -24; x <= 24; x++) {
			for (int y = -24; y <= 24; y++) {
				for (int z = -24; z <= 24; z++) {
					if (world.getBlockEntity(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)) instanceof WitchAltarBlockEntity && (closest == null || pos.getSquaredDistance(mutable.toImmutable()) < pos.getSquaredDistance(closest))) {
						closest = mutable.toImmutable();
					}
				}
			}
		}
		return closest;
	}
	
	private static int calculateLuminance(WitchAltarBlockEntity blockEntity) {
		int luminance = 0;
		Item sword = blockEntity.getStack(0).getItem();
		Item pentacle = blockEntity.getStack(1).getItem();
		Item wand = blockEntity.getStack(2).getItem();
		if (sword instanceof BlockItem) {
			luminance = Math.max(luminance, ((BlockItem) sword).getBlock().getDefaultState().getLuminance());
		}
		if (pentacle instanceof BlockItem) {
			luminance = Math.max(luminance, ((BlockItem) pentacle).getBlock().getDefaultState().getLuminance());
		}
		if (wand instanceof BlockItem) {
			luminance = Math.max(luminance, ((BlockItem) wand).getBlock().getDefaultState().getLuminance());
		}
		return luminance;
	}
}
