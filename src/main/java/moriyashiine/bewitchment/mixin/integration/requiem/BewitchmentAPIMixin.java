package moriyashiine.bewitchment.mixin.integration.requiem;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.integration.requiem.interfaces.RequiemCompatAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BewitchmentAPI.class, remap = false)
public class BewitchmentAPIMixin {
	@Inject(method = "isWeakToSilver", at = @At("HEAD"), cancellable = true)
	private static void isWeakToSilver(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (Bewitchment.isRequiemLoaded && livingEntity instanceof RequiemCompatAccessor && ((RequiemCompatAccessor) livingEntity).getWeakToSilverFromRequiem()) {
			callbackInfo.setReturnValue(true);
		}
	}
}
