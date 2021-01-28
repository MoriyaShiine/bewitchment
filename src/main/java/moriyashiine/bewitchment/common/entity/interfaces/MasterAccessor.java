package moriyashiine.bewitchment.common.entity.interfaces;

import java.util.UUID;

public interface MasterAccessor {
	UUID getMasterUUID();
	
	void setMasterUUID(UUID masterUUID);
}
