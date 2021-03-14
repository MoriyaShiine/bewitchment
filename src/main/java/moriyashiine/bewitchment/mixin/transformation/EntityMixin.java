package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@ModifyVariable(method = "setPose", at = @At("HEAD"))
	private EntityPose setPose(EntityPose pose) {
		if (((Object) this) instanceof PlayerEntity) {
			if (BewitchmentAPI.isVampire((Entity) (Object) this, false) || BewitchmentAPI.isWerewolf((Entity) (Object) this, false)) {
				if (pose == EntityPose.FALL_FLYING || pose == EntityPose.SWIMMING) {
					return EntityPose.STANDING;
				}
			}
		}
		return pose;
	}
	
	@Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
	private void isFireImmune(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && BewitchmentAPI.isVampire((Entity) (Object) this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}
}
