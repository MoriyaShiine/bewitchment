package moriyashiine.bewitchment.common.recipe;

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
		return RitualRecipe.matches(inv, input);
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
			DefaultedList<Ingredient> ingredients = RitualRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for oil recipe");
			}
			else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for oil recipe");
			}
			return new OilRecipe(id, ingredients, ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result")), JsonHelper.getInt(json, "color"));
		}
		
		@Override
		public OilRecipe read(Identifier id, PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			for (int i = 0; i < defaultedList.size(); i++) {
				defaultedList.set(i, Ingredient.fromPacket(buf));
			}
			return new OilRecipe(id, defaultedList, buf.readItemStack(), buf.readInt());
		}
		
		@Override
		public void write(PacketByteBuf buf, OilRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeItemStack(recipe.getOutput());
			buf.writeInt(recipe.color);
		}
	}
}
