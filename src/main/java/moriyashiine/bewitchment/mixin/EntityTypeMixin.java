package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
	@Inject(method = "isInvalidSpawn", at = @At("HEAD"), cancellable = true)
	private void isInvalidSpawn(BlockState blockState, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (blockState.getBlock() instanceof WitchCauldronBlock) {
			callbackInfo.setReturnValue(true);
		}
	}
}
