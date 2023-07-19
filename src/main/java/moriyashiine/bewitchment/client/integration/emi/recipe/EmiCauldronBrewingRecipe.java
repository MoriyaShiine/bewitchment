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
import moriyashiine.bewitchment.common.recipe.CauldronBrewingRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EmiCauldronBrewingRecipe extends BasicEmiRecipe {
	public EmiCauldronBrewingRecipe(CauldronBrewingRecipe recipe) {
		super(BWEmiIntegration.CAULDRON_BREWING_CATEGORY, recipe.getId(), 76, 18);
		inputs.add(EmiIngredient.of(recipe.input));
		List<StatusEffectInstance> effects = Collections.singletonList(new StatusEffectInstance(recipe.output, recipe.time));
		ItemStack potion = PotionUtil.setCustomPotionEffects(new ItemStack(Items.POTION), effects);
		potion.getOrCreateNbt().putInt("CustomPotionColor", PotionUtil.getColor(effects));
		potion.getOrCreateNbt().putBoolean("BewitchmentBrew", true);
		outputs.add(EmiStack.of(potion));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
		widgets.addSlot(inputs.get(0), 0, 0);
		widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
	}
}
