package moriyashiine.bewitchment.api.interfaces.entity;

import java.util.Optional;

public interface WetAccessor {
	static Optional<WetAccessor> of(Object entity) {
		if (entity instanceof WetAccessor) {
			return Optional.of(((WetAccessor) entity));
		}
		return Optional.empty();
	}
	
	int getWetTimer();
	
	void setWetTimer(int wetTimer);
}
