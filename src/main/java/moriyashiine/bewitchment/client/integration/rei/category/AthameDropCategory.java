package moriyashiine.bewitchment.client.integration.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Blocks;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AthameDropCategory implements RecipeCategory<AthameDropCategory.Display> {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "athame_drops");
	private static final EntryStack LOGO = EntryStack.create(BWObjects.athame);
	
	@Override
	public Identifier getIdentifier() {
		return ID;
	}
	
	@Override
	public String getCategoryName() {
		return I18n.translate("jei." + ID.toString().replaceAll(":", "."));
	}
	
	@Override
	public EntryStack getLogo() {
		return LOGO;
	}
	
	@Override
	public int getDisplayHeight() {
		return 32;
	}
	
	@Override
	public List<Widget> setupDisplay(Display recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 16);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 30, startPoint.y + 7)).entry(recipeDisplay.getInputEntries().get(0).get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 7)).entry(recipeDisplay.getOutputEntries().get(0)).markOutput());
		return widgets;
	}
	
	public static class Display implements RecipeDisplay {
		private final List<List<EntryStack>> input;
		private final List<EntryStack> output;
		
		public Display(AthameDropRecipe recipe) {
			input = Collections.singletonList(Collections.singletonList(EntryStack.create(new ItemStack(Blocks.SPAWNER).setCustomName(recipe.entity_type.getName()))));
			output = Collections.singletonList(EntryStack.create(recipe.drop));
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