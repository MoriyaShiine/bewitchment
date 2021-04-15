package moriyashiine.bewitchment.common.integration.requiem.interfaces;

import moriyashiine.bewitchment.api.registry.Transformation;

public interface RequiemCompatAccessor {
	Transformation getCachedTransformationForRequiem();
	
	void setCachedTransformationForRequiem(Transformation transformation);
	
	boolean getWeakToSmiteFromRequiem();
	
	void setWeakToSmiteFromRequiem(boolean weakToSmite);
}
