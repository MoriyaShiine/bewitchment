package moriyashiine.bewitchment.api.interfaces.entity;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface PolymorphAccessor {
	Optional<UUID> getPolymorphUUID();
	
	void setPolymorphUUID(Optional<UUID> uuid);
	
	String getPolymorphName();
	
	void setPolymorphName(String name);
}
