package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.recipe.AthameStrippingRecipe;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class AthameStrippingDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;
	
	public AthameStrippingDisplay(AthameStrippingRecipe recipe) {
		input = Collections.singletonList(EntryIngredients.of(new ItemStack(recipe.log)));
		output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		return input;
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		return output;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return BWREIPlugin.ATHAME_STRIPPING;
	}
}
