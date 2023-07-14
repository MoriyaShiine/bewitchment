/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class FullInvisibilityComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final PlayerEntity obj;
	private boolean fullInvisible = false;

	public FullInvisibilityComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		setFullInvisible(tag.getBoolean("FullInvisible"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("FullInvisible", isFullInvisible());
	}

	@Override
	public void serverTick() {
		if (isFullInvisible() && !obj.isSneaking()) {
			setFullInvisible(false);
			obj.setInvisible(false);
			obj.removeStatusEffect(StatusEffects.INVISIBILITY);
		}
	}

	public boolean isFullInvisible() {
		return fullInvisible;
	}

	public void setFullInvisible(boolean fullInvisible) {
		this.fullInvisible = fullInvisible;
		BWComponents.FULL_INVISIBILITY_COMPONENT.sync(obj);
	}
}
