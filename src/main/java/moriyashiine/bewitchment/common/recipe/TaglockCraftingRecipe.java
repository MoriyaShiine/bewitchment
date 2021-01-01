package moriyashiine.bewitchment.common.recipe;

import moriyashiine.bewitchment.common.item.ContractItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class TaglockCraftingRecipe extends SpecialCraftingRecipe {
	public TaglockCraftingRecipe(Identifier id) {
		super(id);
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean foundTaglock = false, foundCraftable = false;
		int foundItems = 0;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() instanceof TaglockItem && stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID")) {
				if (!foundTaglock) {
					foundTaglock = true;
				}
				foundItems++;
			}
			else if (isTaglockCraftable(stack)) {
				if (!foundCraftable) {
					foundCraftable = true;
				}
				foundItems++;
			}
		}
		return foundTaglock && foundCraftable && foundItems == 2;
	}
	
	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack taglock = null, craftedStack = null;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() instanceof TaglockItem) {
				taglock = stack.copy();
			}
			else if (isTaglockCraftable(stack)) {
				craftedStack = stack.copy();
			}
		}
		craftedStack.getOrCreateTag().putUuid("OwnerUUID", taglock.getOrCreateTag().getUuid("OwnerUUID"));
		craftedStack.getOrCreateTag().putString("OwnerName", taglock.getOrCreateTag().getString("OwnerName"));
		return craftedStack;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.TAGLOCK_CRAFTING_SERIALIZER;
	}
	
	private static boolean isTaglockCraftable(ItemStack stack) {
		if (stack.getItem() instanceof ContractItem) {
			return !(stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID"));
		}
		return false;
	}
}
