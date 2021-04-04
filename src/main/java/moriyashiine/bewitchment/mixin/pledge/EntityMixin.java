package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
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
	public World world;
	
	@Inject(method = "remove", at = @At("TAIL"))
	private void remove(CallbackInfo callbackInfo) {
		if (!world.isClient && this instanceof Pledgeable) {
			BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
			worldState.pledgesToRemove.addAll(((Pledgeable) this).getPledgedPlayerUUIDs());
			worldState.markDirty();
		}
	}
}
