package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockableBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Lockable {
	private final List<UUID> entities = new ArrayList<>();
	private UUID owner = null;
	private boolean modeOnWhitelist = false, locked = false;
	
	public LockableBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.LOCKABLE, pos, state);
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
	public void fromClientTag(NbtCompound tag) {
		fromNbtLockable(tag);
	}
	
	@Override
	public NbtCompound toClientTag(NbtCompound tag) {
		toNbtLockable(tag);
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
