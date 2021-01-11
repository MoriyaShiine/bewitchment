package moriyashiine.bewitchment.api.interfaces;

import java.util.Optional;

public interface RespawnTimerAccessor {
	static Optional<RespawnTimerAccessor> of(Object entity) {
		if (entity instanceof RespawnTimerAccessor) {
			return Optional.of(((RespawnTimerAccessor) entity));
		}
		return Optional.empty();
	}
	
	int getRespawnTimer();
	
	void setRespawnTimer(int respawnTimer);
}
