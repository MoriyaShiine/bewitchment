package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public World world;
	
	@Shadow
	public abstract UUID getUuid();
	
	@Inject(method = "remove", at = @At("TAIL"))
	private void remove(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(world);
			for (int i = universalWorldState.familiars.size() - 1; i >= 0; i--) {
				if (getUuid().equals(universalWorldState.familiars.get(i).getRight().getUuid("UUID"))) {
					universalWorldState.familiars.remove(i);
					universalWorldState.markDirty();
					break;
				}
			}
		}
	}
}
