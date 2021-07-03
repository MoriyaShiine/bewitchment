package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.IntStream;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Inject(method = "getAvailableSlots", at = @At("HEAD"), cancellable = true)
	private static void getAvailableSlots(Inventory inventory, Direction side, CallbackInfoReturnable<IntStream> callbackInfo) {
		if (inventory instanceof BrazierBlockEntity) {
			callbackInfo.setReturnValue(IntStream.empty());
		}
	}
}
