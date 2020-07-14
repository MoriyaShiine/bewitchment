//package moriyashiine.bewitchment.client.integration.rei.category;
//
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.gui.drawable.IDrawable;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.category.IRecipeCategory;
//import moriyashiine.bewitchment.common.Bewitchment;
//import moriyashiine.bewitchment.common.recipe.Ritual;
//import moriyashiine.bewitchment.common.registry.BWObjects;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.TranslationTextComponent;
//
//import javax.annotation.Nonnull;
//import java.util.ArrayList;
//import java.util.List;
//
//public class RitualCategory implements IRecipeCategory<Ritual> {
//	public static final ResourceLocation ID = new ResourceLocation(Bewitchment.MODID, "rituals");
//	private final IDrawable background, icon;
//
//	public RitualCategory(IGuiHelper helper) {
//		background = helper.createDrawable(new ResourceLocation(Bewitchment.MODID, "textures/gui/jei/rituals.png"), 0, 0, 128, 80);
//		icon = helper.createDrawableIngredient(new ItemStack(BWObjects.focal_chalk));
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
//	public Class<? extends Ritual> getRecipeClass() {
//		return Ritual.class;
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
//	public void setIngredients(@Nonnull Ritual recipe, @Nonnull IIngredients ingredients) {
//		ingredients.setInputIngredients(recipe.input);
//		List<ItemStack> outputs = new ArrayList<>();
//		ItemStack chalk = new ItemStack(BWObjects.focal_chalk).setDisplayName(new TranslationTextComponent(recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
//		chalk.getOrCreateTag().putString("inner_circle", recipe.inner);
//		chalk.getOrCreateTag().putString("middle_circle", recipe.middle);
//		chalk.getOrCreateTag().putString("outer_circle", recipe.outer);
//		chalk.getOrCreateTag().putInt("cost", recipe.cost);
//		chalk.getOrCreateTag().putInt("time", recipe.time);
//		outputs.add(chalk);
//		ItemStack output = recipe.output;
//		if (output.getItem() != Ritual.Serializer.EMPTY) {
//			outputs.add(output);
//		}
//		ingredients.setOutputs(VanillaTypes.ITEM, outputs);
//	}
//
//	@Override
//	public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull Ritual recipe, @Nonnull IIngredients ingredients) {
//		int index = 0, posX = 5;
//		for (List<ItemStack> stacks : ingredients.getInputs(VanillaTypes.ITEM)) {
//			layout.getItemStacks().init(index, true, posX, 4);
//			layout.getItemStacks().set(index, stacks);
//			posX += 20;
//			index++;
//		}
//		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
//		layout.getItemStacks().init(index, false, 56, 32);
//		layout.getItemStacks().set(index, outputs.get(0).get(0));
//		if (outputs.size() > 1) {
//			index++;
//			layout.getItemStacks().init(index, false, 56, 56);
//			layout.getItemStacks().set(index, outputs.get(1).get(0));
//		}
//	}
//}