package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.registry.Registry;

public class VampireTransformation extends Transformation {
	@Override
	public void onAdded(LivingEntity entity) {
		Registry.STATUS_EFFECT.stream().forEach(effect -> {
			StatusEffectInstance effectInstance = entity.getStatusEffect(effect);
			if (effectInstance != null && !entity.canHaveStatusEffect(effectInstance)) {
				entity.removeStatusEffect(effect);
			}
		});
	}
	
	@Override
	public void onRemoved(LivingEntity entity) {
		entity.removeStatusEffect(StatusEffects.NIGHT_VISION);
	}
}
