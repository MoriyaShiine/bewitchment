package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class CorruptionStatusEffect extends StatusEffect {
	public static final Map<StatusEffect, StatusEffect> INVERSE_EFFECTS = new HashMap<>();
	
	static {
		INVERSE_EFFECTS.put(StatusEffects.STRENGTH, StatusEffects.WEAKNESS);
		INVERSE_EFFECTS.put(StatusEffects.REGENERATION, StatusEffects.POISON);
		INVERSE_EFFECTS.put(StatusEffects.NIGHT_VISION, StatusEffects.BLINDNESS);
		INVERSE_EFFECTS.put(StatusEffects.HASTE, StatusEffects.MINING_FATIGUE);
		INVERSE_EFFECTS.put(StatusEffects.SPEED, StatusEffects.SLOWNESS);
		INVERSE_EFFECTS.put(StatusEffects.JUMP_BOOST, BWStatusEffects.SINKING);
		INVERSE_EFFECTS.put(StatusEffects.FIRE_RESISTANCE, BWStatusEffects.IGNITION);
		INVERSE_EFFECTS.put(StatusEffects.WATER_BREATHING, BWStatusEffects.GILLS);
		INVERSE_EFFECTS.put(StatusEffects.SLOW_FALLING, StatusEffects.LEVITATION);
		INVERSE_EFFECTS.put(BWStatusEffects.HARDENING, BWStatusEffects.CORROSION);
		INVERSE_EFFECTS.put(BWStatusEffects.ENCHANTED, BWStatusEffects.INHIBITED);
		INVERSE_EFFECTS.put(BWStatusEffects.NOURISHING, StatusEffects.HUNGER);
	}
	
	public CorruptionStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient) {
			Registry.STATUS_EFFECT.stream().forEach(effect -> {
				if (effect.getType() == StatusEffectType.BENEFICIAL && entity.hasStatusEffect(effect) && !entity.getStatusEffect(effect).isAmbient()) {
					StatusEffect inverse = INVERSE_EFFECTS.get(effect);
					StatusEffectInstance inverseEffect = null;
					if (inverse != null) {
						StatusEffectInstance goodEffect = entity.getStatusEffect(effect);
						inverseEffect = new StatusEffectInstance(inverse, goodEffect.getDuration(), goodEffect.getAmplifier(), goodEffect.isAmbient(), goodEffect.shouldShowParticles(), goodEffect.shouldShowIcon());
					}
					entity.removeStatusEffect(effect);
					if (inverseEffect != null) {
						entity.addStatusEffect(inverseEffect);
					}
				}
			});
		}
	}
}
