package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class SupportBWSigns {
	@Inject(method = "supports", at = @At("HEAD"), cancellable = true)
	private void supports(Block block, CallbackInfoReturnable<Boolean> info) {
		Object obj = this;
		//noinspection ConstantConditions
		if (obj == BlockEntityType.SIGN && (block == BWObjects.juniper_standing_sign || block == BWObjects.juniper_wall_sign || block == BWObjects.cypress_standing_sign || block == BWObjects.cypress_wall_sign || block == BWObjects.elder_standing_sign || block == BWObjects.elder_wall_sign || block == BWObjects.dragons_blood_standing_sign || block == BWObjects.dragons_blood_wall_sign)) {
			info.setReturnValue(true);
		}
	}
}
