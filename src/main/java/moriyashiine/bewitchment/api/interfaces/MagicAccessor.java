package moriyashiine.bewitchment.api.interfaces;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public interface MagicAccessor {
	int MAX_MAGIC = 10000;
	
	static Optional<MagicAccessor> of(PlayerEntity player) {
		return Optional.of(((MagicAccessor) player));
	}
	
	int getMagic();
	
	void setMagic(int magic);
	
	int getMagicTimer();
	
	void setMagicTimer(int magicTimer);
	
	default boolean fillMagic(int amount, boolean simulate) {
		if (getMagic() < MAX_MAGIC) {
			if (!simulate) {
				setMagic(Math.min(MAX_MAGIC, getMagic() + amount));
				setMagicTimer(60);
			}
			return true;
		}
		return false;
	}
	
	default boolean drainMagic(int amount, boolean simulate) {
		if (getMagic() - amount >= 0) {
			if (!simulate) {
				setMagic(getMagic() - amount);
				setMagicTimer(60);
			}
			return true;
		}
		return false;
	}
}
