package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BWTags {
	public static final Tag<EntityType<?>> WEAK_TO_SILVER = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "weak_to_silver"));
	
	public static final Tag<Item> SILVER_ARMOR = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_armor"));
	public static final Tag<Item> SILVER_TOOLS = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_tools"));
	
	public static final Tag<Item> BARKS = TagRegistry.item(new Identifier(Bewitchment.MODID, "barks"));
}
