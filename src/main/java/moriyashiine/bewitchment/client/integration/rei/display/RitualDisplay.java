/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;

public class RitualDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;

	public RitualDisplay(RitualRecipe recipe) {
		input = EntryIngredients.ofIngredients(recipe.input);
		ItemStack chalk = new ItemStack(BWObjects.GOLDEN_CHALK).setCustomName(new TranslatableText("ritual." + recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
		chalk.getOrCreateNbt().putString("InnerCircle", "chalk." + Bewitchment.MODID + "." + recipe.inner);
		if (!recipe.outer.isEmpty()) {
			chalk.getOrCreateNbt().putString("OuterCircle", "chalk." + Bewitchment.MODID + "." + recipe.outer);
		}
		chalk.getOrCreateNbt().putInt("Cost", recipe.cost);
		if (recipe.runningTime > 0) {
			chalk.getOrCreateNbt().putInt("RunningTime", recipe.runningTime);
		}
		output = Collections.singletonList(EntryIngredients.of(chalk));
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
		return BWREIPlugin.RITUALS;
	}
}
