/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.emi;

import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.runtime.EmiDrawContext;
import moriyashiine.bewitchment.client.integration.emi.recipe.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BWEmiIntegration implements EmiPlugin {
	private static final EmiStack ATHAME_WORKSTATION = EmiStack.of(BWObjects.ATHAME);
	private static final EmiStack GOLDEN_CHALK_WORKSTATION = EmiStack.of(BWObjects.GOLDEN_CHALK);
	private static final EmiStack WITCH_CAULDRON_WORKSTATION = EmiStack.of(BWObjects.WITCH_CAULDRON);
	private static final EmiStack BRAZIER_WORKSTATION = EmiStack.of(BWObjects.BRAZIER);

	public static final EmiRecipeCategory ATHAME_STRIPPING_CATEGORY = new EmiRecipeCategory(Bewitchment.id("athame_stripping"), ATHAME_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	public static final EmiRecipeCategory ATHAME_DROPS_CATEGORY = new EmiRecipeCategory(Bewitchment.id("athame_drops"), ATHAME_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	public static final EmiRecipeCategory RITUALS_CATGORY = new EmiRecipeCategory(Bewitchment.id("rituals"), GOLDEN_CHALK_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	public static final EmiRecipeCategory OIL_CRAFTING_CATEGORY = new EmiRecipeCategory(Bewitchment.id("oil_crafting"), WITCH_CAULDRON_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	public static final EmiRecipeCategory CAULDRON_BREWING_CATEGORY = new EmiRecipeCategory(Bewitchment.id("cauldron_brewing"), WITCH_CAULDRON_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	public static final EmiRecipeCategory INCENSE_CATEGORY = new EmiRecipeCategory(Bewitchment.id("incenses"), BRAZIER_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	public static final EmiRecipeCategory CURSE_CATEGORY = new EmiRecipeCategory(Bewitchment.id("curses"), BRAZIER_WORKSTATION, (draw, x, y, delta) -> {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(EmiRenderHelper.WIDGETS, x, y, 160, 240, 16, 16);
	}, EmiRecipeSorting.compareInputThenOutput());

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(ATHAME_STRIPPING_CATEGORY);
		registry.addWorkstation(ATHAME_STRIPPING_CATEGORY, ATHAME_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_STRIPPING_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiAthameStrippingRecipe(recipe)));

		registry.addCategory(ATHAME_DROPS_CATEGORY);
		registry.addWorkstation(ATHAME_DROPS_CATEGORY, ATHAME_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiAthameDropRecipe(recipe)));

		registry.addCategory(RITUALS_CATGORY);
		registry.addWorkstation(RITUALS_CATGORY, GOLDEN_CHALK_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.RITUAL_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiRitualRecipe(recipe)));

		registry.addCategory(OIL_CRAFTING_CATEGORY);
		registry.addWorkstation(OIL_CRAFTING_CATEGORY, WITCH_CAULDRON_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiOilRecipe(recipe)));

		registry.addCategory(CAULDRON_BREWING_CATEGORY);
		registry.addWorkstation(CAULDRON_BREWING_CATEGORY, WITCH_CAULDRON_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiCauldronBrewingRecipe(recipe)));

		registry.addCategory(INCENSE_CATEGORY);
		registry.addWorkstation(INCENSE_CATEGORY, BRAZIER_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.INCENSE_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiIncenseRecipe(recipe)));

		registry.addCategory(CURSE_CATEGORY);
		registry.addWorkstation(CURSE_CATEGORY, BRAZIER_WORKSTATION);
		registry.getRecipeManager().listAllOfType(BWRecipeTypes.CURSE_RECIPE_TYPE).forEach(recipe -> registry.addRecipe(new EmiCurseRecipe(recipe)));
	}
}
