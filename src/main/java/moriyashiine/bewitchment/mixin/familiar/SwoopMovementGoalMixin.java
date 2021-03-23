package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PhantomEntity.SwoopMovementGoal.class)
public class SwoopMovementGoalMixin {
	@Inject(method = "shouldContinue", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void shouldContinue(CallbackInfoReturnable<Boolean> callbackInfo, LivingEntity livingEntity) {
		if (livingEntity instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) livingEntity) == EntityType.CAT) {
			callbackInfo.setReturnValue(false);
		}
	}
}
