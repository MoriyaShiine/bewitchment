package moriyashiine.bewitchment.api.interfaces.entity;

public interface MagicAccessor {
	int MAX_MAGIC = 100;
	
	int getMagic();
	
	void setMagic(int magic);
	
	int getMagicTimer();
	
	void setMagicTimer(int magicTimer);
	
	default boolean fillMagic(int amount, boolean simulate) {
		if (getMagic() < MAX_MAGIC) {
			if (!simulate) {
				setMagic(Math.min(MAX_MAGIC, getMagic() + amount));
			}
			setMagicTimer(60);
			return true;
		}
		return false;
	}
	
	default boolean drainMagic(int amount, boolean simulate) {
		if (getMagic() - amount >= 0) {
			if (!simulate) {
				setMagic(getMagic() - amount);
			}
			setMagicTimer(60);
			return true;
		}
		return false;
	}
}
