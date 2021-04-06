package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BWTags {
	public static final Tag<EntityType<?>> VULNERABLE_TO_SILVER = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "vulnerable_to_silver"));
	public static final Tag<EntityType<?>> IMMUNE_TO_SILVER = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "immune_to_silver"));
	public static final Tag<EntityType<?>> HAS_BLOOD = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "has_blood"));
	public static final Tag<EntityType<?>> TAGLOCK_BLACKLIST = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "taglock_blacklist"));
	public static final Tag<EntityType<?>> ENCOUNTER_FORTUNE = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "encounter_fortune"));
	public static final Tag<EntityType<?>> INSANITY_BLACKLIST = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "insanity_blacklist"));
	
	public static final Tag<Item> SKULLS = TagRegistry.item(new Identifier("c", "skulls"));
	public static final Tag<Item> ORES = TagRegistry.item(new Identifier("c", "ores"));
	public static final Tag<Block> GIVES_ALTAR_POWER = TagRegistry.block(new Identifier(Bewitchment.MODID, "gives_altar_power"));
	public static final Tag<Block> HEATS_CAULDRON = TagRegistry.block(new Identifier(Bewitchment.MODID, "heats_cauldron"));
	public static final Tag<Block> UNDEAD_MASK = TagRegistry.block(new Identifier(Bewitchment.MODID, "undead_mask"));
	
	public static final Tag<Item> SILVER_INGOTS = TagRegistry.item(new Identifier("c", "silver_ingots"));
	public static final Tag<Item> SILVER_NUGGETS = TagRegistry.item(new Identifier("c", "silver_nuggets"));
	public static final Tag<Item> BARKS = TagRegistry.item(new Identifier(Bewitchment.MODID, "barks"));
	public static final Tag<Item> WITCHBERRY_FOODS = TagRegistry.item(new Identifier(Bewitchment.MODID, "witchberry_foods"));
	
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
