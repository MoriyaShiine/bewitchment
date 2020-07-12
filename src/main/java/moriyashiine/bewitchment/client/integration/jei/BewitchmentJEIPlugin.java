package moriyashiine.bewitchment.client.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.client.integration.jei.category.AthameDropCategory;
import moriyashiine.bewitchment.client.integration.jei.category.DistillingCategory;
import moriyashiine.bewitchment.client.integration.jei.category.RitualCategory;
import moriyashiine.bewitchment.client.integration.jei.category.SpinningCategory;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@JeiPlugin
public class BewitchmentJEIPlugin implements IModPlugin {
	private static final ResourceLocation ID = new ResourceLocation(Bewitchment.MODID, "jei");
	
	@Override
	@Nonnull
	public ResourceLocation getPluginUid() {
		return ID;
	}
	
	@Override
	public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
		IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new AthameDropCategory(helper));
		registry.addRecipeCategories(new RitualCategory(helper));
		registry.addRecipeCategories(new DistillingCategory(helper));
		registry.addRecipeCategories(new SpinningCategory(helper));
	}
	
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		World world = Minecraft.getInstance().world;
		if (world != null) {
			RecipeManager recipeManager = world.getRecipeManager();
			registry.addRecipes(recipeManager.getRecipes(BWRecipeTypes.athame_drop_type).values(), AthameDropCategory.ID);
			registry.addRecipes(recipeManager.getRecipes(BWRecipeTypes.ritual_type).values(), RitualCategory.ID);
			registry.addRecipes(recipeManager.getRecipes(BWRecipeTypes.distilling_type).values(), DistillingCategory.ID);
			registry.addRecipes(recipeManager.getRecipes(BWRecipeTypes.spinning_type).values(), SpinningCategory.ID);
		}
	}
	
	@Override
	public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(BWObjects.athame), AthameDropCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(BWObjects.focal_chalk), RitualCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(BWObjects.distillery), DistillingCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(BWObjects.spinning_wheel), SpinningCategory.ID);
	}
}