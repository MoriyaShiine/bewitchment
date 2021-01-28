package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("ConstantConditions")
public class PurityStatusEffect extends StatusEffect {
	public PurityStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.age % 20 == 0) {
			Registry.STATUS_EFFECT.stream().forEach(effect -> {
				if (((StatusEffectAccessor) effect).bw_getType() == StatusEffectType.HARMFUL && entity.hasStatusEffect(effect)) {
					entity.removeStatusEffect(effect);
					StatusEffectInstance current = entity.getStatusEffect(this);
					StatusEffectInstance down = new StatusEffectInstance(this, current.getDuration(), amplifier - 1, current.isAmbient(), current.shouldShowParticles(), current.shouldShowIcon());
					entity.removeStatusEffect(this);
					if (amplifier > 0) {
						entity.addStatusEffect(down);
					}
				}
			});
		}
	}
}
