package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

public class PlacedItemBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	public ItemStack stack = ItemStack.EMPTY;
	
	public PlacedItemBlockEntity() {
		super(BWBlockEntityTypes.placed_item);
	}
	
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 0, toTag(new CompoundTag()));
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		stack = ItemStack.fromTag(tag.getCompound("Item"));
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("Item", stack.toTag(new CompoundTag()));
		return super.toTag(tag);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(getCachedState(), tag);
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}
}