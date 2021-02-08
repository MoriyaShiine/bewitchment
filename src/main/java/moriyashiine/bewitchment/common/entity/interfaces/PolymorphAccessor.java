package moriyashiine.bewitchment.common.entity.interfaces;

import java.util.UUID;

public interface PolymorphAccessor {
	UUID getPolymorphUUID();
	
	void setPolymorphUUID(UUID uuid);
	
	String getPolymorphName();
	
	void setPolymorphName(String name);
}
