package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.AthameDropEntry;
import moriyashiine.bewitchment.api.registry.OilRecipe;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWRecipeTypes {
	private static final Map<RecipeSerializer<?>, Identifier> RECIPE_SERIALIZERS = new LinkedHashMap<>();
	private static final Map<RecipeType<?>, Identifier> RECIPE_TYPES = new LinkedHashMap<>();
	
	public static final RecipeSerializer<AthameDropEntry> ATHAME_STRIPPING_RECIPE_SERIALIZER = create("athame_stripping", new AthameDropEntry.Serializer());
	public static final RecipeType<AthameDropEntry> ATHAME_STRIPPING_RECIPE_TYPE = create("athame_stripping");
	
	public static final RecipeSerializer<OilRecipe> OIL_RECIPE_SERIALIZER = create("oil_recipe", new OilRecipe.Serializer());
	public static final RecipeType<OilRecipe> OIL_RECIPE_TYPE = create("oil_recipe");
	
	private static <T extends Recipe<?>> RecipeSerializer<T> create(String name, RecipeSerializer<T> serializer) {
		RECIPE_SERIALIZERS.put(serializer, new Identifier(Bewitchment.MODID, name));
		return serializer;
	}
	
	private static <T extends Recipe<?>> RecipeType<T> create(String name) {
		RecipeType<T> type = new RecipeType<T>() {
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
