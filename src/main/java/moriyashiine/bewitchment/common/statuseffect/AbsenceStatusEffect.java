package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public class AbsenceStatusEffect extends StatusEffect {
	public AbsenceStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		Registry.STATUS_EFFECT.stream().forEach(effect -> {
			if (effect != this && (amplifier == 0 || (amplifier > 0 && ((StatusEffectAccessor) effect).bw_getType() == StatusEffectType.HARMFUL))) {
				entity.removeStatusEffect(effect);
			}
		});
	}
}
