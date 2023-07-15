/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class CurseProcessor implements IComponentProcessor {
	protected CurseRecipe recipe;

	@Override
	public void setup(World level, IVariableProvider variables) {
		recipe = (CurseRecipe) level.getRecipeManager().get(new Identifier(variables.get("recipe").asString())).filter(recipe -> recipe.getType().equals(BWRecipeTypes.CURSE_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(World level, String key) {
		if (key.equals("header")) {
			return IVariable.from(Text.translatable("curse." + BWRegistries.CURSE.getId(recipe.curse).toString().replace(":", ".")));
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
