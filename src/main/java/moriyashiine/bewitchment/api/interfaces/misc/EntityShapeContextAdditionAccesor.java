package moriyashiine.bewitchment.api.interfaces.misc;

import net.minecraft.entity.Entity;

import java.util.Optional;

public interface EntityShapeContextAdditionAccesor {
	static Optional<EntityShapeContextAdditionAccesor> of(Object context) {
		if (context instanceof EntityShapeContextAdditionAccesor) {
			return Optional.of(((EntityShapeContextAdditionAccesor) context));
		}
		return Optional.empty();
	}
	
	Entity getEntity();
}
