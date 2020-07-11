package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import moriyashiine.bewitchment.common.recipe.util.BWRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class AthameDropRecipe extends BWRecipe {
	public final Identifier id;
	public final EntityType<?> entity_type;
	public final Item drop;
	public final float chance;
	
	public AthameDropRecipe(Identifier id, EntityType<?> entity_type, Item drop, float chance) {
		this.id = id;
		this.entity_type = entity_type;
		this.drop = drop;
		this.chance = chance;
	}
	
	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.athame_drop_serializer;
	}
	
	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.athame_drop_type;
	}
	
	public static class Serializer implements RecipeSerializer<AthameDropRecipe> {
		@Override
		public AthameDropRecipe read(Identifier id, JsonObject json) {
			return new AthameDropRecipe(id, Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "entity_type"))), JsonHelper.getItem(json, "drop"), JsonHelper.getFloat(json, "chance"));
		}
		
		@Override
		public AthameDropRecipe read(Identifier id, PacketByteBuf buf) {
			return new AthameDropRecipe(id, Registry.ENTITY_TYPE.get(new Identifier(buf.readString())), Registry.ITEM.get(new Identifier(buf.readString())), buf.readFloat());
		}
		
		@Override
		public void write(PacketByteBuf buf, AthameDropRecipe recipe) {
			buf.writeString(Registry.ENTITY_TYPE.getId(recipe.entity_type).toString());
			buf.writeString(Registry.ITEM.getId(recipe.drop).toString());
			buf.writeFloat(recipe.chance);
		}
	}
}