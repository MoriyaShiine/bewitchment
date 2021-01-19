package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.misc.CanHoldTaglocks;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.UUID;

public class TaglockHolderBlockEntity extends BlockEntity implements BlockEntityClientSerializable, CanHoldTaglocks {
	private final DefaultedList<ItemStack> taglockInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private UUID owner = null;
	
	public TaglockHolderBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public TaglockHolderBlockEntity() {
		this(BWBlockEntityTypes.TAGLOCK_HOLDER);
	}
	
	@Override
	public DefaultedList<ItemStack> getTaglockInventory() {
		return taglockInventory;
	}
	
	@Override
	public UUID getOwner() {
		return owner;
	}
	
	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTagTaglock(tag);
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		toTagTaglock(tag);
		return tag;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		fromClientTag(tag);
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(toClientTag(tag));
	}
}
