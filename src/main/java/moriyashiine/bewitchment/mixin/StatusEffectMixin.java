package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@ModifyConstant(method = "applyUpdateEffect", constant = @Constant(floatValue = 1, ordinal = 2))
	private float applyUpdateEffect(float value, LivingEntity entity) {
		CurseAccessor curseAccessor = CurseAccessor.of(entity).orElse(null);
		if (curseAccessor != null && curseAccessor.hasCurse(BWCurses.SUSCEPTIBILITY) && entity.getHealth() > 3) {
			return 3;
		}
		return value;
	}
	
	@Inject(method = "applyUpdateEffect", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
	private void damage(LivingEntity entity, int amplifier, CallbackInfo callbackInfo) {
		if (entity instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) entity) == BWEntityTypes.SNAKE) {
			MagicAccessor.of(entity).ifPresent(magicAccessor -> {
				magicAccessor.fillMagic(50, false);
			});
			callbackInfo.cancel();
		}
	}
}
