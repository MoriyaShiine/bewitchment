package moriyashiine.bewitchment.client.integration.rei;

import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import moriyashiine.bewitchment.client.integration.rei.category.AthameDropCategory;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BewitchmentREIPlugin implements REIPluginV0 {
	private static final Identifier ID = new Identifier(Bewitchment.MODID, "rei");
	
	@Override
	public Identifier getPluginIdentifier() {
		return ID;
	}
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new AthameDropCategory());
	}
	
	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		World world = MinecraftClient.getInstance().world;
		if (world != null) {
			world.getRecipeManager().method_30027(BWRecipeTypes.athame_drop_type).forEach(recipe -> recipeHelper.registerDisplay(new AthameDropCategory.Display((AthameDropRecipe) recipe)));
		}
	}
}