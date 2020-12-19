package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BWTags {
	public static final Tag<EntityType<?>> WEAK_TO_SILVER = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "weak_to_silver"));
	
	public static final Tag<Block> GIVES_ALTAR_POWER = TagRegistry.block(new Identifier(Bewitchment.MODID, "gives_altar_power"));
	
	public static final Tag<Item> SILVER_ARMOR = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_armor"));
	public static final Tag<Item> SILVER_TOOLS = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_tools"));
	
	public static final Tag<Item> BARKS = TagRegistry.item(new Identifier(Bewitchment.MODID, "barks"));
	public static final Tag<Item> SILVER_NUGGETS = TagRegistry.item(new Identifier("c", "silver_nuggets"));
	
	public static final Tag<Item> SWORDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "swords"));
	public static final Tag<Item> PENTACLES = TagRegistry.item(new Identifier(Bewitchment.MODID, "pentacles"));
	public static final Tag<Item> WANDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "wands"));
	
	public static final Tag<Item> WEAK_SWORDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_swords"));
	public static final Tag<Item> AVERAGE_SWORDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "average_swords"));
	public static final Tag<Item> STRONG_SWORDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_swords"));
	
	public static final Tag<Item> WEAK_PENTACLES = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_pentacles"));
	public static final Tag<Item> AVERAGE_PENTACLES = TagRegistry.item(new Identifier(Bewitchment.MODID, "average_pentacles"));
	public static final Tag<Item> STRONG_PENTACLES = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_pentacles"));
	
	public static final Tag<Item> WEAK_WANDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_wands"));
	public static final Tag<Item> AVERAGE_WANDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "average_wands"));
	public static final Tag<Item> STRONG_WANDS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_wands"));
}