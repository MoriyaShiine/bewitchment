package moriyashiine.bewitchment.api.interfaces.entity;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface InsanityTargetAccessor {
	static Optional<InsanityTargetAccessor> of(Object entity) {
		if (entity instanceof InsanityTargetAccessor) {
			return Optional.of(((InsanityTargetAccessor) entity));
		}
		return Optional.empty();
	}
	
	Optional<UUID> getInsanityTargetUUID();
	
	void setInsanityTargetUUID(Optional<UUID> insanityTargetUUID);
}
