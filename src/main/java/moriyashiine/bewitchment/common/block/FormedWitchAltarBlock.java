package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.client.network.message.SyncPlacedItem;
import moriyashiine.bewitchment.common.block.entity.PlacedItemBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.fabric.api.server.PlayerStream;
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
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
					Objects.requireNonNull(world.getServer()).submit(() -> PlayerStream.watching(placedItemBlockEntity).forEach(foundPlayer -> SyncPlacedItem.send(foundPlayer, placedItemBlockEntity.getPos(), placedItemBlockEntity.stack)));
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
		BlockEntity blockEntity = world.getBlockEntity(findAltarPos(world, pos));
		if (blockEntity instanceof WitchAltarBlockEntity) {
			((WitchAltarBlockEntity) blockEntity).refreshAltar();
		}
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		breakAltar(world, pos);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		super.onDestroyedByExplosion(world, pos, explosion);
		breakAltar(world, pos);
	}
	
	public BlockPos findAltarPos(World world, BlockPos pos) {
		List<BlockPos> validPoses = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
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
			if (world.getBlockState(offset).getBlock() == this) {
				world.breakBlock(offset, true);
				breakAltar(world, offset);
			}
		}
	}
}