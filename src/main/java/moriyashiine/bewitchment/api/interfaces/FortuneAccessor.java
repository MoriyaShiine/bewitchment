package moriyashiine.bewitchment.api.interfaces;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public interface FortuneAccessor {
	static Optional<FortuneAccessor> of(LivingEntity entity) {
		return Optional.of(((FortuneAccessor) entity));
	}
	
	Fortune.Instance getFortune();
	
	void setFortune(Fortune.Instance fortune);
}
