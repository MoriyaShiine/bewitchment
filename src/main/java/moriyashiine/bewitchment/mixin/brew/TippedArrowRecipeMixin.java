package moriyashiine.bewitchment.mixin.brew;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
	@Inject(method = "craft", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void craft(CraftingInventory inventory, CallbackInfoReturnable<ItemStack> callbackInfo) {
		ItemStack stack = inventory.getStack(1 + inventory.getWidth());
		if (stack.hasNbt() && stack.getNbt().contains("BewitchmentBrew")) {
			int color = PotionUtil.getColor(stack);
			UUID uuid = null;
			String name = null;
			if (stack.getNbt().contains("PolymorphUUID")) {
				uuid = stack.getNbt().getUuid("PolymorphUUID");
				name = stack.getNbt().getString("PolymorphName");
			}
			stack = callbackInfo.getReturnValue();
			stack.getOrCreateNbt().putBoolean("BewitchmentBrew", true);
			stack.getOrCreateNbt().putInt("CustomPotionColor", color);
			if (uuid != null) {
				stack.getOrCreateNbt().putUuid("PolymorphUUID", uuid);
				stack.getOrCreateNbt().putString("PolymorphName", name);
			}
			callbackInfo.setReturnValue(stack);
		}
	}
}
