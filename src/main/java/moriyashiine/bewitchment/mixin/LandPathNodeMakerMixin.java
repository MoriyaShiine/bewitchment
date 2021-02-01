package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker {
	@Inject(method = "method_27138", at = @At("RETURN"), cancellable = true)
	private static void method_27138(BlockState blockState, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && blockState.getBlock() instanceof WitchCauldronBlock) {
			callbackInfo.setReturnValue(true);
		}
	}
}
