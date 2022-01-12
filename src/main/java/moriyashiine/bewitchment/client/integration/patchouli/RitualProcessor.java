/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class RitualProcessor implements IComponentProcessor {
	protected RitualRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();
		RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
		recipe = (RitualRecipe) manager.get(new Identifier(recipeId)).filter(recipe -> recipe.getType().equals(BWRecipeTypes.RITUAL_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(String key) {
		switch (key) {
			case "header":
				return IVariable.from(new TranslatableText("ritual." + recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
			case "inner":
				return IVariable.wrap(recipe.inner);
			case "outer":
				return IVariable.wrap(recipe.outer);
			case "cost":
				return IVariable.wrap("$(o)" + I18n.translate("bewitchment.tooltip.cost", recipe.cost));
		}
		for (int i = 0; i < recipe.input.size(); i++) {
			if (key.equals("ingredient" + i)) {
				ItemStack[] stack = recipe.input.get(i).getMatchingStacks();
				return stack.length > 0 ? IVariable.from(stack[0]) : null;
			}

		}
		return null;
	}
}
