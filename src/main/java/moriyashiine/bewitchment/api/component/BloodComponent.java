package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.BloodSetEvents;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class BloodComponent implements AutoSyncedComponent, ServerTickingComponent {
	public static int MAX_BLOOD = 100;
	
	private final LivingEntity obj;
	private int blood = MAX_BLOOD;
	
	public BloodComponent(LivingEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("Blood")) {
			setBlood(tag.getInt("Blood"));
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("Blood", getBlood());
	}
	
	@Override
	public void serverTick() {
		if (BWTags.HAS_BLOOD.contains(obj.getType()) && !BewitchmentAPI.isVampire(obj, true) && obj.getRandom().nextFloat() < (obj.isSleeping() ? 1 / 50f : 1 / 500f)) {
			fillBlood(1, false);
		}
	}
	
	public int getBlood() {
		return blood;
	}
	
	public void setBlood(int blood) {
		BloodSetEvents.ON_BLOOD_SET.invoker().onSetBlood(obj, blood);
		this.blood = blood;
		BWComponents.BLOOD_COMPONENT.sync(obj);
	}
	
	public boolean fillBlood(int amount, boolean simulate) {
		BloodSetEvents.ON_BLOOD_FILL.invoker().onFillBlood(obj, amount, simulate);
		if (getBlood() < MAX_BLOOD) {
			if (!simulate) {
				setBlood(Math.min(MAX_BLOOD, getBlood() + amount));
			}
			return true;
		}
		return false;
	}
	
	public boolean drainBlood(int amount, boolean simulate) {
		BloodSetEvents.ON_BLOOD_DRAIN.invoker().onDrainBlood(obj, amount, simulate);
		if (getBlood() - amount >= 0) {
			if (!simulate) {
				setBlood(getBlood() - amount);
			}
			return true;
		}
		return false;
	}
}
