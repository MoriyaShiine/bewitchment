package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {
	@Inject(method = "transformFireDuration", at = @At("HEAD"), cancellable = true)
	private static void transformFireDuration(LivingEntity entity, int duration, CallbackInfoReturnable<Integer> callbackInfo) {
		if (BewitchmentAPI.isVampire(entity, true)) {
			callbackInfo.setReturnValue(duration);
		}
	}
}
