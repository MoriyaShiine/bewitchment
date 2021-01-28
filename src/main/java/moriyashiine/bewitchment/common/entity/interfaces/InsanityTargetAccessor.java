package moriyashiine.bewitchment.common.entity.interfaces;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface InsanityTargetAccessor {
	Optional<UUID> getInsanityTargetUUID();
	
	void setInsanityTargetUUID(Optional<UUID> insanityTargetUUID);
}
