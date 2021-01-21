package moriyashiine.bewitchment.api.interfaces.entity;

import java.util.Optional;

public interface FamiliarAccessor {
	static Optional<FamiliarAccessor> of(Object entity) {
		if (entity instanceof FamiliarAccessor) {
			return Optional.of(((FamiliarAccessor) entity));
		}
		return Optional.empty();
	}
	
	boolean getFamiliar();
	
	void setFamiliar(boolean familiar);
}
