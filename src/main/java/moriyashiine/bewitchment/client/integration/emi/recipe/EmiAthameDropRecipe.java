/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.emi.recipe;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import moriyashiine.bewitchment.client.integration.emi.BWEmiIntegration;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EmiAthameDropRecipe extends BasicEmiRecipe {
	private final EmiIngredient input, output;

	public EmiAthameDropRecipe(AthameDropRecipe recipe) {
		super(BWEmiIntegration.ATHAME_DROPS_CATEGORY, recipe.getId(), 76, 18);
		input = EmiStack.of(new ItemStack(Items.SPAWNER).setCustomName(recipe.entity_type.getName()));
		output = EmiStack.of(EmiPort.getOutput(recipe));

	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
		widgets.addSlot(input, 0, 0);
		widgets.addSlot(output, 58, 0).recipeContext(this);
	}
}
