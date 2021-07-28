package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Collections;
import java.util.List;

public class AthameDropDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;
	
	public AthameDropDisplay(AthameDropRecipe recipe) {
		input = Collections.singletonList(EntryIngredients.of(new ItemStack(Items.SPAWNER).setCustomName(recipe.entity_type.getName())));
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
		return BWREIPlugin.ATHAME_DROPS;
	}
}
