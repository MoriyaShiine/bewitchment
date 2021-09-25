package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.component.FullInvisibilityComponent;
import moriyashiine.bewitchment.common.entity.component.MinionComponent;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> extends TrackTargetGoal {
	@Shadow
	protected LivingEntity targetEntity;
	
	public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
		super(mob, checkVisibility);
	}
	
	@Inject(method = "findClosestTarget", at = @At("TAIL"))
	private void findClosestTarget(CallbackInfo callbackInfo) {
		if (targetEntity instanceof PlayerEntity player && FullInvisibilityComponent.get(player).isFullInvisible()) {
			targetEntity = null;
		}
		else if (MinionComponent.get(mob).getMaster() == null && mob.isUndead() && targetEntity != null && BWUtil.getArmorPieces(targetEntity, stack -> stack.getItem() == BWObjects.HARBINGER) > 0) {
			targetEntity = null;
		}
	}
}
