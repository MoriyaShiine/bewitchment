package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.client.network.packet.SyncPoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PoppetShelfBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Inventory {
	public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
	
	public boolean markedForSync = false;
	
	public PoppetShelfBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.POPPET_SHELF, pos, state);
	}
	
	@Override
	public void fromClientTag(NbtCompound tag) {
		Inventories.readNbt(tag, inventory);
	}
	
	@Override
	public NbtCompound toClientTag(NbtCompound tag) {
		Inventories.writeNbt(tag, inventory);
		return tag;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		fromClientTag(nbt);
		super.readNbt(nbt);
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		return super.writeNbt(toClientTag(nbt));
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, PoppetShelfBlockEntity blockEntity) {
		if (world != null && !world.isClient && blockEntity.markedForSync) {
			blockEntity.syncPoppetShelf();
		}
	}
	
	@Override
	public int size() {
		return inventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(inventory, slot, amount);
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 16;
	}
	
	@Override
	public void clear() {
		inventory.clear();
	}
	
	public void syncPoppetShelf() {
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(this).forEach(playerEntity -> {
				SyncClientSerializableBlockEntity.send(playerEntity, this);
				SyncPoppetShelfBlockEntity.send(playerEntity, this);
			});
		}
	}
	
	private int getFirstEmptySlot() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}
	
	public void onUse(World world, BlockPos pos, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof PoppetItem) {
			int firstEmpty = getFirstEmptySlot();
			if (firstEmpty != -1) {
				setStack(firstEmpty, stack.split(1));
				markDirty();
				syncPoppetShelf();
			}
		}
		else {
			ItemScatterer.spawn(world, pos, this);
			markDirty();
			syncPoppetShelf();
		}
	}
}
