/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import moriyashiine.bewitchment.client.integration.rei.category.*;
import moriyashiine.bewitchment.client.integration.rei.display.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.*;
import net.minecraft.util.Identifier;

public class BWREIPlugin implements REIClientPlugin {
	public static final CategoryIdentifier<AthameStrippingDisplay> ATHAME_STRIPPING = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "athame_stripping"));
	public static final CategoryIdentifier<AthameDropDisplay> ATHAME_DROPS = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "athame_drops"));
	public static final CategoryIdentifier<RitualDisplay> RITUALS = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "rituals"));
	public static final CategoryIdentifier<OilCraftingDisplay> OIL_CRAFTING = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "oil_crafting"));
	public static final CategoryIdentifier<CauldronBrewingDisplay> CAULDRON_BREWING = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "cauldron_brewing"));
	public static final CategoryIdentifier<IncenseDisplay> INCENSES = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "incenses"));
	public static final CategoryIdentifier<CursesDisplay> CURSES = CategoryIdentifier.of(new Identifier(Bewitchment.MODID, "curses"));

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new AthameStrippingCategory());
		registry.addWorkstations(ATHAME_STRIPPING, AthameStrippingCategory.ICON);
		registry.removePlusButton(ATHAME_STRIPPING);
		registry.add(new AthameDropCategory());
		registry.addWorkstations(ATHAME_DROPS, AthameDropCategory.ICON);
		registry.removePlusButton(ATHAME_DROPS);
		registry.add(new RitualCategory());
		registry.addWorkstations(RITUALS, RitualCategory.ICON);
		registry.removePlusButton(RITUALS);
		registry.add(new OilCraftingCategory());
		registry.addWorkstations(OIL_CRAFTING, OilCraftingCategory.ICON);
		registry.removePlusButton(OIL_CRAFTING);
		registry.add(new CauldronBrewingCategory());
		registry.addWorkstations(CAULDRON_BREWING, CauldronBrewingCategory.ICON);
		registry.removePlusButton(CAULDRON_BREWING);
		registry.add(new IncenseCategory());
		registry.addWorkstations(INCENSES, IncenseCategory.ICON);
		registry.removePlusButton(INCENSES);
		registry.add(new CursesCategory());
		registry.addWorkstations(CURSES, CursesCategory.ICON);
		registry.removePlusButton(CURSES);
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(AthameStrippingRecipe.class, AthameStrippingDisplay::new);
		registry.registerFiller(AthameDropRecipe.class, AthameDropDisplay::new);
		registry.registerFiller(RitualRecipe.class, RitualDisplay::new);
		registry.registerFiller(OilRecipe.class, OilCraftingDisplay::new);
		registry.registerFiller(CauldronBrewingRecipe.class, CauldronBrewingDisplay::new);
		registry.registerFiller(IncenseRecipe.class, IncenseDisplay::new);
		registry.registerFiller(CurseRecipe.class, CursesDisplay::new);
	}
}
