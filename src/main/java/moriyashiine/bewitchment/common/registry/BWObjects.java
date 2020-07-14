package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.*;
import moriyashiine.bewitchment.common.block.util.*;
import moriyashiine.bewitchment.common.item.BottleOfBloodItem;
import moriyashiine.bewitchment.common.item.tool.AthameItem;
import moriyashiine.bewitchment.common.item.tool.ChalkItem;
import moriyashiine.bewitchment.common.item.tool.SilverArrowItem;
import moriyashiine.bewitchment.common.item.tool.misc.BWAxeItem;
import moriyashiine.bewitchment.common.item.tool.misc.BWHoeItem;
import moriyashiine.bewitchment.common.item.tool.misc.BWPickaxeItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.sapling.OakSaplingGenerator;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BWObjects {
	private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
	
	public static final ItemGroup group = FabricItemGroupBuilder.build(new Identifier(Bewitchment.MODID, Bewitchment.MODID), () -> new ItemStack(BWObjects.athame));
	
	////armor_material
	public static final ArmorMaterial SILVER_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return (int) (ArmorMaterials.GOLD.getDurability(slot) * 1.5f);
		}
		
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return ArmorMaterials.CHAIN.getProtectionAmount(slot);
		}
		
		@Override
		public int getEnchantability() {
			return ArmorMaterials.GOLD.getEnchantability();
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return ArmorMaterials.GOLD.getEquipSound();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(silver_ingot);
		}
		
		@Override
		public String getName() {
			return "silver";
		}
		
		@Override
		public float getToughness() {
			return ArmorMaterials.CHAIN.getToughness();
		}
		
		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.CHAIN.getKnockbackResistance();
		}
	};
	////tool_material
	public static final ToolMaterial SILVER_TOOL = new ToolMaterial() {
		@Override
		public int getDurability() {
			return ToolMaterials.STONE.getDurability();
		}
		
		@Override
		public float getMiningSpeedMultiplier() {
			return ToolMaterials.GOLD.getMiningSpeedMultiplier();
		}
		
		@Override
		public float getAttackDamage() {
			return ToolMaterials.STONE.getAttackDamage();
		}
		
		@Override
		public int getMiningLevel() {
			return ToolMaterials.STONE.getMiningLevel();
		}
		
		@Override
		public int getEnchantability() {
			return ToolMaterials.GOLD.getEnchantability();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(silver_ingot);
		}
	};
	//////block
	////no_item_p1
	//crops
	public static final CropBlock aconite_crops = create("aconite_crops", new BWCropBlock(BWObjects.aconite_seeds, FabricBlockSettings.copy(Blocks.WHEAT)), false);
	public static final CropBlock belladonna_crops = create("belladonna_crops", new BWCropBlock(BWObjects.belladonna_seeds, FabricBlockSettings.copy(aconite_crops)), false);
	public static final CropBlock garlic_crops = create("garlic_crops", new BWCropBlock(BWObjects.garlic, FabricBlockSettings.copy(aconite_crops)), false);
	public static final CropBlock mandrake_crops = create("mandrake_crops", new BWCropBlock(BWObjects.mandrake_seeds, FabricBlockSettings.copy(aconite_crops)), false);
	//door
	public static final Block juniper_door_block = create("juniper_door", new BWDoorBlock(FabricBlockSettings.copy(Blocks.OAK_DOOR)), false);
	public static final Block cypress_door_block = create("cypress_door", new BWDoorBlock(FabricBlockSettings.copy(juniper_door_block)), false);
	public static final Block elder_door_block = create("elder_door", new BWDoorBlock(FabricBlockSettings.copy(juniper_door_block)), false);
	public static final Block dragons_blood_door_block = create("dragons_blood_door", new BWDoorBlock(FabricBlockSettings.copy(juniper_door_block)), false);
	//sign
	public static final Block juniper_standing_sign = create("juniper_sign", new SignBlock(FabricBlockSettings.copyOf(Blocks.OAK_SIGN), SignType.OAK), false);
	public static final Block juniper_wall_sign = create("juniper_wall_sign", new WallSignBlock(FabricBlockSettings.copyOf(Blocks.OAK_WALL_SIGN).dropsLike(juniper_standing_sign), SignType.OAK), false);
	public static final Block cypress_standing_sign = create("cypress_sign", new SignBlock(FabricBlockSettings.copyOf(juniper_standing_sign), SignType.OAK), false);
	public static final Block cypress_wall_sign = create("cypress_wall_sign", new WallSignBlock(FabricBlockSettings.copyOf(juniper_wall_sign).dropsLike(cypress_standing_sign), SignType.OAK), false);
	public static final Block elder_standing_sign = create("elder_sign", new SignBlock(FabricBlockSettings.copyOf(juniper_standing_sign), SignType.OAK), false);
	public static final Block elder_wall_sign = create("elder_wall_sign", new WallSignBlock(FabricBlockSettings.copyOf(juniper_wall_sign).dropsLike(elder_standing_sign), SignType.OAK), false);
	public static final Block dragons_blood_standing_sign = create("dragons_blood_sign", new SignBlock(FabricBlockSettings.copyOf(juniper_standing_sign), SignType.OAK), false);
	public static final Block dragons_blood_wall_sign = create("dragons_blood_wall_sign", new WallSignBlock(FabricBlockSettings.copyOf(juniper_wall_sign).dropsLike(dragons_blood_standing_sign), SignType.OAK), false);
	////wood
	//juniper
	public static final Block juniper_log = create("juniper_log", new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG)), true);
	public static final Block stripped_juniper_log = create("stripped_juniper_log", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_LOG)), true);
	public static final Block juniper_wood = create("juniper_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD)), true);
	public static final Block stripped_juniper_wood = create("stripped_juniper_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_WOOD)), true);
	public static final Block juniper_leaves = create("juniper_leaves", new LeavesBlock(FabricBlockSettings.copy(Blocks.OAK_LEAVES)), true);
	public static final SaplingBlock juniper_sapling = create("juniper_sapling", new BWSaplingBlock(new OakSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)), true);
	public static final Block juniper_planks = create("juniper_planks", new Block(FabricBlockSettings.copy(Blocks.OAK_PLANKS)), true);
	public static final Block juniper_stairs = create("juniper_stairs", new BWStairsBlock(juniper_planks.getDefaultState(), FabricBlockSettings.copy(Blocks.OAK_PLANKS)), true);
	public static final Block juniper_slab = create("juniper_slab", new SlabBlock(FabricBlockSettings.copy(Blocks.OAK_SLAB)), true);
	public static final Block juniper_fence = create("juniper_fence", new FenceBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE)), true);
	public static final Block juniper_fence_gate = create("juniper_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE_GATE)), true);
	public static final Block juniper_trapdoor = create("juniper_trapdoor", new BWTrapdoorBlock(FabricBlockSettings.copy(Blocks.OAK_TRAPDOOR)), true);
	public static final Block juniper_pressure_plate = create("juniper_pressure_plate", new BWPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(Blocks.OAK_PRESSURE_PLATE)), true);
	public static final Block juniper_button = create("juniper_button", new BWWoodButtonBlock(FabricBlockSettings.copy(Blocks.OAK_BUTTON)), true);
	public static final Item juniper_sign = create("juniper_sign", new SignItem(gen(), juniper_standing_sign, juniper_wall_sign));
	//cypress
	public static final Block cypress_log = create("cypress_log", new PillarBlock(FabricBlockSettings.copy(juniper_log)), true);
	public static final Block stripped_cypress_log = create("stripped_cypress_log", new PillarBlock(FabricBlockSettings.copy(stripped_juniper_log)), true);
	public static final Block cypress_wood = create("cypress_wood", new PillarBlock(FabricBlockSettings.copy(juniper_wood)), true);
	public static final Block stripped_cypress_wood = create("stripped_cypress_wood", new PillarBlock(FabricBlockSettings.copy(stripped_juniper_wood)), true);
	public static final Block cypress_leaves = create("cypress_leaves", new LeavesBlock(FabricBlockSettings.copy(juniper_leaves)), true);
	public static final SaplingBlock cypress_sapling = create("cypress_sapling", new BWSaplingBlock(new OakSaplingGenerator(), FabricBlockSettings.copy(juniper_sapling)), true);
	public static final Block cypress_planks = create("cypress_planks", new Block(FabricBlockSettings.copy(juniper_planks)), true);
	public static final Block cypress_stairs = create("cypress_stairs", new BWStairsBlock(cypress_planks.getDefaultState(), FabricBlockSettings.copy(cypress_planks)), true);
	public static final Block cypress_slab = create("cypress_slab", new SlabBlock(FabricBlockSettings.copy(juniper_slab)), true);
	public static final Block cypress_fence = create("cypress_fence", new FenceBlock(FabricBlockSettings.copy(juniper_fence)), true);
	public static final Block cypress_fence_gate = create("cypress_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(juniper_fence_gate)), true);
	public static final Block cypress_trapdoor = create("cypress_trapdoor", new BWTrapdoorBlock(FabricBlockSettings.copy(juniper_trapdoor)), true);
	public static final Block cypress_pressure_plate = create("cypress_pressure_plate", new BWPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(juniper_pressure_plate)), true);
	public static final Block cypress_button = create("cypress_button", new BWWoodButtonBlock(FabricBlockSettings.copy(juniper_button)), true);
	public static final Item cypress_sign = create("cypress_sign", new SignItem(gen(), cypress_standing_sign, cypress_wall_sign));
	//elder
	public static final Block elder_log = create("elder_log", new PillarBlock(FabricBlockSettings.copy(juniper_log)), true);
	public static final Block stripped_elder_log = create("stripped_elder_log", new PillarBlock(FabricBlockSettings.copy(stripped_juniper_log)), true);
	public static final Block elder_wood = create("elder_wood", new PillarBlock(FabricBlockSettings.copy(juniper_wood)), true);
	public static final Block stripped_elder_wood = create("stripped_elder_wood", new PillarBlock(FabricBlockSettings.copy(stripped_juniper_wood)), true);
	public static final Block elder_leaves = create("elder_leaves", new LeavesBlock(FabricBlockSettings.copy(juniper_leaves)), true);
	public static final SaplingBlock elder_sapling = create("elder_sapling", new BWSaplingBlock(new OakSaplingGenerator(), FabricBlockSettings.copy(juniper_sapling)), true);
	public static final Block elder_planks = create("elder_planks", new Block(FabricBlockSettings.copy(juniper_planks)), true);
	public static final Block elder_stairs = create("elder_stairs", new BWStairsBlock(elder_planks.getDefaultState(), FabricBlockSettings.copy(elder_planks)), true);
	public static final Block elder_slab = create("elder_slab", new SlabBlock(FabricBlockSettings.copy(juniper_slab)), true);
	public static final Block elder_fence = create("elder_fence", new FenceBlock(FabricBlockSettings.copy(juniper_fence)), true);
	public static final Block elder_fence_gate = create("elder_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(juniper_fence_gate)), true);
	public static final Block elder_trapdoor = create("elder_trapdoor", new BWTrapdoorBlock(FabricBlockSettings.copy(juniper_trapdoor)), true);
	public static final Block elder_pressure_plate = create("elder_pressure_plate", new BWPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(juniper_pressure_plate)), true);
	public static final Block elder_button = create("elder_button", new BWWoodButtonBlock(FabricBlockSettings.copy(juniper_button)), true);
	public static final Item elder_sign = create("elder_sign", new SignItem(gen(), elder_standing_sign, elder_wall_sign));
	//dragons_blood
	public static final Block dragons_blood_log = create("dragons_blood_log", new DragonsBloodLogBlock(FabricBlockSettings.copy(juniper_log).ticksRandomly()), true);
	public static final Block stripped_dragons_blood_log = create("stripped_dragons_blood_log", new PillarBlock(FabricBlockSettings.copy(stripped_juniper_log)), true);
	public static final Block dragons_blood_wood = create("dragons_blood_wood", new PillarBlock(FabricBlockSettings.copy(juniper_wood)), true);
	public static final Block stripped_dragons_blood_wood = create("stripped_dragons_blood_wood", new PillarBlock(FabricBlockSettings.copy(stripped_juniper_wood)), true);
	public static final Block dragons_blood_leaves = create("dragons_blood_leaves", new LeavesBlock(FabricBlockSettings.copy(juniper_leaves)), true);
	public static final SaplingBlock dragons_blood_sapling = create("dragons_blood_sapling", new BWSaplingBlock(new OakSaplingGenerator(), FabricBlockSettings.copy(juniper_sapling)), true);
	public static final Block dragons_blood_planks = create("dragons_blood_planks", new Block(FabricBlockSettings.copy(juniper_planks)), true);
	public static final Block dragons_blood_stairs = create("dragons_blood_stairs", new BWStairsBlock(dragons_blood_planks.getDefaultState(), FabricBlockSettings.copy(dragons_blood_planks)), true);
	public static final Block dragons_blood_slab = create("dragons_blood_slab", new SlabBlock(FabricBlockSettings.copy(juniper_slab)), true);
	public static final Block dragons_blood_fence = create("dragons_blood_fence", new FenceBlock(FabricBlockSettings.copy(juniper_fence)), true);
	public static final Block dragons_blood_fence_gate = create("dragons_blood_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(juniper_fence_gate)), true);
	public static final Block dragons_blood_trapdoor = create("dragons_blood_trapdoor", new BWTrapdoorBlock(FabricBlockSettings.copy(juniper_trapdoor)), true);
	public static final Block dragons_blood_pressure_plate = create("dragons_blood_pressure_plate", new BWPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(juniper_pressure_plate)), true);
	public static final Block dragons_blood_button = create("dragons_blood_button", new BWWoodButtonBlock(FabricBlockSettings.copy(juniper_button)), true);
	public static final Item dragons_blood_sign = create("dragons_blood_sign", new SignItem(gen(), dragons_blood_standing_sign, dragons_blood_wall_sign));
	////natural
	public static final Block spanish_moss = create("spanish_moss", new VineBlock(FabricBlockSettings.copy(Blocks.VINE)), true);
	////material_block
	//mineral
	public static final Block silver_block = create("silver_block", new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK)), true);
	public static final Block salt_block = create("salt_block", new Block(FabricBlockSettings.copy(Blocks.COAL_BLOCK)), true);
	//natural
	public static final Block dragons_blood_resin_block = create("dragons_blood_resin_block", new Block(FabricBlockSettings.copy(Blocks.COAL_BLOCK).sounds(BlockSoundGroup.NETHER_WART)), true);
	//ores
	public static final Block silver_ore = create("silver_ore", new Block(FabricBlockSettings.copy(Blocks.IRON_ORE)), true);
	public static final Block salt_ore = create("salt_ore", new Block(FabricBlockSettings.copy(Blocks.COAL_ORE)), true);
	//cloth
	public static final Block druid_wool = create("druid_wool", new Block(FabricBlockSettings.copy(Blocks.WHITE_WOOL)), true);
	public static final Block alchemist_wool = create("alchemist_wool", new Block(FabricBlockSettings.copy(druid_wool)), true);
	public static final Block besmirched_wool = create("besmirched_wool", new Block(FabricBlockSettings.copy(druid_wool)), true);
	public static final Block druid_carpet = create("druid_carpet", new BWCarpetBlock(DyeColor.GREEN, FabricBlockSettings.copy(Blocks.WHITE_CARPET)), true);
	public static final Block alchemist_carpet = create("alchemist_carpet", new BWCarpetBlock(DyeColor.GRAY, FabricBlockSettings.copy(druid_carpet)), true);
	public static final Block besmirched_carpet = create("besmirched_carpet", new BWCarpetBlock(DyeColor.BLACK, FabricBlockSettings.copy(druid_carpet)), true);
	//candelabra
	public static final Block iron_candelabra = create("iron_candelabra", new CandelabraBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).lightLevel(state -> state.get(Properties.LIT) ? 15 : 0)), true);
	public static final Block golden_candelabra = create("golden_candelabra", new CandelabraBlock(FabricBlockSettings.copy(Blocks.GOLD_BLOCK).lightLevel(state -> state.get(Properties.LIT) ? 15 : 0)), true);
	public static final Block silver_candelabra = create("silver_candelabra", new CandelabraBlock(FabricBlockSettings.copy(silver_block).lightLevel(state -> state.get(Properties.LIT) ? 15 : 0)), true);
	public static final Block netherite_candelabra = create("netherite_candelabra", new CandelabraBlock(FabricBlockSettings.copy(Blocks.NETHERITE_BLOCK).lightLevel(state -> state.get(Properties.LIT) ? 15 : 0)), true);
	//goblet
	public static final Block iron_goblet = create("iron_goblet", new GobletBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK)), true);
	public static final Block golden_goblet = create("golden_goblet", new GobletBlock(FabricBlockSettings.copy(Blocks.GOLD_BLOCK)), true);
	public static final Block silver_goblet = create("silver_goblet", new GobletBlock(FabricBlockSettings.copy(silver_block)), true);
	public static final Block netherite_goblet = create("netherite_goblet", new GobletBlock(FabricBlockSettings.copy(Blocks.NETHERITE_BLOCK)), true);
	////tile
	public static final Block[] stone_witch_altar = createWitchAltar("stone_witch_altar", Blocks.STONE);
	public static final Block[] mossy_cobblestone_witch_altar = createWitchAltar("mossy_cobblestone_witch_altar", Blocks.MOSSY_COBBLESTONE);
	public static final Block[] prismarine_witch_altar = createWitchAltar("prismarine_witch_altar", Blocks.PRISMARINE);
	public static final Block[] blackstone_witch_altar = createWitchAltar("blackstone_witch_altar", Blocks.BLACKSTONE);
	public static final Block[] nether_brick_witch_altar = createWitchAltar("nether_brick_witch_altar", Blocks.NETHER_BRICKS);
	public static final Block[] golden_witch_altar = createWitchAltar("golden_witch_altar", Blocks.GOLD_BLOCK);
	public static final Block[] end_stone_witch_altar = createWitchAltar("end_stone_witch_altar", Blocks.END_STONE);
	public static final Block[] obsidian_witch_altar = createWitchAltar("obsidian_witch_altar", Blocks.OBSIDIAN);
	public static final Block[] purpur_witch_altar = createWitchAltar("purpur_witch_altar", Blocks.PURPUR_BLOCK);
	public static final Block distillery = create("distillery", new DistilleryBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).nonOpaque()), true);
	public static final Block spinning_wheel = create("spinning_wheel", new SpinningWheelBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque()), true);
	////no_item_p2
	//flower_pot
	public static final Block potted_juniper_sapling = create("potted_juniper_sapling", new FlowerPotBlock(BWObjects.juniper_sapling, FabricBlockSettings.copy(Blocks.POTTED_OAK_SAPLING)), false);
	public static final Block potted_cypress_sapling = create("potted_cypress_sapling", new FlowerPotBlock(BWObjects.cypress_sapling, FabricBlockSettings.copy(potted_juniper_sapling)), false);
	public static final Block potted_elder_sapling = create("potted_elder_sapling", new FlowerPotBlock(BWObjects.elder_sapling, FabricBlockSettings.copy(potted_juniper_sapling)), false);
	public static final Block potted_dragons_blood_sapling = create("potted_dragons_blood_sapling", new FlowerPotBlock(BWObjects.dragons_blood_sapling, FabricBlockSettings.copy(potted_juniper_sapling)), false);
	//chalk
	public static final ChalkBlock focal_chalk_block = create("focal_chalk", new ChalkBlock(FabricBlockSettings.of(Material.SUPPORTED).strength(2, 0).breakByTool(FabricToolTags.PICKAXES).collidable(false).dropsNothing().sounds(BWSoundTypes.chalk)), false);
	public static final ChalkBlock chalk_block = create("chalk", new ChalkBlock(FabricBlockSettings.copy(focal_chalk_block)), false);
	public static final ChalkBlock infernal_chalk_block = create("infernal_chalk", new ChalkBlock(FabricBlockSettings.copy(focal_chalk_block).lightLevel(state -> 4)), false);
	public static final ChalkBlock eldritch_chalk_block = create("eldritch_chalk", new ChalkBlock(FabricBlockSettings.copy(focal_chalk_block).lightLevel(state -> 2)), false);
	//////item
	////door
	public static final Item juniper_door_item = create("juniper_door", new TallBlockItem(juniper_door_block, gen()));
	public static final Item cypress_door_item = create("cypress_door", new TallBlockItem(cypress_door_block, gen()));
	public static final Item elder_door_item = create("elder_door", new TallBlockItem(elder_door_block, gen()));
	public static final Item dragons_blood_door_item = create("dragons_blood_door", new TallBlockItem(dragons_blood_door_block, gen()));
	////armor
	public static final Item silver_helmet = create("silver_helmet", new ArmorItem(SILVER_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item silver_chestplate = create("silver_chestplate", new ArmorItem(SILVER_ARMOR, EquipmentSlot.CHEST, gen()));
	public static final Item silver_leggings = create("silver_leggings", new ArmorItem(SILVER_ARMOR, EquipmentSlot.LEGS, gen()));
	public static final Item silver_boots = create("silver_boots", new ArmorItem(SILVER_ARMOR, EquipmentSlot.FEET, gen()));
	////tool
	//silver
	public static final Item silver_sword = create("silver_sword", new SwordItem(SILVER_TOOL, 3, -2.4f, gen()));
	public static final Item silver_pickaxe = create("silver_pickaxe", new BWPickaxeItem(SILVER_TOOL, 1, -2.8f, gen()));
	public static final BWAxeItem silver_axe = create("silver_axe", new BWAxeItem(SILVER_TOOL, 7, -3, gen()));
	public static final Item silver_shovel = create("silver_shovel", new ShovelItem(SILVER_TOOL, 1.5f, -3, gen()));
	public static final Item silver_hoe = create("silver_hoe", new BWHoeItem(SILVER_TOOL, -1, -3, gen()));
	//misc
	public static final Item athame = create("athame", new AthameItem(SILVER_TOOL, 1, -2, gen()));
	public static final Item silver_arrow = create("silver_arrow", new SilverArrowItem(gen()));
	public static final Item focal_chalk = create("focal_chalk", new ChalkItem(focal_chalk_block, gen().maxDamage(252)));
	public static final Item chalk = create("chalk", new ChalkItem(chalk_block, gen().maxDamage(252)));
	public static final Item infernal_chalk = create("infernal_chalk", new ChalkItem(infernal_chalk_block, gen().maxDamage(252)));
	public static final Item eldritch_chalk = create("eldritch_chalk", new ChalkItem(eldritch_chalk_block, gen().maxDamage(252)));
	////material_item
	//oil
	public static final Item fiery_serum = create("fiery_serum", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item aqua_cerate = create("aqua_cerate", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item earth_ichor = create("earth_ichor", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item heaven_extract = create("heaven_extract", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item cleansing_balm = create("cleansing_balm", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item fell_elixir = create("fell_elixir", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	//thread
	public static final Item witchs_stitches = create("witchs_stitches", new Item(gen()));
	public static final Item golden_thread = create("golden_thread", new Item(gen()));
	public static final Item spirit_string = create("spirit_string", new Item(gen()));
	public static final Item pure_filament = create("pure_filament", new Item(gen()));
	public static final Item sanguine_fabric = create("sanguine_fabric", new Item(gen()));
	//mineral
	public static final Item silver_ingot = create("silver_ingot", new Item(gen()));
	public static final Item silver_nugget = create("silver_nugget", new Item(gen()));
	public static final Item salt = create("salt", new Item(gen()));
	//natural
	public static final Item oak_bark = create("oak_bark", new Item(gen()));
	public static final Item spruce_bark = create("spruce_bark", new Item(gen()));
	public static final Item birch_bark = create("birch_bark", new Item(gen()));
	public static final Item juniper_bark = create("juniper_bark", new Item(gen()));
	public static final Item cypress_bark = create("cypress_bark", new Item(gen()));
	public static final Item elder_bark = create("elder_bark", new Item(gen()));
	public static final Item wood_ash = create("wood_ash", new Item(gen()));
	public static final Item dragons_blood_resin = create("dragons_blood_resin", new Item(gen()));
	public static final Item aconite = create("aconite", new Item(gen()));
	public static final Item aconite_seeds = create("aconite_seeds", new AliasedBlockItem(aconite_crops, gen()));
	public static final Item belladonna = create("belladonna", new Item(gen()));
	public static final Item belladonna_seeds = create("belladonna_seeds", new AliasedBlockItem(belladonna_crops, gen()));
	public static final Item garlic = create("garlic", new AliasedBlockItem(garlic_crops, gen().food(FoodComponents.POTATO)));
	public static final Item mandrake_root = create("mandrake_root", new Item(gen()));
	public static final Item mandrake_seeds = create("mandrake_seeds", new AliasedBlockItem(mandrake_crops, gen()));
	//mob_drop
	public static final Item owl_wing = create("owl_wing", new Item(gen()));
	public static final Item snake_tongue = create("snake_tongue", new Item(gen()));
	public static final Item toad_foot = create("toad_foot", new Item(gen()));
	public static final Item ectoplasm = create("ectoplasm", new Item(gen()));
	public static final Item demon_horn = create("demon_horn", new Item(gen()));
	public static final Item demon_heart = create("demon_heart", new Item(gen().food(BWFoodComponents.demon_heart)));
	public static final Item bottle_of_blood = create("bottle_of_blood", new BottleOfBloodItem(gen()));
	//food
	public static final Item elderberries = create("elderberries", new Item(gen().food(FoodComponents.APPLE)));
	public static final Item grilled_garlic = create("grilled_garlic", new Item(gen().food(FoodComponents.BAKED_POTATO)));
	public static final Item garlic_bread = create("garlic_bread", new Item(gen().food(BWFoodComponents.garlic_bread)));
	////spawn_egg
	public static final Item owl_spawn_egg = create("owl_spawn_egg", new SpawnEggItem(BWEntityTypes.owl, 0xff7f00, 0xffffff, gen()));
	public static final Item raven_spawn_egg = create("raven_spawn_egg", new SpawnEggItem(BWEntityTypes.raven, 0x000000, 0x3f3f3f, gen()));
	public static final Item snake_spawn_egg = create("snake_spawn_egg", new SpawnEggItem(BWEntityTypes.snake, 0x7f3f00, 0x3f3f3f, gen()));
	public static final Item toad_spawn_egg = create("toad_spawn_egg", new SpawnEggItem(BWEntityTypes.toad, 0x007f00, 0x7f3f00, gen()));
	
	private static Block[] createWitchAltar(String name, Block base) {
		int id = -1;
		Block[] altars = new Block[20];
		Block unformed = create(name, new UnformedWitchAltarBlock(FabricBlockSettings.copy(base)), true);
		altars[++id] = unformed;
		for (DyeColor color : DyeColor.values()) {
			Block carpet;
			switch (color) {
				case WHITE:
					carpet = Blocks.WHITE_CARPET;
					break;
				case ORANGE:
					carpet = Blocks.ORANGE_CARPET;
					break;
				case MAGENTA:
					carpet = Blocks.MAGENTA_CARPET;
					break;
				case LIGHT_BLUE:
					carpet = Blocks.LIGHT_BLUE_CARPET;
					break;
				case YELLOW:
					carpet = Blocks.YELLOW_CARPET;
					break;
				case LIME:
					carpet = Blocks.LIME_CARPET;
					break;
				case PINK:
					carpet = Blocks.PINK_CARPET;
					break;
				case GRAY:
					carpet = Blocks.GRAY_CARPET;
					break;
				case LIGHT_GRAY:
					carpet = Blocks.LIGHT_GRAY_CARPET;
					break;
				case CYAN:
					carpet = Blocks.CYAN_CARPET;
					break;
				case PURPLE:
					carpet = Blocks.PURPLE_CARPET;
					break;
				case BLUE:
					carpet = Blocks.BLUE_CARPET;
					break;
				case BROWN:
					carpet = Blocks.BROWN_CARPET;
					break;
				case GREEN:
					carpet = Blocks.GREEN_CARPET;
					break;
				case RED:
					carpet = Blocks.RED_CARPET;
					break;
				case BLACK:
					carpet = Blocks.BLACK_CARPET;
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + color);
			}
			BewitchmentAPI.registerAltarMap(unformed, altars[++id] = create(color.getName() + "_" + name, new FormedWitchAltarBlock(unformed, FabricBlockSettings.copy(base)), false), carpet);
		}
		BewitchmentAPI.registerAltarMap(unformed, altars[++id] = create("druid_" + name, new FormedWitchAltarBlock(unformed, FabricBlockSettings.copy(base)), false), druid_carpet);
		BewitchmentAPI.registerAltarMap(unformed, altars[++id] = create("alchemist_" + name, new FormedWitchAltarBlock(unformed, FabricBlockSettings.copy(base)), false), alchemist_carpet);
		BewitchmentAPI.registerAltarMap(unformed, altars[++id] = create("besmirched_" + name, new FormedWitchAltarBlock(unformed, FabricBlockSettings.copy(base)), false), besmirched_carpet);
		return altars;
	}
	
	private static Item.Settings gen() {
		return new Item.Settings().group(group);
	}
	
	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Bewitchment.MODID, name));
		return item;
	}
	
	private static <T extends Block> T create(String name, T block, boolean registerItem) {
		BLOCKS.put(block, new Identifier(Bewitchment.MODID, name));
		if (registerItem) {
			ITEMS.put(new BlockItem(block, gen()), new Identifier(Bewitchment.MODID, name));
		}
		return block;
	}
	
	public static void init() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
		
		//		HashMap<Block, Block> MODIFIED_STRIPPED_BLOCKS = new HashMap<>(STRIPPED_BLOCKS);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.juniper_log, BWObjects.stripped_juniper_log);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.juniper_wood, BWObjects.stripped_juniper_wood);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.cypress_log, BWObjects.stripped_cypress_log);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.cypress_wood, BWObjects.stripped_cypress_wood);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.elder_log, BWObjects.stripped_elder_log);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.elder_wood, BWObjects.stripped_elder_wood);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.dragons_blood_log, BWObjects.stripped_dragons_blood_log);
		//		MODIFIED_STRIPPED_BLOCKS.put(BWObjects.dragons_blood_wood, BWObjects.stripped_dragons_blood_wood);
		
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(juniper_leaves, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(juniper_sapling, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(cypress_leaves, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(cypress_sapling, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(elder_leaves, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(elder_sapling, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(dragons_blood_leaves, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(dragons_blood_sapling, 0.3f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(elderberries, 0.65f);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(spanish_moss, 0.5f);
		
		FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();
		flammableRegistry.add(juniper_log, 5, 5);
		flammableRegistry.add(stripped_juniper_log, 5, 5);
		flammableRegistry.add(juniper_wood, 5, 5);
		flammableRegistry.add(stripped_juniper_wood, 5, 5);
		flammableRegistry.add(juniper_leaves, 30, 60);
		flammableRegistry.add(juniper_planks, 5, 20);
		flammableRegistry.add(juniper_stairs, 5, 20);
		flammableRegistry.add(juniper_slab, 5, 20);
		flammableRegistry.add(juniper_fence, 5, 20);
		flammableRegistry.add(juniper_fence_gate, 5, 20);
		flammableRegistry.add(cypress_log, 5, 5);
		flammableRegistry.add(stripped_cypress_log, 5, 5);
		flammableRegistry.add(cypress_wood, 5, 5);
		flammableRegistry.add(stripped_cypress_wood, 5, 5);
		flammableRegistry.add(cypress_leaves, 30, 60);
		flammableRegistry.add(cypress_planks, 5, 20);
		flammableRegistry.add(cypress_stairs, 5, 20);
		flammableRegistry.add(cypress_slab, 5, 20);
		flammableRegistry.add(cypress_fence, 5, 20);
		flammableRegistry.add(cypress_fence_gate, 5, 20);
		flammableRegistry.add(elder_log, 5, 5);
		flammableRegistry.add(stripped_elder_log, 5, 5);
		flammableRegistry.add(elder_wood, 5, 5);
		flammableRegistry.add(stripped_elder_wood, 5, 5);
		flammableRegistry.add(elder_leaves, 30, 60);
		flammableRegistry.add(elder_planks, 5, 20);
		flammableRegistry.add(elder_stairs, 5, 20);
		flammableRegistry.add(elder_slab, 5, 20);
		flammableRegistry.add(elder_fence, 5, 20);
		flammableRegistry.add(elder_fence_gate, 5, 20);
		flammableRegistry.add(dragons_blood_log, 5, 5);
		flammableRegistry.add(stripped_dragons_blood_log, 5, 5);
		flammableRegistry.add(dragons_blood_wood, 5, 5);
		flammableRegistry.add(stripped_dragons_blood_wood, 5, 5);
		flammableRegistry.add(dragons_blood_leaves, 30, 60);
		flammableRegistry.add(dragons_blood_planks, 5, 20);
		flammableRegistry.add(dragons_blood_stairs, 5, 20);
		flammableRegistry.add(dragons_blood_slab, 5, 20);
		flammableRegistry.add(dragons_blood_fence, 5, 20);
		flammableRegistry.add(dragons_blood_fence_gate, 5, 20);
		
		flammableRegistry.add(druid_wool, 30, 60);
		flammableRegistry.add(alchemist_wool, 30, 60);
		flammableRegistry.add(besmirched_wool, 30, 60);
		flammableRegistry.add(druid_carpet, 60, 20);
		flammableRegistry.add(alchemist_carpet, 60, 20);
		flammableRegistry.add(besmirched_carpet, 60, 20);
		
		flammableRegistry.add(spanish_moss, 15, 100);
		
		BewitchmentAPI.registerLogToBark(Blocks.OAK_LOG, oak_bark);
		BewitchmentAPI.registerLogToBark(Blocks.DARK_OAK_LOG, oak_bark);
		BewitchmentAPI.registerLogToBark(Blocks.SPRUCE_LOG, spruce_bark);
		BewitchmentAPI.registerLogToBark(Blocks.BIRCH_LOG, birch_bark);
		BewitchmentAPI.registerLogToBark(juniper_log, juniper_bark);
		BewitchmentAPI.registerLogToBark(cypress_log, cypress_bark);
		BewitchmentAPI.registerLogToBark(elder_log, elder_bark);
	}
}