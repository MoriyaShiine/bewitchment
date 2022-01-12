/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("ConstantConditions")
public class PurityStatusEffect extends StatusEffect {
	public PurityStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient && entity.age % 20 == 0) {
			Registry.STATUS_EFFECT.stream().forEach(effect -> {
				if (effect.getCategory() == StatusEffectCategory.HARMFUL && entity.hasStatusEffect(effect)) {
					StatusEffectInstance currentPurity = entity.getStatusEffect(this);
					if (currentPurity != null) {
						entity.removeStatusEffect(this);
						if (currentPurity.getAmplifier() > 0) {
							entity.addStatusEffect(new StatusEffectInstance(this, currentPurity.getDuration(), currentPurity.getAmplifier() - 1, currentPurity.isAmbient(), currentPurity.shouldShowParticles(), currentPurity.shouldShowIcon()));
						}
						StatusEffectInstance currentNegative = entity.getStatusEffect(effect);
						entity.removeStatusEffect(effect);
						if (currentNegative.getAmplifier() > 0) {
							entity.addStatusEffect(new StatusEffectInstance(effect, currentNegative.getDuration(), currentNegative.getAmplifier() - 1, currentNegative.isAmbient(), currentNegative.shouldShowParticles(), currentNegative.shouldShowIcon()));
						}
					}
				}
			});
		}
	}
}
