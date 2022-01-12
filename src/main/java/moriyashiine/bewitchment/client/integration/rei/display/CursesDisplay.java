/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class CursesDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;

	public CursesDisplay(CurseRecipe recipe) {
		input = EntryIngredients.ofIngredients(recipe.input);
		ItemStack brazier = new ItemStack(BWObjects.BRAZIER).setCustomName(new TranslatableText("curse." + BWRegistries.CURSES.getId(recipe.curse).toString().replace(":", ".")));
		brazier.getOrCreateNbt().putInt("Cost", recipe.cost);
		output = Collections.singletonList(EntryIngredients.of(brazier));
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
		return BWREIPlugin.CURSES;
	}
}
