package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.interfaces.MagicUser;
import moriyashiine.bewitchment.common.block.entity.PlacedItemBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FormedWitchAltarBlock extends BlockWithEntity {
	private final Block unformed;
	private final boolean hasEntity;
	
	public FormedWitchAltarBlock(boolean hasEntity, Block unformed, Settings settings) {
		super(settings);
		this.unformed = unformed;
		this.hasEntity = hasEntity;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return hasEntity ? new WitchAltarBlockEntity() : null;
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
			ItemStack stack = player.getStackInHand(hand);
			ItemPlacementContext ctx = new ItemPlacementContext(player, hand, stack, new BlockHitResult(hit.getPos().add(0, 1, 0), hit.getSide(), hit.getBlockPos().up(), hit.isInsideBlock()));
			if (!stack.isEmpty() && !(stack.getItem() instanceof BlockItem) && world.getBlockState(pos.up()).canReplace(ctx)) {
				world.setBlockState(pos.up(), BWObjects.placed_item.getPlacementState(ctx));
				BlockEntity blockEntity = world.getBlockEntity(pos.up());
				if (blockEntity instanceof PlacedItemBlockEntity) {
					PlacedItemBlockEntity placedItemBlockEntity = ((PlacedItemBlockEntity) blockEntity);
					placedItemBlockEntity.stack = stack.split(1);
					placedItemBlockEntity.markDirty();
				}
				world.updateNeighbor(pos, this, pos.up());
			}
			else {
				BlockPos altarPos = findAltarPos(world, pos);
				if (altarPos != null) {
					BlockEntity blockEntity = world.getBlockEntity(altarPos);
					if (blockEntity instanceof WitchAltarBlockEntity) {
						WitchAltarBlockEntity witchAltar = (WitchAltarBlockEntity) blockEntity;
						player.sendMessage(new TranslatableText("altar.information", witchAltar.magic, witchAltar.maxMagic, witchAltar.gain), true);
					}
				}
			}
		}
		return ActionResult.success(client);
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(unformed);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		if (fromPos.equals(pos.up()))
		{
			BlockPos altarPos = findAltarPos(world, pos);
			if (altarPos != null)
			{
				BlockEntity blockEntity = world.getBlockEntity(altarPos);
				if (blockEntity instanceof WitchAltarBlockEntity) {
					((WitchAltarBlockEntity) blockEntity).refreshAltar();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.getBlock() != newState.getBlock())
		{
			breakAltar(world, pos);
			if (hasEntity)
			{
				refreshAltarPoses(world, pos);
			}
		}
	}
	
	public static void refreshAltarPoses(World world, BlockPos pos)
	{
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		for (byte x = -24; x <= 24; x++)
		{
			for (byte y = -24; y <= 24; y++)
			{
				for (byte z = -24; z <= 24; z++)
				{
					mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
					BlockEntity blockEntity = world.getBlockEntity(mutablePos);
					if (blockEntity instanceof MagicUser)
					{
						((MagicUser) blockEntity).setAltarPos(getNearestAltarPos(world, mutablePos));
						blockEntity.markDirty();
					}
				}
			}
		}
	}
	
	private static BlockPos getNearestAltarPos(World world, BlockPos pos)
	{
		List<BlockPos> validPoses = new ArrayList<>();
		for (byte x = -24; x <= 24; x++) {
			for (byte y = -24; y <= 24; y++) {
				for (byte z = -24; z <= 24; z++) {
					BlockPos foundPos = pos.add(x, y, z);
					if (world.getBlockState(foundPos).getBlock() instanceof FormedWitchAltarBlock)
					{
						BlockPos altarPos = findAltarPos(world, foundPos);
						if (altarPos != null)
						{
							validPoses.add(altarPos);
						}
					}
				}
			}
		}
		if (validPoses.size() > 0) {
			BlockPos closest = validPoses.get(0);
			for (BlockPos foundPos : validPoses) {
				if (foundPos.getSquaredDistance(pos) < closest.getSquaredDistance(pos)) {
					closest = foundPos;
				}
			}
			return closest;
		}
		return null;
	}
	
	public static BlockPos findAltarPos(World world, BlockPos pos) {
		List<BlockPos> validPoses = new ArrayList<>();
		for (byte x = -1; x <= 1; x++) {
			for (byte z = -1; z <= 1; z++) {
				BlockPos foundPos = pos.add(x, 0, z);
				if (world.getBlockEntity(foundPos) instanceof WitchAltarBlockEntity) {
					validPoses.add(foundPos);
				}
			}
		}
		if (validPoses.size() > 0) {
			BlockPos closest = validPoses.get(0);
			for (BlockPos foundPos : validPoses) {
				if (foundPos.getSquaredDistance(pos) < closest.getSquaredDistance(pos)) {
					closest = foundPos;
				}
			}
			return closest;
		}
		return null;
	}
	
	private void breakAltar(World world, BlockPos pos) {
		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockPos offset = pos.offset(direction);
			Block block = world.getBlockState(offset).getBlock();
			if (block instanceof FormedWitchAltarBlock) {
				world.breakBlock(offset, true);
				breakAltar(world, offset);
			}
		}
	}
}