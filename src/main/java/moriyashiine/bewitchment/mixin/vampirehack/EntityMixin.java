/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.vampirehack;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
	private void bewitchment$makeVampiresNotImmuneToFire(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isVampire(Entity.class.cast(this), true)) {
			callbackInfo.setReturnValue(false);
		}
	}
}
