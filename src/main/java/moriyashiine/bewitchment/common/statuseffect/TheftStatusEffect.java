package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public class TheftStatusEffect extends StatusEffect {
	public TheftStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.age % 20 == 0) {
			entity.world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(3 * (amplifier + 1)), livingEntity -> livingEntity != entity).forEach(livingEntity -> Registry.STATUS_EFFECT.stream().forEach(effect -> {
				if (effect != this && ((StatusEffectAccessor) effect).bw_getType() == StatusEffectType.BENEFICIAL && livingEntity.hasStatusEffect(effect)) {
					//noinspection ConstantConditions
					entity.addStatusEffect(new StatusEffectInstance(livingEntity.getStatusEffect(effect).getEffectType(), livingEntity.getStatusEffect(effect).getDuration() / 2, livingEntity.getStatusEffect(effect).getAmplifier()));
					livingEntity.removeStatusEffect(effect);
				}
			}));
		}
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		entity.removeStatusEffect(BWStatusEffects.SYNCHRONIZED);
	}
}
