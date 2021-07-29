package moriyashiine.bewitchment.mixin;

import com.terraformersmc.terraform.boat.impl.TerraformBoatTypeImpl;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("EqualsBetweenInconvertibleTypes")
@Mixin(TerraformBoatTypeImpl.class)
public class TerraformBoatTypeImplMixin {
	@Inject(method = "getItem", at = @At("HEAD"), remap = false, cancellable = true)
	private void getItem(CallbackInfoReturnable<Item> callbackInfo) {
		if (this == ((TerraformBoatItemMixin) BWObjects.JUNIPER_BOAT).bw_getSupplier().get()) {
			callbackInfo.setReturnValue(BWObjects.JUNIPER_BOAT);
		}
		else if (this == ((TerraformBoatItemMixin) BWObjects.CYPRESS_BOAT).bw_getSupplier().get()) {
			callbackInfo.setReturnValue(BWObjects.CYPRESS_BOAT);
		}
		else if (this == ((TerraformBoatItemMixin) BWObjects.ELDER_BOAT).bw_getSupplier().get()) {
			callbackInfo.setReturnValue(BWObjects.ELDER_BOAT);
		}
		else if (this == ((TerraformBoatItemMixin) BWObjects.DRAGONS_BLOOD_BOAT).bw_getSupplier().get()) {
			callbackInfo.setReturnValue(BWObjects.DRAGONS_BLOOD_BOAT);
		}
	}
}
