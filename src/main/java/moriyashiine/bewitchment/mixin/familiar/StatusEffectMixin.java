package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@Inject(method = "applyUpdateEffect", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
	private void applyUpdateEffect(LivingEntity entity, int amplifier, CallbackInfo callbackInfo) {
		if (entity instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) entity) == BWEntityTypes.SNAKE) {
			BewitchmentAPI.fillMagic((PlayerEntity) entity, 1, false);
			callbackInfo.cancel();
		}
	}
}
