package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;

public class InvigoratingStatusEffect extends StatusEffect {
	public InvigoratingStatusEffect(StatusEffectType type, int color) {
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
		if (entity instanceof PlayerEntity) {
			BewitchmentAPI.fillMagic((PlayerEntity) entity, 20 * (amplifier + 1), false);
		}
	}
}
