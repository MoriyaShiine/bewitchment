package moriyashiine.bewitchment.client.integration.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import moriyashiine.bewitchment.Bewitchment;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AthameDropCategory implements IRecipeCategory<AthameDropRecipe> {
	public static final ResourceLocation ID = new ResourceLocation(Bewitchment.MODID, "athame_drops");
	private final IDrawable background, icon;
	
	public AthameDropCategory(IGuiHelper helper) {
		background = helper.createDrawable(new ResourceLocation(Bewitchment.MODID, "textures/gui/jei/athame_drops.png"), 0, 0, 128, 32);
		icon = helper.createDrawableIngredient(new ItemStack(BWObjects.athame));
	}
	
	@Override
	@Nonnull
	public ResourceLocation getUid() {
		return ID;
	}
	
	@Override
	@Nonnull
	public Class<? extends AthameDropRecipe> getRecipeClass() {
		return AthameDropRecipe.class;
	}
	
	@Override
	@Nonnull
	public String getTitle() {
		return I18n.format("jei." + ID.toString().replaceAll(":", "."));
	}
	
	@Override
	@Nonnull
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	@Nonnull
	public IDrawable getIcon() {
		return icon;
	}
	
	@Override
	public void setIngredients(@Nonnull AthameDropRecipe recipe, @Nonnull IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, new ItemStack(Blocks.SPAWNER).setDisplayName(recipe.entity_type.getName()));
		ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.drop));
	}
	
	@Override
	public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull AthameDropRecipe recipe, @Nonnull IIngredients ingredients) {
		ItemStack input = ingredients.getInputs(VanillaTypes.ITEM).get(0).get(0);
		ItemStack output = ingredients.getOutputs(VanillaTypes.ITEM).get(0).get(0);
		layout.getItemStacks().init(0, true, 30, 7);
		layout.getItemStacks().set(0, input);
		layout.getItemStacks().init(1, false, 80, 7);
		layout.getItemStacks().set(1, output);
	}
}