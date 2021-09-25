package moriyashiine.bewitchment.common.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class IgnitionStatusEffect extends StatusEffect {
	public IgnitionStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		entity.setOnFireFor(1);
	}
}
