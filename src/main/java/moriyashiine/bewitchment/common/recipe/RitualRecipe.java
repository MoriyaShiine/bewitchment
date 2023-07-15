/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final DefaultedList<Ingredient> input;
	public final String inner, outer;
	public final RitualFunction ritualFunction;
	public final int cost, runningTime;

	public RitualRecipe(Identifier identifier, DefaultedList<Ingredient> input, String inner, String outer, RitualFunction ritualFunction, int cost, int runningTime) {
		this.identifier = identifier;
		this.input = input;
		this.inner = inner;
		this.outer = outer;
		this.ritualFunction = ritualFunction;
		this.cost = cost;
		this.runningTime = runningTime;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return matches(inv, input);
	}

	@Override
	public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public Identifier getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.RITUAL_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.RITUAL_RECIPE_TYPE;
	}

	public static boolean matches(Inventory inv, DefaultedList<Ingredient> input) {
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

	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<RitualRecipe> {
		@Override
		public RitualRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for ritual recipe");
			} else if (ingredients.size() > 6) {
				throw new JsonParseException("Too many ingredients for ritual recipe");
			}
			String inner = JsonHelper.getString(json, "inner");
			if (inner.isEmpty()) {
				throw new JsonParseException("Inner circle is empty");
			}
			return new RitualRecipe(id, ingredients, inner, JsonHelper.getString(json, "outer", ""), BWRegistries.RITUAL_FUNCTION.get(new Identifier(JsonHelper.getString(json, "ritual_function"))), JsonHelper.getInt(json, "cost"), JsonHelper.getInt(json, "running_time", 0));
		}

		@Override
		public RitualRecipe read(Identifier id, PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			defaultedList.replaceAll(ignored -> Ingredient.fromPacket(buf));
			return new RitualRecipe(id, defaultedList, buf.readString(), buf.readString(), BWRegistries.RITUAL_FUNCTION.get(new Identifier(buf.readString())), buf.readInt(), buf.readInt());
		}

		@Override
		public void write(PacketByteBuf buf, RitualRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeString(recipe.inner);
			buf.writeString(recipe.outer);
			buf.writeString(BWRegistries.RITUAL_FUNCTION.getId(recipe.ritualFunction).toString());
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.runningTime);
		}

		public static DefaultedList<Ingredient> getIngredients(JsonArray json) {
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
