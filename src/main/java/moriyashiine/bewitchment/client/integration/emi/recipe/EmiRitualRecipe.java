/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.emi.recipe;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import moriyashiine.bewitchment.client.integration.emi.BWEmiIntegration;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EmiRitualRecipe extends BasicEmiRecipe {
	private final List<EmiIngredient> input;
	private final EmiIngredient output;

	public EmiRitualRecipe(RitualRecipe recipe) {
		super(BWEmiIntegration.RITUALS_CATGORY, recipe.getId(), 0, 18);
		input = new ArrayList<>();
		for (Ingredient ingredient : recipe.input) {
			if (!ingredient.isEmpty()) {
				input.add(EmiIngredient.of(ingredient));
				width += 18;
			}
		}
		width += 58;
		ItemStack chalk = new ItemStack(BWObjects.GOLDEN_CHALK).setCustomName(Text.translatable("ritual." + recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
		chalk.getOrCreateNbt().putString("InnerCircle", "chalk." + Bewitchment.MODID + "." + recipe.inner);
		if (!recipe.outer.isEmpty()) {
			chalk.getNbt().putString("OuterCircle", "chalk." + Bewitchment.MODID + "." + recipe.outer);
		}
		chalk.getNbt().putInt("Cost", recipe.cost);
		if (recipe.runningTime > 0) {
			chalk.getNbt().putInt("RunningTime", recipe.runningTime);
		}
		output = EmiStack.of(chalk);
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int x = 0;
		for (EmiIngredient emiIngredient : input) {
			widgets.addSlot(emiIngredient, x, 0);
			x += 18;
		}
		widgets.addTexture(EmiTexture.EMPTY_ARROW, x + 8, 1);
		widgets.addSlot(output, x + 40, 0).recipeContext(this);
	}
}
