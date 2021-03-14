package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EscapeDangerGoal.class)
public abstract class EscapeDangerGoalMixin extends Goal {
	@Shadow
	@Final
	protected PathAwareEntity mob;
	
	@Inject(method = "canStart", at = @At("RETURN"), cancellable = true)
	private void canStart(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && !mob.world.isClient && mob.age % 20 == 0 && !mob.world.getEntitiesByClass(LivingEntity.class, new Box(mob.getBlockPos()).expand(8), entity -> mob.canSee(entity) && entity.isAlive() && ((CurseAccessor) entity).hasCurse(BWCurses.APATHY)).isEmpty()) {
			callbackInfo.setReturnValue(true);
		}
	}
}
