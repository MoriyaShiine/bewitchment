package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.CoffinBlock;
import net.minecraft.block.Block;
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
	private void supports(Block block, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof BedBlockEntity && block instanceof CoffinBlock) {
			callbackInfo.setReturnValue(true);
		}
	}
}
