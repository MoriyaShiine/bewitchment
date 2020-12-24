package moriyashiine.bewitchment.api.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class OilRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final DefaultedList<Ingredient> input;
	private final ItemStack output;
	public final int color;
	
	public OilRecipe(Identifier identifier, DefaultedList<Ingredient> input, ItemStack output, int color) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.color = color;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		List<ItemStack> checklist = new ArrayList<>();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				checklist.add(stack);
			}
		}
		if (input.size() != checklist.size()) {
			return false;
		}
		for (Ingredient ingredient : input) {
			boolean found = false;
			for (ItemStack stack : checklist) {
				if (ingredient.test(stack)) {
					found = true;
					checklist.remove(stack);
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return output;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}
	
	@Override
	public Identifier getId() {
		return identifier;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.OIL_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.OIL_RECIPE_TYPE;
	}
	
	public static class Serializer implements RecipeSerializer<OilRecipe> {
		@Override
		public OilRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for oil recipe");
			}
			else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for oil recipe");
			}
			return new OilRecipe(id, ingredients, ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result")), JsonHelper.getInt(json, "color"));
		}
		
		@Override
		public OilRecipe read(Identifier id, PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			for (int i = 0; i < defaultedList.size(); i++) {
				defaultedList.set(i, Ingredient.fromPacket(buf));
			}
			ItemStack itemStack = buf.readItemStack();
			return new OilRecipe(id, defaultedList, itemStack, buf.readInt());
		}
		
		@Override
		public void write(PacketByteBuf buf, OilRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeItemStack(recipe.output);
			buf.writeInt(recipe.color);
		}
		
		private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
			DefaultedList<Ingredient> ingredients = DefaultedList.of();
			for (int i = 0; i < json.size(); i++) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					ingredients.add(ingredient);
				}
			}
			return ingredients;
		}
	}
}
