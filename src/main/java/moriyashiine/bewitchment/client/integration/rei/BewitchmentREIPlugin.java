package moriyashiine.bewitchment.client.integration.rei;

import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import moriyashiine.bewitchment.client.integration.rei.category.AthameDropsCategory;
import moriyashiine.bewitchment.client.integration.rei.category.AthameStrippingCategory;
import moriyashiine.bewitchment.client.integration.rei.category.CauldronBrewingCategory;
import moriyashiine.bewitchment.client.integration.rei.category.OilCraftingCategory;
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
		recipeHelper.registerCategory(new AthameStrippingCategory());
		recipeHelper.registerCategory(new AthameDropsCategory());
		recipeHelper.registerCategory(new OilCraftingCategory());
		recipeHelper.registerCategory(new CauldronBrewingCategory());
	}
	
	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		World world = MinecraftClient.getInstance().world;
		if (world != null) {
			world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_STRIPPING_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new AthameStrippingCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new AthameDropsCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new OilCraftingCategory.Display(recipe)));
			world.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).forEach(recipe -> recipeHelper.registerDisplay(new CauldronBrewingCategory.Display(recipe)));
		}
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(AthameStrippingCategory.IDENTIFIER, AthameStrippingCategory.LOGO);
		recipeHelper.registerWorkingStations(AthameDropsCategory.IDENTIFIER, AthameDropsCategory.LOGO);
		recipeHelper.registerWorkingStations(OilCraftingCategory.IDENTIFIER, OilCraftingCategory.LOGO);
		recipeHelper.registerWorkingStations(CauldronBrewingCategory.IDENTIFIER, CauldronBrewingCategory.LOGO);
	}
}
