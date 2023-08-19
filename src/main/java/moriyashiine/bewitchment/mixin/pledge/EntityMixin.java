/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private World world;

	@Inject(method = "setRemoved", at = @At("TAIL"))
	private void bewitchment$pledge$markAsRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
		if (!world.isClient && reason.shouldDestroy() && this instanceof Pledgeable pledgeable) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(world);
			universalWorldState.pledgesToRemove.addAll(pledgeable.getPledgedPlayerUUIDs());
			universalWorldState.markDirty();
		}
	}
}
