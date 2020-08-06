package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BWTags {
	public static final Tag<EntityType<?>> HAS_BLOOD = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "has_blood"));
	public static final Tag<EntityType<?>> WEAK_TO_SILVER = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "weak_to_silver"));
	
	public static final Tag<Block> GIVES_ALTAR_POWER = TagRegistry.block(new Identifier(Bewitchment.MODID, "gives_altar_power"));
	
	public static final Tag<Item> SILVER_TOOLS = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_tools"));
	public static final Tag<Item> SILVER_ARMOR = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_armor"));
	public static final Tag<Item> SILVER_NUGGETS = TagRegistry.item(new Identifier("c", "silver_nuggets"));
	
	public static final Tag<Item> WEAK_SWORD_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_sword_modifiers"));
	public static final Tag<Item> MODERATE_SWORD_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_sword_modifiers"));
	public static final Tag<Item> STRONG_SWORD_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_sword_modifiers"));
	
	public static final Tag<Item> WEAK_CUP_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_cup_modifiers"));
	public static final Tag<Item> MODERATE_CUP_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_cup_modifiers"));
	public static final Tag<Item> STRONG_CUP_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_cup_modifiers"));
	
	public static final Tag<Item> WEAK_WAND_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_wand_modifiers"));
	public static final Tag<Item> MODERATE_WAND_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_wand_modifiers"));
	public static final Tag<Item> STRONG_WAND_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_wand_modifiers"));
	
	public static final Tag<Item> WEAK_PENTACLE_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_pentacle_modifiers"));
	public static final Tag<Item> MODERATE_PENTACLE_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_pentacle_modifiers"));
	public static final Tag<Item> STRONG_PENTACLE_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_pentacle_modifiers"));
}
