package moriyashiine.bewitchment.api.interfaces.entity;

import java.util.UUID;

public interface MasterAccessor {
	UUID getMasterUUID();
	
	void setMasterUUID(UUID masterUUID);
}
