/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class OilCraftingProcessor implements IComponentProcessor {
	protected OilRecipe recipe;

	@Override
	public void setup(World level, IVariableProvider variables) {
		recipe = (OilRecipe) level.getRecipeManager().get(new Identifier(variables.get("recipe").asString())).filter(recipe -> recipe.getType().equals(BWRecipeTypes.OIL_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(World level, String key) {
		if (key.equals("output")) {
			return IVariable.from(recipe.getOutput(level.getRegistryManager()));
		} else if (key.equals("header")) {
			return IVariable.from(recipe.getOutput(level.getRegistryManager()).getName());
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
