package moriyashiine.bewitchment.api.interfaces;

import java.util.Optional;

public interface BloodAccessor {
	int MAX_BLOOD = 100;
	
	static Optional<BloodAccessor> of(Object entity) {
		if (entity instanceof BloodAccessor) {
			return Optional.of(((BloodAccessor) entity));
		}
		return Optional.empty();
	}
	
	int getBlood();
	
	void setBlood(int blood);
	
	default boolean fillBlood(int amount, boolean simulate) {
		if (getBlood() < MAX_BLOOD) {
			if (!simulate) {
				setBlood(Math.min(MAX_BLOOD, getBlood() + amount));
			}
			return true;
		}
		return false;
	}
	
	default boolean drainBlood(int amount, boolean simulate) {
		if (getBlood() - amount >= 0) {
			if (!simulate) {
				setBlood(getBlood() - amount);
			}
			return true;
		}
		return false;
	}
}
