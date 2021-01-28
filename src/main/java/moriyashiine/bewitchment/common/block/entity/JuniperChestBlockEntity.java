package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.UUID;

public class JuniperChestBlockEntity extends BWChestBlockEntity implements BlockEntityClientSerializable, TaglockHolder {
	private final DefaultedList<ItemStack> taglockInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private UUID owner = null;
	
	public JuniperChestBlockEntity() {
		super(BWBlockEntityTypes.JUNIPER_CHEST, Type.JUNIPER, false);
	}
	
	public JuniperChestBlockEntity(BlockEntityType<?> blockEntityType, boolean trapped) {
		super(blockEntityType, Type.JUNIPER, trapped);
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
