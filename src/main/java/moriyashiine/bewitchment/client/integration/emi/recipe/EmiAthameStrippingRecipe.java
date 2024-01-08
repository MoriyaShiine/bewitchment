/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.emi.recipe;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import moriyashiine.bewitchment.client.integration.emi.BWEmiIntegration;
import moriyashiine.bewitchment.common.recipe.AthameStrippingRecipe;

public class EmiAthameStrippingRecipe extends BasicEmiRecipe {
	public EmiAthameStrippingRecipe(AthameStrippingRecipe recipe) {
		super(BWEmiIntegration.ATHAME_STRIPPING_CATEGORY, recipe.getId(), 94, 18);
		inputs.add(EmiStack.of(recipe.log));
		inputs.add(EmiStack.of(recipe.strippedLog));
		outputs.add(EmiStack.of(EmiPort.getOutput(recipe)));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
		widgets.addSlot(inputs.get(0), 0, 0);
		widgets.addSlot(inputs.get(1), 58, 0);
		widgets.addSlot(outputs.get(0), 76, 0).recipeContext(this);
	}
}
