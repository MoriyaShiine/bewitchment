package moriyashiine.bewitchment.client.integration.rei;

import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import moriyashiine.bewitchment.client.integration.rei.category.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class BewitchmentREIPlugin implements REIPluginV0 {
	private static final Identifier IDENTIFIER = new Identifier(Bewitchment.MODID, "rei");
	
	@Override
	public Identifier getPluginIdentifier() {
		return IDENTIFIER;
	}
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new AthameDropsCategory());
		recipeHelper.registerCategory(new RitualCategory());
		recipeHelper.registerCategory(new OilCraftingCategory());
		recipeHelper.registerCategory(new CauldronBrewingCategory());
		recipeHelper.registerCategory(new IncenseCategory());
		recipeHelper.registerCategory(new CurseCategory());
	}
	
	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		World world = MinecraftClient.getInstance().world;
		if (world != null) {
			world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new AthameDropsCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.RITUAL_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new RitualCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new OilCraftingCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new CauldronBrewingCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.INCENSE_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new IncenseCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.CURSE_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new CurseCategory.Display(recipe)));
		}
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(AthameDropsCategory.IDENTIFIER, AthameDropsCategory.LOGO);
		recipeHelper.registerWorkingStations(RitualCategory.IDENTIFIER, RitualCategory.LOGO);
		recipeHelper.registerWorkingStations(OilCraftingCategory.IDENTIFIER, OilCraftingCategory.LOGO);
		recipeHelper.registerWorkingStations(CauldronBrewingCategory.IDENTIFIER, CauldronBrewingCategory.LOGO);
		recipeHelper.registerWorkingStations(IncenseCategory.IDENTIFIER, IncenseCategory.LOGO);
		recipeHelper.registerWorkingStations(CurseCategory.IDENTIFIER, CurseCategory.LOGO);
	}
}
