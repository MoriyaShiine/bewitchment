package moriyashiine.bewitchment.common.misc;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BWDataTrackers {
	public static final TrackedData<Integer> BLOOD = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> MAGIC = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	public static final int MAX_BLOOD = 100;
	public static final int MAX_MAGIC = 100;
	
	private static final Tag<EntityType<?>> HAS_BLOOD = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "has_blood"));
	
	public static boolean hasBlood(LivingEntity livingEntity) {
		return HAS_BLOOD.contains(livingEntity.getType());
	}
	
	public static int getBlood(LivingEntity livingEntity) {
		return livingEntity.getDataTracker().get(BLOOD);
	}
	
	public static void setBlood(LivingEntity livingEntity, int amount) {
		livingEntity.getDataTracker().set(BLOOD, amount);
	}
	
	public static boolean fillBlood(LivingEntity livingEntity, int amount, boolean simulate) {
		int blood = getBlood(livingEntity);
		if (blood < MAX_BLOOD) {
			if (!simulate) {
				setBlood(livingEntity, blood + amount);
			}
			return true;
		}
		return false;
	}
	
	public static boolean drainBlood(LivingEntity livingEntity, int amount, boolean simulate) {
		int blood = getBlood(livingEntity);
		if (blood - amount >= 0) {
			if (!simulate) {
				setBlood(livingEntity, blood - amount);
			}
			return true;
		}
		return false;
	}
	
	public static int getMagic(LivingEntity livingEntity) {
		return livingEntity.getDataTracker().get(MAGIC);
	}
	
	public static void setMagic(LivingEntity livingEntity, int amount) {
		livingEntity.getDataTracker().set(MAGIC, amount);
	}
	
	public static boolean fillMagic(LivingEntity livingEntity, int amount, boolean simulate) {
		int magic = getMagic(livingEntity);
		if (magic < MAX_MAGIC) {
			if (!simulate) {
				setMagic(livingEntity, magic + amount);
			}
			return true;
		}
		return false;
	}
	
	public static boolean drainMagic(LivingEntity livingEntity, int amount, boolean simulate) {
		int magic = getMagic(livingEntity);
		if (magic - amount >= 0) {
			if (!simulate) {
				setMagic(livingEntity, magic - amount);
			}
			return true;
		}
		return false;
	}
}