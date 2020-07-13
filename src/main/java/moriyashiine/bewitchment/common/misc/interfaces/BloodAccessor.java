package moriyashiine.bewitchment.common.misc.interfaces;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public interface BloodAccessor
{
	Tag<EntityType<?>> HAS_BLOOD = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "has_blood"));
	
	int MAX_BLOOD = 100;
	
	int getBlood();
	
	void setBlood(int blood);
	
	default boolean hasBlood(Entity entity)
	{
		return entity instanceof PlayerEntity || HAS_BLOOD.contains(entity.getType());
	}
	
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