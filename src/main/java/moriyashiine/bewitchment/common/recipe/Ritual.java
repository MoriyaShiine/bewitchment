package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.recipe.util.BWRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWRitualTypes;
import moriyashiine.bewitchment.common.registry.entry.RitualFunction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Ritual extends BWRecipe {
	public final Identifier id;
	public final RitualFunction function;
	public final List<Ingredient> input;
	public final String inner, middle, outer;
	public final ItemStack output;
	public final int time, cost;
	
	public Ritual(Identifier id, RitualFunction function, List<Ingredient> input, String inner, String middle, String outer, ItemStack output, int cost, int time) {
		this.id = id;
		this.function = function;
		this.input = input;
		this.inner = inner;
		this.middle = middle;
		this.outer = outer;
		this.output = output;
		this.cost = cost;
		this.time = time;
	}
	
	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.ritual_serializer;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.ritual_type;
	}
	
	public static class Serializer implements RecipeSerializer<Ritual> {
		public static final Item EMPTY = Items.BARRIER;
		
		private static final List<String> validBlocks = Arrays.asList("", "chalk", "infernal_chalk", "eldritch_chalk", "any_chalk");
		
		@Override
		public Ritual read(Identifier id, JsonObject json) {
			List<Ingredient> ingredients = new ArrayList<>();
			for (JsonElement element : JsonHelper.getArray(json, "ingredients")) {
				ingredients.add(Ingredient.fromJson(element));
			}
			if (ingredients.isEmpty()) {
				throw new JsonParseException("Ingredients cannot be empty");
			}
			if (ingredients.size() > 6) {
				throw new JsonParseException("Ingredients cannot be larger than 6");
			}
			ItemStack output = new ItemStack(EMPTY);
			if (JsonHelper.hasElement(json, "result")) {
				output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			}
			String inner = JsonHelper.getString(json, "inner", "");
			String middle = JsonHelper.getString(json, "middle", "");
			String outer = JsonHelper.getString(json, "outer", "");
			if (!validBlocks.contains(inner) || !validBlocks.contains(middle) || !validBlocks.contains(outer)) {
				throw new JsonParseException("Ritual chalk can only contain " + validBlocks);
			}
			if (inner.isEmpty()) {
				throw new JsonParseException("Ritual cannot have an empty inner circle");
			}
			if (middle.isEmpty() && !outer.isEmpty()) {
				throw new JsonParseException("Ritual must have middle circle if outer circle exists");
			}
			return new Ritual(id, BWRegistries.ritual_function.get(new Identifier(JsonHelper.getString(json, "function", Objects.requireNonNull(BWRegistries.ritual_function.getId(BWRitualTypes.crafting)).toString()))), ingredients, inner, middle, outer, output, JsonHelper.getInt(json, "cost"), JsonHelper.getInt(json, "time"));
		}
		
		@Override
		public Ritual read(Identifier id, PacketByteBuf buf) {
			RitualFunction function = BWRegistries.ritual_function.get(new Identifier(buf.readString()));
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < buf.readVarInt(); i++) {
				ingredients.add(Ingredient.fromPacket(buf));
			}
			String inner = buf.readString();
			String middle = buf.readString();
			String outer = buf.readString();
			ItemStack output = buf.readItemStack();
			int cost = buf.readInt();
			int time = buf.readInt();
			return new Ritual(id, function, ingredients, inner, middle, outer, output, cost, time);
		}
		
		@Override
		public void write(PacketByteBuf buf, Ritual recipe) {
			buf.writeString(Objects.requireNonNull(BWRegistries.ritual_function.getId(recipe.function)).toString());
			buf.writeInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeString(recipe.inner);
			buf.writeString(recipe.middle);
			buf.writeString(recipe.outer);
			buf.writeItemStack(recipe.output);
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.time);
		}
	}
}