/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.TippedArrowRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
	@Inject(method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack stack = recipeInputInventory.getStack(1 + recipeInputInventory.getWidth());
		if (stack.hasNbt() && stack.getNbt().contains("BewitchmentBrew")) {
			int color = PotionUtil.getColor(stack);
			UUID uuid = null;
			String name = null;
			if (stack.getNbt().contains("PolymorphUUID")) {
				uuid = stack.getNbt().getUuid("PolymorphUUID");
				name = stack.getNbt().getString("PolymorphName");
			}
			stack = cir.getReturnValue();
			stack.getOrCreateNbt().putBoolean("BewitchmentBrew", true);
			stack.getOrCreateNbt().putInt("CustomPotionColor", color);
			if (uuid != null) {
				stack.getOrCreateNbt().putUuid("PolymorphUUID", uuid);
				stack.getOrCreateNbt().putString("PolymorphName", name);
			}
			cir.setReturnValue(stack);
		}
	}
}
