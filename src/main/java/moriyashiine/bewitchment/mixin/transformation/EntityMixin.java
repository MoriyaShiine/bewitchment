/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@ModifyVariable(method = "setPose", at = @At("HEAD"), argsOnly = true)
	private EntityPose modifySetPose(EntityPose pose) {
		if (((Object) this) instanceof PlayerEntity player && (BewitchmentAPI.isVampire(player, false) || BewitchmentAPI.isWerewolf(player, false)) && (pose == EntityPose.FALL_FLYING || pose == EntityPose.SWIMMING)) {
			return EntityPose.STANDING;
		}
		return pose;
	}
}
