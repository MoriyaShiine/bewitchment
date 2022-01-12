/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.common.recipe.CauldronBrewingRecipe;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;

import java.util.Collections;
import java.util.List;

public class CauldronBrewingDisplay implements Display {
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;

	public CauldronBrewingDisplay(CauldronBrewingRecipe recipe) {
		input = Collections.singletonList(EntryIngredients.ofIngredient(recipe.input));
		List<StatusEffectInstance> effects = Collections.singletonList(new StatusEffectInstance(recipe.output, recipe.time));
		ItemStack potion = PotionUtil.setCustomPotionEffects(new ItemStack(Items.POTION), effects);
		potion.getOrCreateNbt().putInt("CustomPotionColor", PotionUtil.getColor(effects));
		potion.getOrCreateNbt().putBoolean("BewitchmentBrew", true);
		output = Collections.singletonList(EntryIngredients.of(potion));
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
		return BWREIPlugin.CAULDRON_BREWING;
	}
}
