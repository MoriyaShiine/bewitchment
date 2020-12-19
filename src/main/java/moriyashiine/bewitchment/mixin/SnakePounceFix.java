package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PounceAtTargetGoal.class)
public abstract class SnakePounceFix {
	@Shadow
	@Final
	private MobEntity mob;
	
	@Inject(method = "start", at = @At("TAIL"), cancellable = true)
	private void getCollisionShape(CallbackInfo callbackInfo) {
		if (mob instanceof SnakeEntity) {
			((SnakeEntity) mob).toggleAttack(true);
		}
	}
}
