package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.misc.Lockable;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ElderChestBlockEntity extends BWChestBlockEntity implements BlockEntityClientSerializable, Lockable {
	private final List<UUID> entities = new ArrayList<>();
	private UUID owner = null;
	private boolean modeOnWhitelist = false, locked = false;
	
	public ElderChestBlockEntity() {
		super(BWBlockEntityTypes.ELDER_CHEST, Type.ELDER, false);
	}
	
	public ElderChestBlockEntity(BlockEntityType<?> blockEntityType, boolean trapped) {
		super(blockEntityType, Type.ELDER, trapped);
	}
	
	@Override
	public List<UUID> getEntities() {
		return entities;
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
	public boolean getModeOnWhitelist() {
		return modeOnWhitelist;
	}
	
	@Override
	public void setModeOnWhitelist(boolean modeOnWhitelist) {
		this.modeOnWhitelist = modeOnWhitelist;
	}
	
	@Override
	public boolean getLocked() {
		return locked;
	}
	
	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTagLockable(tag);
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		toTagLockable(tag);
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
