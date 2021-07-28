package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AthameStrippingRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final Block log, strippedLog;
	private final ItemStack output;
	
	public AthameStrippingRecipe(Identifier identifier, Block log, Block strippedLog, ItemStack output) {
		this.identifier = identifier;
		this.log = log;
		this.strippedLog = strippedLog;
		this.output = output;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return false;
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return output;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return false;
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
		return BWRecipeTypes.ATHAME_STRIPPING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.ATHAME_STRIPPING_RECIPE_TYPE;
	}
	
	public static class Serializer implements RecipeSerializer<AthameStrippingRecipe> {
		@Override
		public AthameStrippingRecipe read(Identifier id, JsonObject json) {
			return new AthameStrippingRecipe(id, Registry.BLOCK.get(new Identifier(JsonHelper.getString(json, "log"))), Registry.BLOCK.get(new Identifier(JsonHelper.getString(json, "stripped_log"))), ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result")));
		}
		
		@Override
		public AthameStrippingRecipe read(Identifier id, PacketByteBuf buf) {
			return new AthameStrippingRecipe(id, Registry.BLOCK.get(new Identifier(buf.readString())), Registry.BLOCK.get(new Identifier(buf.readString())), buf.readItemStack());
		}
		
		@Override
		public void write(PacketByteBuf buf, AthameStrippingRecipe recipe) {
			buf.writeString(Registry.BLOCK.getId(recipe.log).toString());
			buf.writeString(Registry.BLOCK.getId(recipe.strippedLog).toString());
			buf.writeItemStack(recipe.getOutput());
		}
	}
}
