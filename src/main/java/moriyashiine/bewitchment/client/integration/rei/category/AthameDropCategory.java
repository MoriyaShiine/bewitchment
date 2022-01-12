/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import moriyashiine.bewitchment.client.integration.rei.BWREIPlugin;
import moriyashiine.bewitchment.client.integration.rei.display.AthameDropDisplay;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class AthameDropCategory implements DisplayCategory<AthameDropDisplay> {
	public static final TranslatableText TITLE = new TranslatableText("rei.bewitchment.athame_drops");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(BWObjects.ATHAME);

	@Override
	public Renderer getIcon() {
		return ICON;
	}

	@Override
	public Text getTitle() {
		return TITLE;
	}

	@Override
	public int getDisplayHeight() {
		return 36;
	}

	@Override
	public CategoryIdentifier<? extends AthameDropDisplay> getCategoryIdentifier() {
		return BWREIPlugin.ATHAME_DROPS;
	}

	@Override
	public List<Widget> setupDisplay(AthameDropDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 16);
		Point outputPoint = new Point(startPoint.x + 84, startPoint.y + 8);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 50, startPoint.y + 7)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 27, startPoint.y + 8)).entry(display.getInputEntries().get(0).get(0)).markInput());
		widgets.add(Widgets.createResultSlotBackground(outputPoint));
		widgets.add(Widgets.createSlot(outputPoint).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
}
