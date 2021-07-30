package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.BloodSetEvents;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class BloodComponent implements AutoSyncedComponent, ServerTickingComponent {
	public static int MAX_BLOOD = 100;
	
	private final LivingEntity entity;
	private int blood = MAX_BLOOD;
	
	public BloodComponent(LivingEntity entity) {
		this.entity = entity;
	}
	
	public int getBlood() {
		return blood;
	}
	
	public void setBlood(int blood) {
		BloodSetEvents.ON_BLOOD_SET.invoker().onSetBlood(entity, blood);
		this.blood = blood;
		BWComponents.BLOOD_COMPONENT.sync(entity);
	}
	
	public boolean fillBlood(int amount, boolean simulate) {
		BloodSetEvents.ON_BLOOD_FILL.invoker().onFillBlood(entity, amount, simulate);
		if (getBlood() < MAX_BLOOD) {
			if (!simulate) {
				setBlood(Math.min(MAX_BLOOD, getBlood() + amount));
			}
			return true;
		}
		return false;
	}
	
	public boolean drainBlood(int amount, boolean simulate) {
		BloodSetEvents.ON_BLOOD_DRAIN.invoker().onDrainBlood(entity, amount, simulate);
		if (getBlood() - amount >= 0) {
			if (!simulate) {
				setBlood(getBlood() - amount);
			}
			return true;
		}
		return false;
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
		if (BWTags.HAS_BLOOD.contains(entity.getType()) && !BewitchmentAPI.isVampire(entity, true) && entity.getRandom().nextFloat() < (entity.isSleeping() ? 1 / 50f : 1 / 500f)) {
			fillBlood(1, false);
		}
	}
	
	public static BloodComponent get(LivingEntity entity) {
		return BWComponents.BLOOD_COMPONENT.get(entity);
	}
	
	public static Optional<BloodComponent> maybeGet(LivingEntity entity) {
		return BWComponents.BLOOD_COMPONENT.maybeGet(entity);
	}
}
