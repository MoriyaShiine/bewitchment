/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;

public class VampireTransformation extends Transformation {
	@Override
	public void onAdded(PlayerEntity player) {
		Registries.STATUS_EFFECT.stream().forEach(effect -> {
			StatusEffectInstance effectInstance = player.getStatusEffect(effect);
			if (effectInstance != null && !player.canHaveStatusEffect(effectInstance)) {
				player.removeStatusEffect(effect);
			}
		});
	}

	@Override
	public void onRemoved(PlayerEntity player) {
		player.removeStatusEffect(StatusEffects.NIGHT_VISION);
	}
}
