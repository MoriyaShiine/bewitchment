package moriyashiine.bewitchment.api.interfaces.entity;

import java.util.Optional;

public interface DespawnAccessor {
	static Optional<DespawnAccessor> of(Object entity) {
		if (entity instanceof DespawnAccessor) {
			return Optional.of(((DespawnAccessor) entity));
		}
		return Optional.empty();
	}
	
	void setDespawnTimer(int despawnTimer);
}
