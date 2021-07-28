package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class JuniperChestBlockEntity extends BWChestBlockEntity implements BlockEntityClientSerializable, TaglockHolder {
	private final DefaultedList<ItemStack> taglockInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private UUID owner = null;
	
	public JuniperChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, Type type, boolean trapped) {
		super(blockEntityType, blockPos, blockState, type, trapped);
	}
	
	public JuniperChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(BWBlockEntityTypes.JUNIPER_CHEST, blockPos, blockState, Type.JUNIPER, false);
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
	public void fromClientTag(NbtCompound tag) {
		fromNbtTaglock(tag);
	}
	
	@Override
	public NbtCompound toClientTag(NbtCompound tag) {
		toNbtTaglock(tag);
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
}
