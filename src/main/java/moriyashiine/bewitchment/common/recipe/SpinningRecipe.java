package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.recipe.util.BWRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class SpinningRecipe extends BWRecipe {
	public final Identifier id;
	public final List<Ingredient> input;
	public final ItemStack output;
	
	public SpinningRecipe(Identifier id, List<Ingredient> input, ItemStack output) {
		this.id = id;
		this.input = input;
		this.output = output;
	}
	
	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.spinning_serializer;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.spinning_type;
	}
	
	public static class Serializer implements RecipeSerializer<SpinningRecipe> {
		@Override
		public SpinningRecipe read(Identifier id, JsonObject json) {
			List<Ingredient> ingredients = new ArrayList<>();
			for (JsonElement element : JsonHelper.getArray(json, "ingredients")) {
				ingredients.add(Ingredient.fromJson(element));
			}
			if (ingredients.isEmpty()) {
				throw new JsonParseException("Ingredients cannot be empty");
			}
			if (ingredients.size() > 4) {
				throw new JsonParseException("Ingredients cannot be larger than 4");
			}
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			return new SpinningRecipe(id, ingredients, output);
		}
		
		@Override
		public SpinningRecipe read(Identifier id, PacketByteBuf buf) {
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < buf.readVarInt(); i++) {
				ingredients.add(Ingredient.fromPacket(buf));
			}
			ItemStack output = buf.readItemStack();
			return new SpinningRecipe(id, ingredients, output);
		}
		
		@Override
		public void write(PacketByteBuf buf, SpinningRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeItemStack(recipe.output);
		}
	}
}
