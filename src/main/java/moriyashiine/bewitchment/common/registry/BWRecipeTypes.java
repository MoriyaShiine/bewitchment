package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.recipe.DistillingRecipe;
import moriyashiine.bewitchment.common.recipe.Ritual;
import moriyashiine.bewitchment.common.recipe.SpinningRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class BWRecipeTypes {
	private static final Map<RecipeSerializer<?>, Identifier> RECIPE_SERIALIZERS = new LinkedHashMap<>();
	private static final Map<DummyRecipeType<?>, Identifier> RECIPE_TYPES = new LinkedHashMap<>();
	
	public static final RecipeSerializer<AthameDropRecipe> athame_drop_serializer = create("athame_drop", new AthameDropRecipe.Serializer());
	public static final DummyRecipeType<AthameDropRecipe> athame_drop_type = create("athame_drop");
	
	public static final RecipeSerializer<Ritual> ritual_serializer = create("ritual", new Ritual.Serializer());
	public static final DummyRecipeType<Ritual> ritual_type = create("ritual");
	
	public static final RecipeSerializer<DistillingRecipe> distilling_serializer = create("distilling", new DistillingRecipe.Serializer());
	public static final DummyRecipeType<DistillingRecipe> distilling_type = create("distilling");
	
	public static final RecipeSerializer<SpinningRecipe> spinning_serializer = create("spinning", new SpinningRecipe.Serializer());
	public static final DummyRecipeType<SpinningRecipe> spinning_type = create("spinning");
	
	private static <T extends Recipe<?>> DummyRecipeType<T> create(String name) {
		DummyRecipeType<T> type = new DummyRecipeType<>();
		RECIPE_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}
	
	private static <T extends Recipe<?>> RecipeSerializer<T> create(String name, RecipeSerializer<T> serializer) {
		RECIPE_SERIALIZERS.put(serializer, new Identifier(Bewitchment.MODID, name));
		return serializer;
	}
	
	public static void init() {
		RECIPE_TYPES.keySet().forEach(type -> Registry.register(Registry.RECIPE_TYPE, RECIPE_TYPES.get(type), type));
		RECIPE_SERIALIZERS.keySet().forEach(serializer -> Registry.register(Registry.RECIPE_SERIALIZER, RECIPE_SERIALIZERS.get(serializer), serializer));
	}
	
	private static class DummyRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(this)).toString();
		}
	}
}