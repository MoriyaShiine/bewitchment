package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.api.registry.Fortune;

public interface FortuneAccessor {
	Fortune.Instance getFortune();
	
	void setFortune(Fortune.Instance fortune);
}
