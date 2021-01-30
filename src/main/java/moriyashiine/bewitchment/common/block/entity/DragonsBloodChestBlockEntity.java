package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DragonsBloodChestBlockEntity extends BWChestBlockEntity implements BlockEntityClientSerializable, SigilHolder {
	private final List<UUID> entities = new ArrayList<>();
	private UUID owner = null;
	private Sigil sigil = null;
	private int uses = 0;
	private boolean modeOnWhitelist = false;
	
	public DragonsBloodChestBlockEntity() {
		super(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST, Type.DRAGONS_BLOOD, false);
	}
	
	public DragonsBloodChestBlockEntity(BlockEntityType<?> blockEntityType, boolean trapped) {
		super(blockEntityType, Type.DRAGONS_BLOOD, trapped);
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
	public Sigil getSigil() {
		return sigil;
	}
	
	@Override
	public void setSigil(Sigil sigil) {
		this.sigil = sigil;
	}
	
	@Override
	public int getUses() {
		return uses;
	}
	
	@Override
	public void setUses(int uses) {
		this.uses = uses;
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
	public void fromClientTag(CompoundTag tag) {
		fromTagSigil(tag);
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		toTagSigil(tag);
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
	
	@Override
	public void tick() {
		super.tick();
		tick(world, pos, this);
	}
}
