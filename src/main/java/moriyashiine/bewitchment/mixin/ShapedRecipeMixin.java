package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.item.SigilItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
	private static final Random RANDOM = new Random();
	
	@Inject(method = "craft", at = @At("RETURN"))
	private void craft(CallbackInfoReturnable<ItemStack> callbackInfo) {
		ItemStack stack = callbackInfo.getReturnValue();
		if (stack.getItem() instanceof SigilItem) {
			stack.getOrCreateTag().putInt("Type", RANDOM.nextInt(10));
		}
	}
}
