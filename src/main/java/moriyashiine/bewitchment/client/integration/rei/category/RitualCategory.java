package moriyashiine.bewitchment.client.integration.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RitualCategory implements RecipeCategory<RitualCategory.Display> {
	public static final Identifier IDENTIFIER = new Identifier(Bewitchment.MODID, "rituals");
	public static final EntryStack LOGO = EntryStack.create(BWObjects.GOLDEN_CHALK);
	
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
		return 49;
	}
	
	@Override
	public @NotNull List<Widget> setupDisplay(Display recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 16);
		Point outputPoint = new Point(startPoint.x + 90, startPoint.y + 8);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 57, startPoint.y + 8)));
		List<List<EntryStack>> inputs = recipeDisplay.getInputEntries();
		widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y)).entries(inputs.get(0)).markInput());
		if (inputs.size() > 1) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y)).entries(inputs.get(1)).markInput());
			if (inputs.size() > 2) {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 36, startPoint.y)).entries(inputs.get(2)).markInput());
				if (inputs.size() > 3) {
					widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 18)).entries(inputs.get(3)).markInput());
					if (inputs.size() > 4) {
						widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 18)).entries(inputs.get(4)).markInput());
						if (inputs.size() > 5) {
							widgets.add(Widgets.createSlot(new Point(startPoint.x + 36, startPoint.y + 18)).entries(inputs.get(5)).markInput());
						}
					}
				}
			}
		}
		widgets.add(Widgets.createResultSlotBackground(outputPoint));
		widgets.add(Widgets.createSlot(outputPoint).entries(recipeDisplay.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
	
	public static class Display implements RecipeDisplay {
		private final List<List<EntryStack>> input;
		private final List<List<EntryStack>> output;
		
		public Display(RitualRecipe recipe) {
			List<List<EntryStack>> input = new ArrayList<>();
			for (Ingredient ingredient : recipe.input) {
				List<EntryStack> entries = new ArrayList<>();
				for (ItemStack stack : ingredient.getMatchingStacksClient()) {
					entries.add(EntryStack.create(stack));
				}
				input.add(entries);
			}
			this.input = input;
			ItemStack chalk = new ItemStack(BWObjects.GOLDEN_CHALK).setCustomName(new TranslatableText("ritual." + recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
			chalk.getOrCreateTag().putString("InnerCircle", "chalk." + Bewitchment.MODID + "." + recipe.inner);
			if (!recipe.outer.isEmpty()) {
				chalk.getOrCreateTag().putString("OuterCircle", "chalk." + Bewitchment.MODID + "." + recipe.outer);
			}
			chalk.getOrCreateTag().putInt("Cost", recipe.cost);
			if (recipe.runningTime > 0) {
				chalk.getOrCreateTag().putInt("RunningTime", recipe.runningTime);
			}
			output = Collections.singletonList(Collections.singletonList(EntryStack.create(chalk)));
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
