package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.entity.effect.StatusEffect;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class IncenseRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final DefaultedList<Ingredient> input;
	public final StatusEffect effect;
	public final int amplifier;
	
	public IncenseRecipe(Identifier identifier, DefaultedList<Ingredient> input, StatusEffect effect, int amplifier) {
		this.identifier = identifier;
		this.input = input;
		this.effect = effect;
		this.amplifier = amplifier;
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
		return BWRecipeTypes.INCENSE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.INCENSE_RECIPE_TYPE;
	}
	
	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<IncenseRecipe> {
		@Override
		public IncenseRecipe read(Identifier id, JsonObject json) {
			DefaultedList<Ingredient> ingredients = RitualRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for incense recipe");
			}
			else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for incense recipe");
			}
			return new IncenseRecipe(id, ingredients, Registry.STATUS_EFFECT.get(new Identifier(JsonHelper.getString(json, "effect"))), JsonHelper.getInt(json, "amplifier", 0));
		}
		
		@Override
		public IncenseRecipe read(Identifier id, PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			for (int i = 0; i < defaultedList.size(); i++) {
				defaultedList.set(i, Ingredient.fromPacket(buf));
			}
			return new IncenseRecipe(id, defaultedList, Registry.STATUS_EFFECT.get(new Identifier(buf.readString())), buf.readInt());
		}
		
		@Override
		public void write(PacketByteBuf buf, IncenseRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.write(buf);
			}
			buf.writeString(Registry.STATUS_EFFECT.getId(recipe.effect).toString());
			buf.writeInt(recipe.amplifier);
		}
	}
}
