package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

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
	public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
		Registry.STATUS_EFFECT.stream().forEach(effect -> {
			if (((StatusEffectAccessor) effect).bw_getType() == StatusEffectType.BENEFICIAL && target.hasStatusEffect(effect) && !target.getStatusEffect(effect).isAmbient()) {
				StatusEffect inverse = INVERSE_EFFECTS.get(effect);
				StatusEffectInstance inverseEffect = null;
				if (inverse != null) {
					StatusEffectInstance goodEffect = target.getStatusEffect(effect);
					inverseEffect = new StatusEffectInstance(inverse, goodEffect.getDuration(), goodEffect.getAmplifier(), goodEffect.isAmbient(), goodEffect.shouldShowParticles(), goodEffect.shouldShowIcon());
				}
				target.removeStatusEffect(effect);
				if (inverseEffect != null) {
					target.addStatusEffect(inverseEffect);
				}
			}
		});
	}
}
