package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWRecipeTypes {
	private static final Map<RecipeSerializer<?>, Identifier> RECIPE_SERIALIZERS = new LinkedHashMap<>();
	private static final Map<RecipeType<?>, Identifier> RECIPE_TYPES = new LinkedHashMap<>();
	
	public static final RecipeSerializer<AthameStrippingRecipe> ATHAME_STRIPPING_RECIPE_SERIALIZER = create("athame_stripping", new AthameStrippingRecipe.Serializer());
	public static final RecipeType<AthameStrippingRecipe> ATHAME_STRIPPING_RECIPE_TYPE = create("athame_stripping");
	
	public static final RecipeSerializer<AthameDropRecipe> ATHAME_DROP_RECIPE_SERIALIZER = create("athame_drop", new AthameDropRecipe.Serializer());
	public static final RecipeType<AthameDropRecipe> ATHAME_DROP_RECIPE_TYPE = create("athame_drop");
	
	public static final RecipeSerializer<RitualRecipe> RITUAL_RECIPE_SERIALIZER = create("ritual_recipe", new RitualRecipe.Serializer());
	public static final RecipeType<RitualRecipe> RITUAL_RECIPE_TYPE = create("ritual_recipe");
	
	public static final RecipeSerializer<OilRecipe> OIL_RECIPE_SERIALIZER = create("oil_recipe", new OilRecipe.Serializer());
	public static final RecipeType<OilRecipe> OIL_RECIPE_TYPE = create("oil_recipe");
	
	public static final RecipeSerializer<CauldronBrewingRecipe> CAULDRON_BREWING_RECIPE_SERIALIZER = create("cauldron_brewing_recipe", new CauldronBrewingRecipe.Serializer());
	public static final RecipeType<CauldronBrewingRecipe> CAULDRON_BREWING_RECIPE_TYPE = create("cauldron_brewing_recipe");
	
	public static final RecipeSerializer<IncenseRecipe> INCENSE_RECIPE_SERIALIZER = create("incense_recipe", new IncenseRecipe.Serializer());
	public static final RecipeType<IncenseRecipe> INCENSE_RECIPE_TYPE = create("incense_recipe");
	
	public static final RecipeSerializer<CurseRecipe> CURSE_RECIPE_SERIALIZER = create("curse_recipe", new CurseRecipe.Serializer());
	public static final RecipeType<CurseRecipe> CURSE_RECIPE_TYPE = create("curse_recipe");
	
	public static final RecipeSerializer<TaglockCraftingRecipe> TAGLOCK_CRAFTING_SERIALIZER = create("taglock_crafting", new SpecialRecipeSerializer<>(TaglockCraftingRecipe::new));
	public static final RecipeSerializer<ScepterCraftingRecipe> SCEPTER_CRAFTING_SERIALIZER = create("scepter_crafting", new SpecialRecipeSerializer<>(ScepterCraftingRecipe::new));
	public static final RecipeSerializer<PricklyBeltCraftingRecipe> PRICKLY_BELT_CRAFTING_SERIALIZER = create("prickly_belt_crafting", new SpecialRecipeSerializer<>(PricklyBeltCraftingRecipe::new));
	
	private static <T extends Recipe<?>> RecipeSerializer<T> create(String name, RecipeSerializer<T> serializer) {
		RECIPE_SERIALIZERS.put(serializer, new Identifier(Bewitchment.MODID, name));
		return serializer;
	}
	
	private static <T extends Recipe<?>> RecipeType<T> create(String name) {
		RecipeType<T> type = new RecipeType<>() {
			@Override
			public String toString() {
				return name;
			}
		};
		RECIPE_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}
	
	public static void init() {
		RECIPE_SERIALIZERS.keySet().forEach(recipeSerializer -> Registry.register(Registry.RECIPE_SERIALIZER, RECIPE_SERIALIZERS.get(recipeSerializer), recipeSerializer));
		RECIPE_TYPES.keySet().forEach(recipeType -> Registry.register(Registry.RECIPE_TYPE, RECIPE_TYPES.get(recipeType), recipeType));
	}
}
