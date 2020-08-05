package moriyashiine.bewitchment.api.interfaces;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface BloodAccessor {
	static Optional<BloodAccessor> get(Entity entity) {
		if (entity instanceof BloodAccessor && hasBlood(entity)) {
			return Optional.of((BloodAccessor) entity);
		}
		return Optional.empty();
	}
	
	static boolean hasBlood(Entity entity) {
		return entity instanceof PlayerEntity || HAS_BLOOD.contains(entity.getType());
	}
	
	Tag<EntityType<?>> HAS_BLOOD = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "has_blood"));
	
	int MAX_BLOOD = 100;
	
	int getBlood();
	
	void setBlood(int blood);
	
	default boolean fillBlood(int amount, boolean simulate) {
		int magic = getBlood();
		if (magic < MAX_BLOOD) {
			if (!simulate) {
				setBlood(magic + amount);
			}
			return true;
		}
		return false;
	}
	
	default boolean drainBlood(int amount, boolean simulate) {
		int magic = getBlood();
		if (magic - amount >= 0) {
			if (!simulate) {
				setBlood(magic - amount);
			}
			return true;
		}
		return false;
	}
}
