package moriyashiine.bewitchment.mixin.brew;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {
	@Inject(method = "craft", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/potion/PotionUtil;setPotion(Lnet/minecraft/item/ItemStack;Lnet/minecraft/potion/Potion;)Lnet/minecraft/item/ItemStack;"), cancellable = true)
	private static void craft(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (ingredient.hasTag()) {
			NbtCompound tag = ingredient.getTag();
			if (tag.getBoolean("BewitchmentBrew")) {
				ItemStack potionStack = new ItemStack(input.getItem() == Items.GUNPOWDER ? Items.SPLASH_POTION : input.getItem() == Items.DRAGON_BREATH ? Items.LINGERING_POTION : Items.POTION);
				potionStack.getOrCreateTag().copyFrom(tag);
				callbackInfo.setReturnValue(potionStack);
			}
		}
	}
}
