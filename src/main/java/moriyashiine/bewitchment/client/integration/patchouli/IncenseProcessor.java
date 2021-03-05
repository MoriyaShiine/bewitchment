package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class IncenseProcessor implements IComponentProcessor {
	protected IncenseRecipe recipe;
	
	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();
		RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
		recipe = (IncenseRecipe) manager.get(new Identifier(recipeId)).filter(recipe -> recipe.getType().equals(BWRecipeTypes.INCENSE_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}
	
	@Override
	public IVariable process(String key) {
		if (key.equals("header")) {
			return IVariable.from(recipe.effect.getName());
		}
		for (int i = 0; i < recipe.input.size(); i++) {
			if (key.equals("ingredient" + i)) {
				ItemStack[] stack = recipe.input.get(i).getMatchingStacksClient();
				return stack.length > 0 ? IVariable.from(stack[0]) : null;
			}
			
		}
		return null;
	}
}
