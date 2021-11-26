package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BWTags {
	public static final Tag<EntityType<?>> VULNERABLE_TO_SILVER = TagFactory.ENTITY_TYPE.create(new Identifier(Bewitchment.MODID, "vulnerable_to_silver"));
	public static final Tag<EntityType<?>> IMMUNE_TO_SILVER = TagFactory.ENTITY_TYPE.create(new Identifier(Bewitchment.MODID, "immune_to_silver"));
	public static final Tag<EntityType<?>> HAS_BLOOD = TagFactory.ENTITY_TYPE.create(new Identifier(Bewitchment.MODID, "has_blood"));
	public static final Tag<EntityType<?>> TAGLOCK_BLACKLIST = TagFactory.ENTITY_TYPE.create(new Identifier(Bewitchment.MODID, "taglock_blacklist"));
	public static final Tag<EntityType<?>> ENCOUNTER_FORTUNE = TagFactory.ENTITY_TYPE.create(new Identifier(Bewitchment.MODID, "encounter_fortune"));
	public static final Tag<EntityType<?>> INSANITY_BLACKLIST = TagFactory.ENTITY_TYPE.create(new Identifier(Bewitchment.MODID, "insanity_blacklist"));
	
	public static final Tag<Item> SKULLS = TagFactory.ITEM.create(new Identifier("c", "skulls"));
	public static final Tag<Item> ORES = TagFactory.ITEM.create(new Identifier("c", "ores"));
	public static final Tag<Block> GIVES_ALTAR_POWER = TagFactory.BLOCK.create(new Identifier(Bewitchment.MODID, "gives_altar_power"));
	public static final Tag<Block> HEATS_CAULDRON = TagFactory.BLOCK.create(new Identifier(Bewitchment.MODID, "heats_cauldron"));
	public static final Tag<Block> NATURAL_TERRAIN = TagFactory.BLOCK.create(new Identifier(Bewitchment.MODID, "natural_terrain"));
	public static final Tag<Block> UNDEAD_MASK = TagFactory.BLOCK.create(new Identifier(Bewitchment.MODID, "undead_mask"));
	
	public static final Tag<Item> SILVER_INGOTS = TagFactory.ITEM.create(new Identifier("c", "silver_ingots"));
	public static final Tag<Item> SILVER_NUGGETS = TagFactory.ITEM.create(new Identifier("c", "silver_nuggets"));
	public static final Tag<Item> BARKS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "barks"));
	public static final Tag<Item> WITCHBERRY_FOODS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "witchberry_foods"));
	
	public static final Tag<Item> SWORDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "swords"));
	public static final Tag<Item> PENTACLES = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "pentacles"));
	public static final Tag<Item> WANDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "wands"));
	
	public static final Tag<Item> WEAK_SWORDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "weak_swords"));
	public static final Tag<Item> AVERAGE_SWORDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "average_swords"));
	public static final Tag<Item> STRONG_SWORDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "strong_swords"));
	
	public static final Tag<Item> WEAK_PENTACLES = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "weak_pentacles"));
	public static final Tag<Item> AVERAGE_PENTACLES = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "average_pentacles"));
	public static final Tag<Item> STRONG_PENTACLES = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "strong_pentacles"));
	
	public static final Tag<Item> WEAK_WANDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "weak_wands"));
	public static final Tag<Item> AVERAGE_WANDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "average_wands"));
	public static final Tag<Item> STRONG_WANDS = TagFactory.ITEM.create(new Identifier(Bewitchment.MODID, "strong_wands"));
}
