/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@Inject(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHungerManager()Lnet/minecraft/entity/player/HungerManager;"))
	private void applyUpdateEffect(LivingEntity entity, int amplifier, CallbackInfo ci) {
		if (BewitchmentAPI.isVampire(entity, true)) {
			BWComponents.BLOOD_COMPONENT.maybeGet(entity).ifPresent(bloodComponent -> {
				if (bloodComponent.getBlood() < BloodComponent.MAX_BLOOD) {
					bloodComponent.fillBlood(amplifier + 1, false);
				}
			});
		}
	}
}
