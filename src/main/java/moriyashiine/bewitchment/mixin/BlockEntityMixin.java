package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.CoffinBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(BlockEntityType.class)
public class BlockEntityMixin {
	@Inject(method = "supports", at = @At("HEAD"), cancellable = true)
	private void supports(BlockState state, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof BedBlockEntity && state.getBlock() instanceof CoffinBlock) {
			callbackInfo.setReturnValue(true);
		}
	}
}
