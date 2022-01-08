package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;
import java.util.stream.Collectors;

public class TheftStatusEffect extends StatusEffect {
	public TheftStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient && entity.age % 20 == 0) {
			entity.world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(3 * (amplifier + 1)), foundEntity -> foundEntity != entity).forEach(livingEntity -> {
				List<StatusEffectInstance> statusEffects = livingEntity.getStatusEffects().stream().filter(instance -> instance.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL && !instance.isAmbient()).toList();
				for (StatusEffectInstance statusEffect : statusEffects) {
					entity.addStatusEffect(new StatusEffectInstance(statusEffect.getEffectType(), statusEffect.getDuration() / 2, statusEffect.getAmplifier()));
					livingEntity.removeStatusEffect(statusEffect.getEffectType());
				}
			});
		}
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		entity.removeStatusEffect(BWStatusEffects.SYNCHRONIZED);
	}
}
