package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.HasSigil;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;

public class SigilBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, HasSigil {
	private Sigil sigil = null;
	private int uses = 0;
	
	public SigilBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public SigilBlockEntity() {
		this(BWBlockEntityTypes.SIGIL);
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
		tick(world, pos, this);
	}
	
	public void syncSigil() {
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(this).forEach(playerEntity -> SyncClientSerializableBlockEntity.send(playerEntity, this));
		}
	}
}
