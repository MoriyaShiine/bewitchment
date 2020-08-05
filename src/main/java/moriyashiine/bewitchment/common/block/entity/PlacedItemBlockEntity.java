package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class PlacedItemBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	public ItemStack stack = ItemStack.EMPTY;
	
	public PlacedItemBlockEntity() {
		super(BWBlockEntityTypes.placed_item);
	}
	
	private void fromTagAdditional(CompoundTag tag) {
		stack = ItemStack.fromTag(tag.getCompound("Item"));
	}
	
	private CompoundTag toTagAdditional(CompoundTag tag) {
		tag.put("Item", stack.toTag(new CompoundTag()));
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
}
