package moriyashiine.bewitchment.common.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class MortalCoilStatusEffect extends StatusEffect {
	public MortalCoilStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		//noinspection ConstantConditions
		if (entity.getStatusEffect(this).getDuration() == 1) {
			entity.damage(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
		}
	}
}
