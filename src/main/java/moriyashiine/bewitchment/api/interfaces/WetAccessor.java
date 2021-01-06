package moriyashiine.bewitchment.api.interfaces;

import net.minecraft.entity.Entity;

import java.util.Optional;

public interface WetAccessor {
	static Optional<WetAccessor> of(Entity entity) {
		return Optional.of(((WetAccessor) entity));
	}
	
	int getWetTimer();
	
	void setWetTimer(int wetTimer);
}
