package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class SusceptibilityCurse extends Curse {
	public SusceptibilityCurse(Type type) {
		super(type);
	}
	
	@Override
	public void tick(LivingEntity target) {
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 19, 1, true, false));
		target.addStatusEffect(new StatusEffectInstance(BWStatusEffects.SINKING, 19, 0, true, false));
	}
}
