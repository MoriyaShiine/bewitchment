package moriyashiine.bewitchment.api.interfaces;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;
import java.util.UUID;

public interface PolymorphAccessor {
	static Optional<PolymorphAccessor> of(PlayerEntity player) {
		return Optional.of(((PolymorphAccessor) player));
	}
	
	Optional<UUID> getPolymorphUUID();
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	void setPolymorphUUID(Optional<UUID> uuid);
	
	String getPolymorphName();
	
	void setPolymorphName(String name);
}
