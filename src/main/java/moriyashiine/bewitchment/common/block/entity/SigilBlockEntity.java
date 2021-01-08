package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.HasSigil;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class SigilBlockEntity extends BlockEntity implements Tickable, HasSigil {
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
	public void fromTag(BlockState state, CompoundTag tag) {
		fromTagSigil(tag);
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		toTagSigil(tag);
		return super.toTag(tag);
	}
	
	@Override
	public void tick() {
		tick(world, pos, this);
	}
}
