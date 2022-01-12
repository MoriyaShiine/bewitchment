/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.ritual;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "isWet", at = @At("RETURN"), cancellable = true)
	private void isWet(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(this).getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}

	@Inject(method = "isTouchingWaterOrRain", at = @At("RETURN"), cancellable = true)
	private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(this).getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
}
