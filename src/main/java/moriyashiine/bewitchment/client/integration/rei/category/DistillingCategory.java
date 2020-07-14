//package moriyashiine.bewitchment.client.integration.rei.category;
//
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.gui.drawable.IDrawable;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.category.IRecipeCategory;
//import moriyashiine.bewitchment.common.Bewitchment;
//import moriyashiine.bewitchment.common.recipe.DistillingRecipe;
//import moriyashiine.bewitchment.common.registry.BWObjects;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//
//import javax.annotation.Nonnull;
//import java.util.List;
//
//public class DistillingCategory implements IRecipeCategory<DistillingRecipe> {
//	public static final ResourceLocation ID = new ResourceLocation(Bewitchment.MODID, "distilling");
//	private final IDrawable background, icon;
//
//	public DistillingCategory(IGuiHelper helper) {
//		background = helper.createDrawable(new ResourceLocation(Bewitchment.MODID, "textures/gui/jei/distilling_and_spinning.png"), 0, 0, 96, 48);
//		icon = helper.createDrawableIngredient(new ItemStack(BWObjects.distillery));
//	}
//
//	@Override
//	@Nonnull
//	public ResourceLocation getUid() {
//		return ID;
//	}
//
//	@Override
//	@Nonnull
//	public Class<? extends DistillingRecipe> getRecipeClass() {
//		return DistillingRecipe.class;
//	}
//
//	@Override
//	@Nonnull
//	public String getTitle() {
//		return I18n.format("jei." + ID.toString().replaceAll(":", "."));
//	}
//
//	@Override
//	@Nonnull
//	public IDrawable getBackground() {
//		return background;
//	}
//
//	@Override
//	@Nonnull
//	public IDrawable getIcon() {
//		return icon;
//	}
//
//	@Override
//	public void setIngredients(@Nonnull DistillingRecipe recipe, @Nonnull IIngredients ingredients) {
//		ingredients.setInputIngredients(recipe.input);
//		ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
//	}
//
//	@Override
//	public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull DistillingRecipe recipe, @Nonnull IIngredients ingredients) {
//		int index = 0;
//		for (List<ItemStack> stacks : ingredients.getInputs(VanillaTypes.ITEM)) {
//			layout.getItemStacks().init(index, true, 5 + (index % 2) * 18, 6 + (index / 2) * 18);
//			layout.getItemStacks().set(index, stacks);
//			index++;
//		}
//		layout.getItemStacks().init(index, false, 69, 15);
//		layout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0).get(0));
//	}
//}