package moriyashiine.bewitchment.api.accessor;

public interface MagicAccessor {
	int MAX_MAGIC = 100;
	
	int getMagic();
	
	void setMagic(int magic);
	
	default boolean fillMagic(int amount, boolean simulate) {
		int magic = getMagic();
		if (magic < MAX_MAGIC) {
			if (!simulate) {
				setMagic(magic + amount);
			}
			return true;
		}
		return false;
	}
	
	default boolean drainMagic(int amount, boolean simulate) {
		int magic = getMagic();
		if (magic - amount >= 0) {
			if (!simulate) {
				setMagic(magic - amount);
			}
			return true;
		}
		return false;
	}
}