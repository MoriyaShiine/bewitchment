package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.entity.EntityType;
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

public class AthameDropRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final EntityType<?> entity_type;
	private final ItemStack output;
	public final float chance;
	
	public AthameDropRecipe(Identifier id, EntityType<?> entity_type, ItemStack output, float chance) {
		this.identifier = id;
		this.entity_type = entity_type;
		this.output = output;
		this.chance = chance;
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
		return BWRecipeTypes.ATHAME_DROP_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE;
	}
	
	public static class Serializer implements RecipeSerializer<AthameDropRecipe> {
		@Override
		public AthameDropRecipe read(Identifier id, JsonObject json) {
			return new AthameDropRecipe(id, Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "entity_type"))), ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result")), JsonHelper.getFloat(json, "chance"));
		}
		
		@Override
		public AthameDropRecipe read(Identifier id, PacketByteBuf buf) {
			return new AthameDropRecipe(id, Registry.ENTITY_TYPE.get(new Identifier(buf.readString())), buf.readItemStack(), buf.readFloat());
		}
		
		@Override
		public void write(PacketByteBuf buf, AthameDropRecipe recipe) {
			buf.writeString(Registry.ENTITY_TYPE.getId(recipe.entity_type).toString());
			buf.writeItemStack(recipe.getOutput());
			buf.writeFloat(recipe.chance);
		}
	}
}
