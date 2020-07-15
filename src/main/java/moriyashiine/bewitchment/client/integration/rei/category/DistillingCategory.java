package moriyashiine.bewitchment.client.integration.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.DistillingRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistillingCategory implements RecipeCategory<DistillingCategory.Display> {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "distilling");
	public static final EntryStack LOGO = EntryStack.create(BWObjects.distillery);
	
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
		return 49;
	}
	
	@Override
	public List<Widget> setupDisplay(Display recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 16);
		Point outputPoint = new Point(startPoint.x + 90, startPoint.y + 8);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 53, startPoint.y + 8)));
		List<List<EntryStack>> inputs = recipeDisplay.getInputEntries();
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y)).entries(inputs.get(0)).markInput());
		if (inputs.size() > 1)
		{
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 28, startPoint.y)).entries(inputs.get(1)).markInput());
			if (inputs.size() > 2)
			{
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y + 18)).entries(inputs.get(2)).markInput());
				if (inputs.size() > 3)
				{
					widgets.add(Widgets.createSlot(new Point(startPoint.x + 28, startPoint.y + 18)).entries(inputs.get(3)).markInput());
				}
			}
		}
		widgets.add(Widgets.createResultSlotBackground(outputPoint));
		widgets.add(Widgets.createSlot(outputPoint).entry(recipeDisplay.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
	
	public static class Display implements RecipeDisplay {
		private final List<List<EntryStack>> input;
		private final List<EntryStack> output;
		
		public Display(DistillingRecipe recipe) {
			List<List<EntryStack>> input = new ArrayList<>();
			for (Ingredient ingredient : recipe.input)
			{
				List<EntryStack> entries = new ArrayList<>();
				for (ItemStack stack : ingredient.getMatchingStacksClient())
				{
					entries.add(EntryStack.create(stack));
				}
				input.add(entries);
			}
			this.input = input;
			output = Collections.singletonList(EntryStack.create(recipe.output));
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