package moriyashiine.bewitchment.common.recipe.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.world.World;

public abstract class BWRecipe implements Recipe<Inventory> {
	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return false;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return false;
	}
}
