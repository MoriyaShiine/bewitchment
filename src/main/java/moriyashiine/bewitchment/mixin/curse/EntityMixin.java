package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.entity.interfaces.InsanityTargetAccessor;
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
	public World world;
	
	@Inject(method = "setOnFireFor", at = @At("HEAD"), cancellable = true)
	private void setOnFireFor(int seconds, CallbackInfo callbackInfo) {
		if (!world.isClient && this instanceof InsanityTargetAccessor && ((InsanityTargetAccessor) this).getInsanityTargetUUID().isPresent()) {
			callbackInfo.cancel();
		}
	}
}
