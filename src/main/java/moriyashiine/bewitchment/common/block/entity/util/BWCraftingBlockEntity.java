package moriyashiine.bewitchment.common.block.entity.util;

import moriyashiine.bewitchment.api.interfaces.MagicUser;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class BWCraftingBlockEntity extends LockableContainerBlockEntity implements BlockEntityClientSerializable, SidedInventory, Tickable, MagicUser {
	public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
		@Override
		public int get(int index) {
			return recipeTime;
		}
		
		@Override
		public void set(int index, int value) {
			recipeTime = value;
		}
		
		@Override
		public int size() {
			return 1;
		}
	};
	
	protected final int[] INPUT_SLOTS = {0, 1, 2, 3};
	protected final int[] OUTPUT_SLOTS = {4};
	
	protected final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
	
	protected int recipeTime = 0;
	
	private BlockPos altarPos = null;
	
	protected BWCraftingBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}
	
	protected void fromTagAdditional(CompoundTag tag) {
		Inventories.fromTag(tag, inventory);
		recipeTime = tag.getInt("RecipeTime");
		fromTagMagicUser(tag);
	}
	
	protected CompoundTag toTagAdditional(CompoundTag tag) {
		Inventories.toTag(tag, inventory);
		tag.putInt("RecipeTime", recipeTime);
		toTagMagicUser(tag);
		return tag;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		fromTagAdditional(tag);
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		toTagAdditional(tag);
		return super.toTag(tag);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTagAdditional(tag);
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTagAdditional(tag);
	}
	
	@Override
	public boolean isEmpty() {
		for (ItemStack stack : inventory) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		return side == Direction.DOWN ? OUTPUT_SLOTS : INPUT_SLOTS;
	}
	
	@Override
	public int size() {
		return inventory.size();
	}
	
	@Override
	public void clear() {
		inventory.clear();
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
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		int maxCount = getMaxCountPerStack();
		if (stack.getCount() > maxCount) {
			stack.setCount(maxCount);
		}
		markDirty();
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot != 4;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return (world == null || world.getBlockEntity(pos) == this) && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 74;
	}
	
	@Override
	public BlockPos getAltarPos() {
		return altarPos;
	}
	
	@Override
	public void setAltarPos(BlockPos altarPos) {
		this.altarPos = altarPos;
	}
}