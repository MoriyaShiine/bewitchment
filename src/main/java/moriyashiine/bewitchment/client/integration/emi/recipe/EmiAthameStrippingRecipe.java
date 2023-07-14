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
import moriyashiine.bewitchment.common.recipe.AthameStrippingRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EmiAthameStrippingRecipe extends BasicEmiRecipe {
	private final EmiIngredient log, stripped_log, bark;

	public EmiAthameStrippingRecipe(AthameStrippingRecipe recipe) {
		super(BWEmiIntegration.ATHAME_STRIPPING_CATEGORY, recipe.getId(), 94, 18);
		log = EmiStack.of(recipe.log);
		stripped_log = EmiStack.of(recipe.strippedLog);
		bark = EmiStack.of(EmiPort.getOutput(recipe));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
		widgets.addSlot(log, 0, 0);
		widgets.addSlot(stripped_log, 58, 0);
		widgets.addSlot(bark, 76, 0).recipeContext(this);
	}
}
