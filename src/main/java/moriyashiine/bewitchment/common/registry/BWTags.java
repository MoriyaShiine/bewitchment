/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class BWTags {
	public static final TagKey<EntityType<?>> VULNERABLE_TO_SILVER = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MODID, "vulnerable_to_silver"));
	public static final TagKey<EntityType<?>> IMMUNE_TO_SILVER = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MODID, "immune_to_silver"));
	public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MODID, "has_blood"));
	public static final TagKey<EntityType<?>> TAGLOCK_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MODID, "taglock_blacklist"));
	public static final TagKey<EntityType<?>> ENCOUNTER_FORTUNE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MODID, "encounter_fortune"));
	public static final TagKey<EntityType<?>> INSANITY_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MODID, "insanity_blacklist"));

	public static final TagKey<Block> ORES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "ores"));
	public static final TagKey<Item> SKULLS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "skulls"));
	public static final TagKey<Block> GIVES_ALTAR_POWER = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MODID, "gives_altar_power"));
	public static final TagKey<Block> HEATS_CAULDRON = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MODID, "heats_cauldron"));
	public static final TagKey<Block> NATURAL_TERRAIN = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MODID, "natural_terrain"));
	public static final TagKey<Block> UNDEAD_MASK = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MODID, "undead_mask"));

	public static final TagKey<Item> SILVER_INGOTS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_ingots"));
	public static final TagKey<Item> SILVER_NUGGETS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_nuggets"));
	public static final TagKey<Item> BARKS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "barks"));
	public static final TagKey<Item> WITCHBERRY_FOODS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "witchberry_foods"));

	public static final TagKey<Item> SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "swords"));
	public static final TagKey<Item> PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "pentacles"));
	public static final TagKey<Item> WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "wands"));

	public static final TagKey<Item> WEAK_SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "weak_swords"));
	public static final TagKey<Item> AVERAGE_SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "average_swords"));
	public static final TagKey<Item> STRONG_SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "strong_swords"));

	public static final TagKey<Item> WEAK_PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "weak_pentacles"));
	public static final TagKey<Item> AVERAGE_PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "average_pentacles"));
	public static final TagKey<Item> STRONG_PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "strong_pentacles"));

	public static final TagKey<Item> WEAK_WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "weak_wands"));
	public static final TagKey<Item> AVERAGE_WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "average_wands"));
	public static final TagKey<Item> STRONG_WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "strong_wands"));

	public static final TagKey<Item> SUN_GLASSES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MODID, "sun_glasses"));
}
