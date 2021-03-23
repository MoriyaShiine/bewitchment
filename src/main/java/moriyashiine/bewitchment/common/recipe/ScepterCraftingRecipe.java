package moriyashiine.bewitchment.common.recipe;

import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class ScepterCraftingRecipe extends SpecialCraftingRecipe {
	public ScepterCraftingRecipe(Identifier id) {
		super(id);
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean foundScepter = false, foundPotion = false;
		int foundItems = 0;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() instanceof ScepterItem) {
				if (!foundScepter) {
					foundScepter = true;
				}
				foundItems++;
			}
			else if (stack.getItem() instanceof SplashPotionItem && (!PotionUtil.getPotionEffects(stack).isEmpty() || !PotionUtil.getCustomPotionEffects(stack).isEmpty())) {
				if (!foundPotion) {
					foundPotion = true;
				}
				foundItems++;
			}
		}
		return foundScepter && foundPotion && foundItems == 2;
	}
	
	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack scepter = null, potion = null;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() instanceof ScepterItem) {
				scepter = stack.copy();
			}
			else if (stack.getItem() instanceof SplashPotionItem) {
				potion = stack.copy();
			}
		}
		scepter.getOrCreateTag().putInt("PotionUses", 4);
		PotionUtil.setCustomPotionEffects(scepter, !PotionUtil.getCustomPotionEffects(potion).isEmpty() ? PotionUtil.getCustomPotionEffects(potion) : PotionUtil.getPotionEffects(potion));
		if (potion.getOrCreateTag().contains("PolymorphUUID")) {
			scepter.getOrCreateTag().putUuid("PolymorphUUID", potion.getOrCreateTag().getUuid("PolymorphUUID"));
			scepter.getOrCreateTag().putString("PolymorphName", potion.getOrCreateTag().getString("PolymorphName"));
		}
		return scepter;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.SCEPTER_CRAFTING_SERIALIZER;
	}
}
