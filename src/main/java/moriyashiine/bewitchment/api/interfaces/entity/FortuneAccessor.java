package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.api.registry.Fortune;

import java.util.Optional;

public interface FortuneAccessor {
	static Optional<FortuneAccessor> of(Object entity) {
		if (entity instanceof FortuneAccessor) {
			return Optional.of(((FortuneAccessor) entity));
		}
		return Optional.empty();
	}
	
	Fortune.Instance getFortune();
	
	void setFortune(Fortune.Instance fortune);
}
