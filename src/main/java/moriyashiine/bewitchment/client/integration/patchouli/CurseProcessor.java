/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class CurseProcessor implements IComponentProcessor {
	protected CurseRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();
		RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
		recipe = (CurseRecipe) manager.get(new Identifier(recipeId)).filter(recipe -> recipe.getType().equals(BWRecipeTypes.CURSE_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(String key) {
		if (key.equals("header")) {
			return IVariable.from(new TranslatableText("curse." + BWRegistries.CURSES.getId(recipe.curse).toString().replace(":", ".")));
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
