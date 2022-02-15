/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class AdditionalWaterDataComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Entity obj;
	private boolean submerged = false;
	private int wetTimer = 0;

	public AdditionalWaterDataComponent(Entity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		setSubmerged(tag.getBoolean("Submerged"));
		setWetTimer(tag.getInt("WetTimer"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("Submerged", isSubmerged());
		tag.putInt("WetTimer", getWetTimer());
	}

	@Override
	public void serverTick() {
		if (isSubmerged()) {
			setSubmerged(false);
		}
		if (getWetTimer() > 0) {
			setWetTimer(getWetTimer() - 1);
		}
	}

	public boolean isSubmerged() {
		return submerged;
	}

	public void setSubmerged(boolean submerged) {
		this.submerged = submerged;
		BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.sync(obj);
	}

	public int getWetTimer() {
		return wetTimer;
	}

	public void setWetTimer(int wetTimer) {
		this.wetTimer = wetTimer;
		BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.sync(obj);
	}
}
