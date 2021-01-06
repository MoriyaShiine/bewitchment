package moriyashiine.bewitchment.api.interfaces;

import net.minecraft.entity.LivingEntity;

import java.util.Optional;
import java.util.UUID;

public interface PolymorphAccessor {
	static Optional<PolymorphAccessor> of(LivingEntity entity) {
		return Optional.of(((PolymorphAccessor) entity));
	}
	
	Optional<UUID> getPolymorphUUID();
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	void setPolymorphUUID(Optional<UUID> uuid);
	
	String getPolymorphName();
	
	void setPolymorphName(String name);
}
