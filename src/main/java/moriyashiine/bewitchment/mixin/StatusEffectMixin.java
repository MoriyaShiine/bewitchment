package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.CurseAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@Inject(method = "applyUpdateEffect", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
	private void applyUpdateEffect(LivingEntity entity, int amplifier, CallbackInfo callbackInfo) {
		CurseAccessor.of(entity).ifPresent(curseAccessor -> {
			if (curseAccessor.hasCurse(BWCurses.SUSCEPTIBILITY)) {
				if (entity.getHealth() > 3) {
					entity.damage(DamageSource.MAGIC, 3);
					callbackInfo.cancel();
				}
			}
		});
	}
}
