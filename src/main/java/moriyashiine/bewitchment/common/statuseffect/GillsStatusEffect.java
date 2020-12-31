package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.mixin.LivingEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class GillsStatusEffect extends StatusEffect {
	public GillsStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.isSubmergedInWater() && !entity.world.hasRain(entity.getBlockPos())) {
			entity.setAir(((LivingEntityAccessor) entity).bw_getNextAirUnderwater(entity.getAir() - ((LivingEntityAccessor) entity).bw_getNextAirOnLand(0)));
			if (entity.getAir() == -20) {
				entity.damage(DamageSource.GENERIC, 2);
				entity.setAir(0);
			}
		}
		else if (entity.getAir() < entity.getMaxAir()) {
			entity.setAir(((LivingEntityAccessor) entity).bw_getNextAirOnLand(entity.getAir()));
		}
	}
}
