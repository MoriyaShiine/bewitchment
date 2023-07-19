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

@Environment(EnvType.CLIENT)
public class EmiRitualRecipe extends BasicEmiRecipe {
	public EmiRitualRecipe(RitualRecipe recipe) {
		super(BWEmiIntegration.RITUALS_CATGORY, recipe.getId(), 0, 18);
		for (Ingredient ingredient : recipe.input) {
			if (!ingredient.isEmpty()) {
				inputs.add(EmiIngredient.of(ingredient));
				width += 18;
			}
		}
		width += 58;
		ItemStack chalk = new ItemStack(BWObjects.GOLDEN_CHALK).setCustomName(Text.translatable("ritual." + recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
		chalk.getOrCreateNbt().putString("InnerCircle", "chalk." + Bewitchment.MOD_ID + "." + recipe.inner);
		if (!recipe.outer.isEmpty()) {
			chalk.getNbt().putString("OuterCircle", "chalk." + Bewitchment.MOD_ID + "." + recipe.outer);
		}
		chalk.getNbt().putInt("Cost", recipe.cost);
		if (recipe.runningTime > 0) {
			chalk.getNbt().putInt("RunningTime", recipe.runningTime);
		}
		outputs.add(EmiStack.of(chalk));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int x = 0;
		for (EmiIngredient emiIngredient : inputs) {
			widgets.addSlot(emiIngredient, x, 0);
			x += 18;
		}
		widgets.addTexture(EmiTexture.EMPTY_ARROW, x + 8, 1);
		widgets.addSlot(outputs.get(0), x + 40, 0).recipeContext(this);
	}
}
