package moriyashiine.bewitchment.api.accessor;

import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;

import java.util.Optional;

public interface EntityShapeContextAdditionAccesor {
	static Optional<EntityShapeContextAdditionAccesor> of(EntityShapeContext context) {
		if (context instanceof EntityShapeContextAdditionAccesor) {
			return Optional.of(((EntityShapeContextAdditionAccesor) context));
		}
		return Optional.empty();
	}
	
	Entity getEntity();
}
