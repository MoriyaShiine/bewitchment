package moriyashiine.bewitchment.api.interfaces;

import java.util.Optional;
import java.util.UUID;

public interface MasterAccessor {
	static Optional<MasterAccessor> of(Object entity) {
		if (entity instanceof MasterAccessor) {
			return Optional.of(((MasterAccessor) entity));
		}
		return Optional.empty();
	}
	
	UUID getMasterUUID();
	
	void setMasterUUID(UUID masterUUID);
}
