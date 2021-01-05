package moriyashiine.bewitchment.api.registry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class RitualRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final DefaultedList<Ingredient> input;
	public final RitualFunction ritualFunction;
	public final int cost, runningTime;
	
	public RitualRecipe(Identifier identifier, DefaultedList<Ingredient> input, RitualFunction ritualFunction, int cost, int runningTime) {
		this.identifier = identifier;
		this.input = input;
		this.ritualFunction = ritualFunction;
		this.cost = cost;
		this.runningTime = runningTime;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return OilRecipe.matches(inv, input);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
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
	
	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<RitualRecipe> {
		@Override
		public RitualRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = OilRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for ritual recipe");
			}
			else if (ingredients.size() > 6) {
				throw new JsonParseException("Too many ingredients for ritual recipe");
			}
			return new RitualRecipe(id, ingredients, BWRegistries.RITUAL_FUNCTIONS.get(new Identifier(JsonHelper.getString(json, "ritual_function"))), JsonHelper.getInt(json, "cost"), JsonHelper.getInt(json, "running_time"));
		}
		
		@Override
		public RitualRecipe read(Identifier id, PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			for (int i = 0; i < defaultedList.size(); i++) {
				defaultedList.set(i, Ingredient.fromPacket(buf));
			}
			return new RitualRecipe(id, defaultedList, BWRegistries.RITUAL_FUNCTIONS.get(new Identifier(buf.readString())), buf.readInt(), buf.readInt());
		}
		
		@Override
		public void write(PacketByteBuf buf, RitualRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeString(BWRegistries.RITUAL_FUNCTIONS.getId(recipe.ritualFunction).toString());
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.runningTime);
		}
	}
}
