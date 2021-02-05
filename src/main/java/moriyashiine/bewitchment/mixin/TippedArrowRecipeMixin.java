package moriyashiine.bewitchment.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
	@Inject(method = "craft", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void craft(CraftingInventory inventory, CallbackInfoReturnable<ItemStack> callbackInfo) {
		ItemStack stack = inventory.getStack(1 + inventory.getWidth());
		if (stack.hasTag() && stack.getTag().contains("BewitchmentBrew")) {
			int color = PotionUtil.getColor(stack);
			stack = callbackInfo.getReturnValue();
			stack.getOrCreateTag().putBoolean("BewitchmentBrew", true);
			stack.getOrCreateTag().putInt("CustomPotionColor", color);
			callbackInfo.setReturnValue(stack);
		}
	}
}
