package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.recipe.OilRecipe;

import java.util.Collections;
import java.util.List;

public class OilCraftingDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;
	
	public OilCraftingDisplay(OilRecipe recipe) {
		input = EntryIngredients.ofIngredients(recipe.input);
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
		return BWREIPlugin.OIL_CRAFTING;
	}
}
