package moriyashiine.bewitchment.api.interfaces;

public interface MagicAccessor {
	int MAX_PLAYER_MAGIC = 100;
	
	int getMagic();
	
	void setMagic(int magic);
	
	default boolean fillMagicPlayer(int amount, boolean simulate) {
		return fillMagic(MAX_PLAYER_MAGIC, amount, simulate);
	}
	
	default boolean fillMagic(int maxMagic, int amount, boolean simulate) {
		int magic = getMagic();
		if (magic < maxMagic) {
			if (!simulate) {
				setMagic(Math.min(magic + amount, maxMagic));
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