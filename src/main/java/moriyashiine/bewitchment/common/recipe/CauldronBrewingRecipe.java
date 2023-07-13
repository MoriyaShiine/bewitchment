/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class CauldronBrewingRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final Ingredient input;
	public final StatusEffect output;
	public final int time;

	public CauldronBrewingRecipe(Identifier identifier, Ingredient input, StatusEffect output, int time) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.time = time;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
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
		return BWRecipeTypes.CAULDRON_BREWING_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE;
	}

	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<CauldronBrewingRecipe> {
		@Override
		public CauldronBrewingRecipe read(Identifier id, JsonObject json) {
			return new CauldronBrewingRecipe(id, Ingredient.fromJson(JsonHelper.getObject(json, "ingredient")), Registries.STATUS_EFFECT.get(new Identifier(JsonHelper.getString(json, "effect"))), JsonHelper.getInt(json, "time"));
		}

		@Override
		public CauldronBrewingRecipe read(Identifier id, PacketByteBuf buf) {
			return new CauldronBrewingRecipe(id, Ingredient.fromPacket(buf), Registries.STATUS_EFFECT.get(new Identifier(buf.readString())), buf.readInt());
		}

		@Override
		public void write(PacketByteBuf buf, CauldronBrewingRecipe recipe) {
			recipe.input.write(buf);
			buf.writeString(Registries.STATUS_EFFECT.getId(recipe.output).toString());
			buf.writeInt(recipe.time);
		}
	}
}
