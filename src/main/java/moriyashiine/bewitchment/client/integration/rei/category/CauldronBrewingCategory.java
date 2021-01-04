package moriyashiine.bewitchment.client.integration.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import moriyashiine.bewitchment.api.registry.CauldronBrewingRecipe;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CauldronBrewingCategory implements RecipeCategory<CauldronBrewingCategory.Display> {
	public static final Identifier IDENTIFIER = new Identifier(Bewitchment.MODID, "cauldron_brewing");
	public static final EntryStack LOGO = EntryStack.create(BWObjects.WITCH_CAULDRON);
	
	@Override
	public @NotNull Identifier getIdentifier() {
		return IDENTIFIER;
	}
	
	@Override
	public @NotNull String getCategoryName() {
		return I18n.translate("rei." + IDENTIFIER.toString().replaceAll(":", "."));
	}
	
	@Override
	public @NotNull EntryStack getLogo() {
		return LOGO;
	}
	
	@Override
	public int getDisplayHeight() {
		return 36;
	}
	
	@Override
	public @NotNull List<Widget> setupDisplay(Display recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 16);
		Point outputPoint = new Point(startPoint.x + 84, startPoint.y + 8);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 50, startPoint.y + 7)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 27, startPoint.y + 8)).entries(recipeDisplay.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createResultSlotBackground(outputPoint));
		widgets.add(Widgets.createSlot(outputPoint).entries(recipeDisplay.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
	
	public static class Display implements RecipeDisplay {
		private final List<List<EntryStack>> input;
		private final List<List<EntryStack>> output;
		
		public Display(CauldronBrewingRecipe recipe) {
			List<List<EntryStack>> input = new ArrayList<>();
			List<EntryStack> entries = new ArrayList<>();
			for (ItemStack stack : recipe.input.getMatchingStacksClient()) {
				entries.add(EntryStack.create(stack));
			}
			input.add(entries);
			List<StatusEffectInstance> effects = Collections.singletonList(new StatusEffectInstance(recipe.output, recipe.time));
			ItemStack potion = PotionUtil.setCustomPotionEffects(new ItemStack(Items.POTION), effects);
			potion.getOrCreateTag().putInt("CustomPotionColor", PotionUtil.getColor(effects));
			potion.getOrCreateTag().putBoolean("BewitchmentBrew", true);
			this.input = input;
			output = Collections.singletonList(Collections.singletonList(EntryStack.create(potion)));
		}
		
		@Override
		public @NotNull List<List<EntryStack>> getInputEntries() {
			return input;
		}
		
		@Override
		public @NotNull List<List<EntryStack>> getResultingEntries() {
			return output;
		}
		
		@Override
		public @NotNull Identifier getRecipeCategory() {
			return IDENTIFIER;
		}
	}
}
