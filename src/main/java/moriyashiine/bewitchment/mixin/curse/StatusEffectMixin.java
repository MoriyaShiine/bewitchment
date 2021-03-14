package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@ModifyConstant(method = "applyUpdateEffect", constant = @Constant(floatValue = 1, ordinal = 2))
	private float applyUpdateEffect(float value, LivingEntity entity) {
		if (((CurseAccessor) entity).hasCurse(BWCurses.SUSCEPTIBILITY) && entity.getHealth() > 3) {
			return 3;
		}
		return value;
	}
}
