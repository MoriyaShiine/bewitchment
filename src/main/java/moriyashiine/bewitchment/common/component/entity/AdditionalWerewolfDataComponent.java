package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class AdditionalWerewolfDataComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private int variant = 0;
	private boolean forcedTransformation = false;
	
	public AdditionalWerewolfDataComponent(PlayerEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		setForcedTransformation(tag.getBoolean("ForcedTransformation"));
		setVariant(tag.getInt("WerewolfVariant"));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ForcedTransformation", isForcedTransformation());
		tag.putInt("WerewolfVariant", getVariant());
	}
	
	@Override
	public void tick() {
		if (BewitchmentAPI.isWerewolf(obj, false)) {
			obj.airStrafingSpeed *= 1.5f;
		}
	}
	
	public int getVariant() {
		return variant;
	}
	
	public void setVariant(int variant) {
		this.variant = variant;
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.sync(obj);
	}
	
	public boolean isForcedTransformation() {
		return forcedTransformation;
	}
	
	public void setForcedTransformation(boolean forcedTransformation) {
		this.forcedTransformation = forcedTransformation;
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.sync(obj);
	}
}
