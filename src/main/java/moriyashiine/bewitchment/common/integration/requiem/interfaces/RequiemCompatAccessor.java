package moriyashiine.bewitchment.common.integration.requiem.interfaces;

import moriyashiine.bewitchment.api.registry.Transformation;

public interface RequiemCompatAccessor {
	Transformation getCachedTransformationForRequiem();
	
	void setCachedTransformationForRequiem(Transformation transformation);
	
	boolean getWeakToSilverFromRequiem();
	
	void setWeakToSilverFromRequiem(boolean weakToSilver);
}
