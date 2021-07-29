package moriyashiine.bewitchment.mixin;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.impl.item.TerraformBoatItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(TerraformBoatItem.class)
public interface TerraformBoatItemMixin {
	@Accessor(value = "boatSupplier", remap = false)
	Supplier<TerraformBoatType> bw_getSupplier();
}
