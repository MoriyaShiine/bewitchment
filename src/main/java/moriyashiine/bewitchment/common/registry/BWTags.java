/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class BWTags {
	public static final TagKey<Biome> SPAWNS_HELLHOUNDS = TagKey.of(RegistryKeys.BIOME, new Identifier(Bewitchment.MOD_ID, "spawns_hellhounds"));

	public static final TagKey<EntityType<?>> VULNERABLE_TO_SILVER = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MOD_ID, "vulnerable_to_silver"));
	public static final TagKey<EntityType<?>> IMMUNE_TO_SILVER = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MOD_ID, "immune_to_silver"));
	public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MOD_ID, "has_blood"));
	public static final TagKey<EntityType<?>> TAGLOCK_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MOD_ID, "taglock_blacklist"));
	public static final TagKey<EntityType<?>> ENCOUNTER_FORTUNE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MOD_ID, "encounter_fortune"));
	public static final TagKey<EntityType<?>> INSANITY_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Bewitchment.MOD_ID, "insanity_blacklist"));

	public static final TagKey<Block> ORES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "ores"));
	public static final TagKey<Item> SKULLS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "skulls"));
	public static final TagKey<Block> GIVES_ALTAR_POWER = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MOD_ID, "gives_altar_power"));
	public static final TagKey<Block> HEATS_CAULDRON = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MOD_ID, "heats_cauldron"));
	public static final TagKey<Block> NATURAL_TERRAIN = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MOD_ID, "natural_terrain"));
	public static final TagKey<Block> UNDEAD_MASK = TagKey.of(RegistryKeys.BLOCK, new Identifier(Bewitchment.MOD_ID, "undead_mask"));

	public static final TagKey<Item> SILVER_INGOTS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_ingots"));
	public static final TagKey<Item> SILVER_NUGGETS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_nuggets"));
	public static final TagKey<Item> BARKS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "barks"));
	public static final TagKey<Item> WITCHBERRY_FOODS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "witchberry_foods"));

	public static final TagKey<Item> SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "swords"));
	public static final TagKey<Item> PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "pentacles"));
	public static final TagKey<Item> WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "wands"));

	public static final TagKey<Item> WEAK_SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "weak_swords"));
	public static final TagKey<Item> AVERAGE_SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "average_swords"));
	public static final TagKey<Item> STRONG_SWORDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "strong_swords"));

	public static final TagKey<Item> WEAK_PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "weak_pentacles"));
	public static final TagKey<Item> AVERAGE_PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "average_pentacles"));
	public static final TagKey<Item> STRONG_PENTACLES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "strong_pentacles"));

	public static final TagKey<Item> WEAK_WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "weak_wands"));
	public static final TagKey<Item> AVERAGE_WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "average_wands"));
	public static final TagKey<Item> STRONG_WANDS = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "strong_wands"));

	public static final TagKey<Item> SUN_GLASSES = TagKey.of(RegistryKeys.ITEM, new Identifier(Bewitchment.MOD_ID, "sun_glasses"));
}
