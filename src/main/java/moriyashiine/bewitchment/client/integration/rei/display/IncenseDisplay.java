package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

import java.util.Collections;
import java.util.List;

public class IncenseDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;

	public IncenseDisplay(IncenseRecipe recipe) {
		input = EntryIngredients.ofIngredients(recipe.input);
		output = Collections.singletonList(EntryIngredients.of(PotionUtil.setCustomPotionEffects(new ItemStack(BWObjects.BRAZIER), Collections.singletonList(new StatusEffectInstance(recipe.effect, 24000, recipe.amplifier)))));
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
		return BWREIPlugin.INCENSES;
	}
}
