package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.api.registry.Curse;
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

public class CurseRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final DefaultedList<Ingredient> input;
	public final Curse curse;
	public final int cost;
	
	public CurseRecipe(Identifier identifier, DefaultedList<Ingredient> input, Curse curse, int cost) {
		this.identifier = identifier;
		this.input = input;
		this.curse = curse;
		this.cost = cost;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return RitualRecipe.matches(inv, input);
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
		return BWRecipeTypes.CURSE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.CURSE_RECIPE_TYPE;
	}
	
	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<CurseRecipe> {
		@Override
		public CurseRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = RitualRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for curse recipe");
			}
			else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for curse recipe");
			}
			return new CurseRecipe(id, ingredients, BWRegistries.CURSES.get(new Identifier(JsonHelper.getString(json, "curse"))), JsonHelper.getInt(json, "cost"));
		}
		
		@Override
		public CurseRecipe read(Identifier id, PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			for (int i = 0; i < defaultedList.size(); i++) {
				defaultedList.set(i, Ingredient.fromPacket(buf));
			}
			return new CurseRecipe(id, defaultedList, BWRegistries.CURSES.get(new Identifier(buf.readString())), buf.readInt());
		}
		
		@Override
		public void write(PacketByteBuf buf, CurseRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeString(BWRegistries.CURSES.getId(recipe.curse).toString());
			buf.writeInt(recipe.cost);
		}
	}
}
