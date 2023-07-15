/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class BWDamageSources {
	public static final RegistryKey<DamageType> MAGIC_COPY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Bewitchment.id("magic_copy"));
	public static final RegistryKey<DamageType> WEDNESDAY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Bewitchment.id("wednesday"));
	public static final RegistryKey<DamageType> DEATH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Bewitchment.id("death"));
	public static final RegistryKey<DamageType> VAMPIRE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Bewitchment.id("vampire"));
	public static final RegistryKey<DamageType> SUN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Bewitchment.id("sun"));

	public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), source, attacker);
	}

	public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity attacker) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), attacker);
	}

	public static DamageSource create(World world, RegistryKey<DamageType> key) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
	}

	public static float handleDamage(LivingEntity entity, DamageSource source, float amount) {
		if (BewitchmentAPI.isWeakToSilver(entity) && BewitchmentAPI.isSourceFromSilver(source)) {
			amount += 4;
		} else if (BewitchmentAPI.isWerewolf(entity, false) && !isEffective(source, false)) {
			amount *= 2 / 3f;
		} else if (BewitchmentAPI.isVampire(entity, true)) {
			amount = handleVampireDamage(entity, source, amount);
		}
		return amount;
	}

	public static boolean isEffective(DamageSource source, boolean vampire) {
		if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) || (vampire && (source.isOf(MAGIC_COPY) || source.isOf(SUN)))) {
			return true;
		}
		Entity attacker = source.getSource();
		if (attacker != null) {
			if (attacker.getType().isIn(BWTags.TAGLOCK_BLACKLIST) || BewitchmentAPI.isVampire(attacker, true) || BewitchmentAPI.isWerewolf(attacker, true)) {
				return true;
			} else if (attacker instanceof LivingEntity && EnchantmentHelper.getEquipmentLevel(Enchantments.SMITE, (LivingEntity) attacker) > 0) {
				return true;
			}
		}
		return BewitchmentAPI.isSourceFromSilver(source);
	}

	private static float handleVampireDamage(LivingEntity entity, DamageSource source, float amount) {
		if (!isEffective(source, true)) {
			if (entity.getHealth() - amount < 1) {
				BloodComponent bloodComponent = BWComponents.BLOOD_COMPONENT.get(entity);
				while (entity.getHealth() - amount <= 0 && bloodComponent.getBlood() > 0) {
					amount--;
					bloodComponent.drainBlood(1, false);
				}
			}
		}
		return amount;
	}
}
