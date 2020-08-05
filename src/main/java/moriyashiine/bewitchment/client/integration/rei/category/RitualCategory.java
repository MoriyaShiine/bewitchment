package moriyashiine.bewitchment.client.integration.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.Ritual;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RitualCategory implements RecipeCategory<RitualCategory.Display> {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "rituals");
	public static final EntryStack LOGO = EntryStack.create(BWObjects.focal_chalk);
	
	@Override
	public Identifier getIdentifier() {
		return ID;
	}
	
	@Override
	public String getCategoryName() {
		return I18n.translate("rei." + ID.toString().replaceAll(":", "."));
	}
	
	@Override
	public EntryStack getLogo() {
		return LOGO;
	}
	
	@Override
	public int getDisplayHeight() {
		return 64;
	}
	
	@Override
	public List<Widget> setupDisplay(RitualCategory.Display recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 16);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 7)));
		List<List<EntryStack>> inputs = recipeDisplay.getInputEntries();
		widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y - 1)).entries(inputs.get(0)).markInput());
		if (inputs.size() > 1) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y - 1)).entries(inputs.get(1)).markInput());
			if (inputs.size() > 2) {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 36, startPoint.y - 1)).entries(inputs.get(2)).markInput());
				if (inputs.size() > 3) {
					widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 17)).entries(inputs.get(3)).markInput());
					if (inputs.size() > 4) {
						widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 17)).entries(inputs.get(3)).markInput());
						if (inputs.size() > 5) {
							widgets.add(Widgets.createSlot(new Point(startPoint.x + 36, startPoint.y + 17)).entries(inputs.get(3)).markInput());
						}
					}
				}
			}
		}
		List<EntryStack> outputs = recipeDisplay.getOutputEntries();
		if (outputs.size() == 1) {
			Point outputPoint = new Point(startPoint.x + 96, startPoint.y + 7);
			widgets.add(Widgets.createResultSlotBackground(outputPoint));
			widgets.add(Widgets.createSlot(outputPoint).entry(outputs.get(0)).disableBackground().markOutput());
		}
		else {
			Point outputPoint = new Point(startPoint.x + 96, startPoint.y - 2);
			widgets.add(Widgets.createResultSlotBackground(outputPoint));
			widgets.add(Widgets.createSlot(outputPoint).entry(outputs.get(0)).disableBackground().markOutput());
			outputPoint = new Point(startPoint.x + 96, startPoint.y + 22);
			widgets.add(Widgets.createSlotBackground(outputPoint));
			widgets.add(Widgets.createSlot(outputPoint).entry(outputs.get(1)).disableBackground().markOutput());
		}
		return widgets;
	}
	
	public static class Display implements RecipeDisplay {
		private final List<List<EntryStack>> input;
		private final List<EntryStack> output;
		
		public Display(Ritual ritual) {
			List<List<EntryStack>> input = new ArrayList<>();
			for (Ingredient ingredient : ritual.input) {
				List<EntryStack> entries = new ArrayList<>();
				for (ItemStack stack : ingredient.getMatchingStacksClient()) {
					entries.add(EntryStack.create(stack));
				}
				input.add(entries);
			}
			this.input = input;
			List<EntryStack> ritualOutput = new ArrayList<>();
			List<ItemStack> outputs = new ArrayList<>();
			ItemStack chalk = new ItemStack(BWObjects.focal_chalk).setCustomName(new TranslatableText(ritual.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
			chalk.getOrCreateTag().putString("inner_circle", ritual.inner);
			chalk.getOrCreateTag().putString("middle_circle", ritual.middle);
			chalk.getOrCreateTag().putString("outer_circle", ritual.outer);
			chalk.getOrCreateTag().putInt("cost", ritual.cost);
			chalk.getOrCreateTag().putInt("time", ritual.time);
			outputs.add(chalk);
			ItemStack output = ritual.output;
			if (output.getItem() != Ritual.Serializer.EMPTY) {
				outputs.add(output);
			}
			for (ItemStack stack : outputs) {
				ritualOutput.add(EntryStack.create(stack));
			}
			this.output = ritualOutput;
		}
		
		@Override
		public List<List<EntryStack>> getInputEntries() {
			return input;
		}
		
		@Override
		public List<EntryStack> getOutputEntries() {
			return output;
		}
		
		@Override
		public Identifier getRecipeCategory() {
			return ID;
		}
	}
}
