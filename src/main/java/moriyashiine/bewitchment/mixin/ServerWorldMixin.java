package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "spawnEntity", at = @At("HEAD"))
	private void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (entity instanceof LightningEntity) {
			LivingEntity closest = null;
			for (LivingEntity found : entity.world.getEntitiesByClass(LivingEntity.class, new Box(entity.getBlockPos()).expand(256), foundEntity -> foundEntity.isAlive() && CurseAccessor.of(foundEntity).orElse(null).hasCurse(BWCurses.LIGHTNING_ROD))) {
				if (closest == null || found.distanceTo(entity) < closest.distanceTo(entity)) {
					closest = found;
				}
			}
			if (closest != null) {
				entity.updatePositionAndAngles(closest.getX(), closest.getY(), closest.getZ(), 0, entity.world.random.nextInt(360));
			}
		}
	}
}
