package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
	@Inject(method = "isValidSpawn", at = @At("RETURN"), cancellable = true)
	private static void isValidSpawn(ServerWorld world, MobEntity entity, double squaredDistance, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && entity instanceof Monster) {
			for (Long longPos : BWWorldState.get(world).glowingBrambles) {
				if (new Box(entity.getBlockPos()).expand(16).intersects(new Box(BlockPos.fromLong(longPos)))) {
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}
