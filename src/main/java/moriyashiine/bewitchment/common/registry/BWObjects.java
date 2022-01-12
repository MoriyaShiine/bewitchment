/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import com.terraformersmc.terraform.wood.block.*;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.CandelabraBlock;
import moriyashiine.bewitchment.api.block.PoppetShelfBlock;
import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.item.BroomItem;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.item.SigilItem;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.*;
import moriyashiine.bewitchment.common.block.dragonsblood.*;
import moriyashiine.bewitchment.common.block.elder.*;
import moriyashiine.bewitchment.common.block.juniper.*;
import moriyashiine.bewitchment.common.block.util.BWCropBlock;
import moriyashiine.bewitchment.common.block.util.BWSaplingBlock;
import moriyashiine.bewitchment.common.item.*;
import moriyashiine.bewitchment.common.item.util.BWBookItem;
import moriyashiine.bewitchment.common.world.generator.tree.generator.CypressSaplingGenerator;
import moriyashiine.bewitchment.common.world.generator.tree.generator.DragonsBloodSaplingGenerator;
import moriyashiine.bewitchment.common.world.generator.tree.generator.ElderSaplingGenerator;
import moriyashiine.bewitchment.common.world.generator.tree.generator.JuniperSaplingGenerator;
import moriyashiine.bewitchment.mixin.BlocksAccessor;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copyOf;
import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.of;

public class BWObjects {
	private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//misc_no_item
	public static final Block SALT_LINE = create("salt_line", new SaltLineBlock(copyOf(Blocks.REDSTONE_WIRE)), false);
	public static final Block TEMPORARY_COBWEB = create("temporary_cobweb", new CobwebBlock(copyOf(Blocks.COBWEB).dropsNothing().ticksRandomly()) {
		@Override
		public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}, false);
	public static final Block GOLDEN_GLYPH = create("golden_glyph", new GlyphBlock(FabricBlockSettings.of(Material.DECORATION).sounds(new BlockSoundGroup(1, 1, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, BWSoundEvents.BLOCK_GLPYH_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL)).noCollision().dropsNothing().strength(1, 0)), false);
	public static final Block GLYPH = create("glyph", new GlyphBlock(copyOf(GOLDEN_GLYPH)), false);
	public static final Block FIERY_GLYPH = create("fiery_glyph", new GlyphBlock(copyOf(GOLDEN_GLYPH).luminance(4)), false);
	public static final Block ELDRITCH_GLYPH = create("eldritch_glyph", new GlyphBlock(copyOf(GOLDEN_GLYPH).luminance(2)), false);
	public static final Block SIGIL = create("sigil", new SigilBlock(FabricBlockSettings.of(Material.DECORATION).sounds(BlockSoundGroup.BAMBOO).noCollision().dropsNothing().breakInstantly()), false);
	//crop
	public static final Block ACONITE_CROP = create("aconite", new BWCropBlock(copyOf(Blocks.WHEAT)), false);
	public static final Block BELLADONNA_CROP = create("belladonna", new BWCropBlock(copyOf(ACONITE_CROP)), false);
	public static final Block GARLIC_CROP = create("garlic", new BWCropBlock(copyOf(ACONITE_CROP)), false);
	public static final Block MANDRAKE_CROP = create("mandrake", new BWCropBlock(copyOf(ACONITE_CROP)), false);
	//juniper
	public static final Block STRIPPED_JUNIPER_LOG = create("stripped_juniper_log", new PillarBlock(copyOf(Blocks.OAK_LOG)), true);
	public static final Block STRIPPED_JUNIPER_WOOD = create("stripped_juniper_wood", new PillarBlock(copyOf(STRIPPED_JUNIPER_LOG)), true);
	public static final Block JUNIPER_LOG = create("juniper_log", new StrippableLogBlock(() -> STRIPPED_JUNIPER_LOG, MapColor.BROWN, copyOf(STRIPPED_JUNIPER_LOG)), true);
	public static final Block JUNIPER_WOOD = create("juniper_wood", new StrippableLogBlock(() -> STRIPPED_JUNIPER_WOOD, MapColor.BROWN, copyOf(STRIPPED_JUNIPER_LOG)), true);
	public static final Block JUNIPER_LEAVES = create("juniper_leaves", BlocksAccessor.callCreateLeavesBlock(BlockSoundGroup.GRASS), true);
	public static final Block JUNIPER_SAPLING = create("juniper_sapling", new BWSaplingBlock(new JuniperSaplingGenerator(), copyOf(Blocks.OAK_SAPLING)), true);
	public static final Block POTTED_JUNIPER_SAPLING = create("potted_juniper_sapling", new FlowerPotBlock(JUNIPER_SAPLING, copyOf(Blocks.POTTED_OAK_SAPLING)), false);
	public static final Block JUNIPER_PLANKS = create("juniper_planks", new Block(copyOf(Blocks.OAK_PLANKS)), true);
	public static final Block JUNIPER_STAIRS = create("juniper_stairs", new TerraformStairsBlock(JUNIPER_PLANKS, copyOf(Blocks.OAK_STAIRS)), true);
	public static final Block JUNIPER_SLAB = create("juniper_slab", new SlabBlock(copyOf(Blocks.OAK_SLAB)), true);
	public static final Block JUNIPER_FENCE = create("juniper_fence", new FenceBlock(copyOf(Blocks.OAK_FENCE)), true);
	public static final Block JUNIPER_FENCE_GATE = create("juniper_fence_gate", new JuniperFenceGateBlock(copyOf(Blocks.OAK_FENCE_GATE)), true);
	public static final Block JUNIPER_PRESSURE_PLATE = create("juniper_pressure_plate", new JuniperPressurePlateBlock(copyOf(Blocks.OAK_PRESSURE_PLATE)), true);
	public static final Block JUNIPER_BUTTON = create("juniper_button", new JuniperButtonBlock(copyOf(Blocks.OAK_BUTTON)), true);
	public static final Block JUNIPER_TRAPDOOR = create("juniper_trapdoor", new JuniperTrapdoorBlock(copyOf(Blocks.OAK_TRAPDOOR)), true);
	public static final Block JUNIPER_DOOR = create("juniper_door", new JuniperDoorBlock(copyOf(Blocks.OAK_DOOR)), false);
	public static final Item JUNIPER_DOOR_ITEM = create("juniper_door", new TallBlockItem(JUNIPER_DOOR, gen()));
	public static final Block JUNIPER_CHEST = create("juniper_chest", new JuniperChestBlock(copyOf(Blocks.CHEST), () -> BWBlockEntityTypes.JUNIPER_CHEST, false), true);
	public static final Block TRAPPED_JUNIPER_CHEST = create("trapped_juniper_chest", new JuniperChestBlock(copyOf(Blocks.CHEST), () -> BWBlockEntityTypes.JUNIPER_CHEST, true), true);
	private static final Identifier JUNIPER_SIGN_TEXTURE = new Identifier(Bewitchment.MODID, "entity/sign/juniper");
	public static final TerraformSignBlock JUNIPER_SIGN = create("juniper_sign", new TerraformSignBlock(JUNIPER_SIGN_TEXTURE, copyOf(Blocks.OAK_SIGN)), false);
	public static final Block JUNIPER_WALL_SIGN = create("juniper_wall_sign", new TerraformWallSignBlock(JUNIPER_SIGN_TEXTURE, copyOf(Blocks.OAK_WALL_SIGN)), false);
	public static final Item JUNIPER_SIGN_ITEM = create("juniper_sign", new SignItem(gen().maxCount(16), JUNIPER_SIGN, JUNIPER_WALL_SIGN));
	//cypress
	public static final Block STRIPPED_CYPRESS_LOG = create("stripped_cypress_log", new PillarBlock(copyOf(JUNIPER_LOG)), true);
	public static final Block STRIPPED_CYPRESS_WOOD = create("stripped_cypress_wood", new PillarBlock(copyOf(STRIPPED_CYPRESS_LOG)), true);
	public static final Block CYPRESS_LOG = create("cypress_log", new StrippableLogBlock(() -> STRIPPED_CYPRESS_LOG, MapColor.BROWN, copyOf(STRIPPED_CYPRESS_LOG)), true);
	public static final Block CYPRESS_WOOD = create("cypress_wood", new StrippableLogBlock(() -> STRIPPED_CYPRESS_WOOD, MapColor.BROWN, copyOf(STRIPPED_CYPRESS_LOG)), true);
	public static final Block CYPRESS_LEAVES = create("cypress_leaves", BlocksAccessor.callCreateLeavesBlock(BlockSoundGroup.GRASS), true);
	public static final Block CYPRESS_SAPLING = create("cypress_sapling", new BWSaplingBlock(new CypressSaplingGenerator(), copyOf(JUNIPER_SAPLING)), true);
	public static final Block POTTED_CYPRESS_SAPLING = create("potted_cypress_sapling", new FlowerPotBlock(CYPRESS_SAPLING, copyOf(POTTED_JUNIPER_SAPLING)), false);
	public static final Block CYPRESS_PLANKS = create("cypress_planks", new Block(copyOf(JUNIPER_PLANKS)), true);
	public static final Block CYPRESS_STAIRS = create("cypress_stairs", new TerraformStairsBlock(CYPRESS_PLANKS, copyOf(JUNIPER_STAIRS)), true);
	public static final Block CYPRESS_SLAB = create("cypress_slab", new SlabBlock(copyOf(JUNIPER_SLAB)), true);
	public static final Block CYPRESS_FENCE = create("cypress_fence", new FenceBlock(copyOf(JUNIPER_FENCE)), true);
	public static final Block CYPRESS_FENCE_GATE = create("cypress_fence_gate", new FenceGateBlock(copyOf(JUNIPER_FENCE_GATE)), true);
	public static final Block CYPRESS_PRESSURE_PLATE = create("cypress_pressure_plate", new TerraformPressurePlateBlock(copyOf(JUNIPER_PRESSURE_PLATE)), true);
	public static final Block CYPRESS_BUTTON = create("cypress_button", new TerraformButtonBlock(copyOf(JUNIPER_BUTTON)), true);
	public static final Block CYPRESS_TRAPDOOR = create("cypress_trapdoor", new TerraformTrapdoorBlock(copyOf(JUNIPER_TRAPDOOR)), true);
	public static final Block CYPRESS_DOOR = create("cypress_door", new TerraformDoorBlock(copyOf(JUNIPER_DOOR)), false);
	public static final Item CYPRESS_DOOR_ITEM = create("cypress_door", new TallBlockItem(CYPRESS_DOOR, gen()));
	public static final Block CYPRESS_CHEST = create("cypress_chest", new BWChestBlock(copyOf(JUNIPER_CHEST), () -> BWBlockEntityTypes.BW_CHEST, false), true);
	public static final Block TRAPPED_CYPRESS_CHEST = create("trapped_cypress_chest", new BWChestBlock(copyOf(JUNIPER_CHEST), () -> BWBlockEntityTypes.BW_CHEST, true), true);
	private static final Identifier CYPRESS_SIGN_TEXTURE = new Identifier(Bewitchment.MODID, "entity/sign/cypress");
	public static final TerraformSignBlock CYPRESS_SIGN = create("cypress_sign", new TerraformSignBlock(CYPRESS_SIGN_TEXTURE, copyOf(JUNIPER_SIGN)), false);
	public static final Block CYPRESS_WALL_SIGN = create("cypress_wall_sign", new TerraformWallSignBlock(CYPRESS_SIGN_TEXTURE, copyOf(JUNIPER_WALL_SIGN)), false);
	public static final Item CYPRESS_SIGN_ITEM = create("cypress_sign", new SignItem(gen().maxCount(16), CYPRESS_SIGN, CYPRESS_WALL_SIGN));
	//elder
	public static final Block STRIPPED_ELDER_LOG = create("stripped_elder_log", new PillarBlock(copyOf(JUNIPER_LOG)), true);
	public static final Block STRIPPED_ELDER_WOOD = create("stripped_elder_wood", new PillarBlock(copyOf(STRIPPED_ELDER_LOG)), true);
	public static final Block ELDER_LOG = create("elder_log", new StrippableLogBlock(() -> STRIPPED_ELDER_LOG, MapColor.BROWN, copyOf(STRIPPED_ELDER_LOG)), true);
	public static final Block ELDER_WOOD = create("elder_wood", new StrippableLogBlock(() -> STRIPPED_ELDER_WOOD, MapColor.BROWN, copyOf(STRIPPED_ELDER_LOG)), true);
	public static final Block ELDER_LEAVES = create("elder_leaves", BlocksAccessor.callCreateLeavesBlock(BlockSoundGroup.GRASS), true);
	public static final Block ELDER_SAPLING = create("elder_sapling", new BWSaplingBlock(new ElderSaplingGenerator(), copyOf(JUNIPER_SAPLING)), true);
	public static final Block POTTED_ELDER_SAPLING = create("potted_elder_sapling", new FlowerPotBlock(ELDER_SAPLING, copyOf(POTTED_JUNIPER_SAPLING)), false);
	public static final Block ELDER_PLANKS = create("elder_planks", new Block(copyOf(JUNIPER_PLANKS)), true);
	public static final Block ELDER_STAIRS = create("elder_stairs", new TerraformStairsBlock(ELDER_PLANKS, copyOf(JUNIPER_STAIRS)), true);
	public static final Block ELDER_SLAB = create("elder_slab", new SlabBlock(copyOf(JUNIPER_SLAB)), true);
	public static final Block ELDER_FENCE = create("elder_fence", new FenceBlock(copyOf(JUNIPER_FENCE)), true);
	public static final Block ELDER_FENCE_GATE = create("elder_fence_gate", new ElderFenceGateBlock(copyOf(JUNIPER_FENCE_GATE)), true);
	public static final Block ELDER_PRESSURE_PLATE = create("elder_pressure_plate", new ElderPressurePlateBlock(copyOf(JUNIPER_PRESSURE_PLATE)), true);
	public static final Block ELDER_BUTTON = create("elder_button", new ElderButtonBlock(copyOf(JUNIPER_BUTTON)), true);
	public static final Block ELDER_TRAPDOOR = create("elder_trapdoor", new ElderTrapdoorBlock(copyOf(JUNIPER_TRAPDOOR)), true);
	public static final Block ELDER_DOOR = create("elder_door", new ElderDoorBlock(copyOf(JUNIPER_DOOR)), false);
	public static final Item ELDER_DOOR_ITEM = create("elder_door", new TallBlockItem(ELDER_DOOR, gen()));
	public static final Block ELDER_CHEST = create("elder_chest", new ElderChestBlock(copyOf(JUNIPER_CHEST), () -> BWBlockEntityTypes.ELDER_CHEST, false), true);
	public static final Block TRAPPED_ELDER_CHEST = create("trapped_elder_chest", new ElderChestBlock(copyOf(JUNIPER_CHEST), () -> BWBlockEntityTypes.ELDER_CHEST, true), true);
	private static final Identifier ELDER_SIGN_TEXTURE = new Identifier(Bewitchment.MODID, "entity/sign/elder");
	public static final TerraformSignBlock ELDER_SIGN = create("elder_sign", new TerraformSignBlock(ELDER_SIGN_TEXTURE, copyOf(JUNIPER_SIGN)), false);
	public static final Block ELDER_WALL_SIGN = create("elder_wall_sign", new TerraformWallSignBlock(ELDER_SIGN_TEXTURE, copyOf(JUNIPER_WALL_SIGN)), false);
	public static final Item ELDER_SIGN_ITEM = create("elder_sign", new SignItem(gen().maxCount(16), ELDER_SIGN, ELDER_WALL_SIGN));
	//dragons_blood
	public static final Block STRIPPED_DRAGONS_BLOOD_LOG = create("stripped_dragons_blood_log", new PillarBlock(copyOf(JUNIPER_LOG)), true);
	public static final Block STRIPPED_DRAGONS_BLOOD_WOOD = create("stripped_dragons_blood_wood", new PillarBlock(copyOf(STRIPPED_DRAGONS_BLOOD_LOG)), true);
	public static final Block DRAGONS_BLOOD_LOG = create("dragons_blood_log", new DragonsBloodLogBlock(() -> STRIPPED_DRAGONS_BLOOD_LOG, MapColor.BROWN, copyOf(STRIPPED_DRAGONS_BLOOD_LOG).ticksRandomly()), true);
	public static final Block DRAGONS_BLOOD_WOOD = create("dragons_blood_wood", new StrippableLogBlock(() -> STRIPPED_DRAGONS_BLOOD_WOOD, MapColor.BROWN, copyOf(STRIPPED_DRAGONS_BLOOD_LOG)), true);
	public static final Block DRAGONS_BLOOD_LEAVES = create("dragons_blood_leaves", BlocksAccessor.callCreateLeavesBlock(BlockSoundGroup.GRASS), true);
	public static final Block DRAGONS_BLOOD_SAPLING = create("dragons_blood_sapling", new BWSaplingBlock(new DragonsBloodSaplingGenerator(), copyOf(JUNIPER_SAPLING)), true);
	public static final Block POTTED_DRAGONS_BLOOD_SAPLING = create("potted_dragons_blood_sapling", new FlowerPotBlock(DRAGONS_BLOOD_SAPLING, copyOf(POTTED_JUNIPER_SAPLING)), false);
	public static final Block DRAGONS_BLOOD_PLANKS = create("dragons_blood_planks", new Block(copyOf(JUNIPER_PLANKS)), true);
	public static final Block DRAGONS_BLOOD_STAIRS = create("dragons_blood_stairs", new TerraformStairsBlock(DRAGONS_BLOOD_PLANKS, copyOf(JUNIPER_STAIRS)), true);
	public static final Block DRAGONS_BLOOD_SLAB = create("dragons_blood_slab", new SlabBlock(copyOf(JUNIPER_SLAB)), true);
	public static final Block DRAGONS_BLOOD_FENCE = create("dragons_blood_fence", new FenceBlock(copyOf(JUNIPER_FENCE)), true);
	public static final Block DRAGONS_BLOOD_FENCE_GATE = create("dragons_blood_fence_gate", new DragonsBloodFenceGateBlock(copyOf(JUNIPER_FENCE_GATE)), true);
	public static final Block DRAGONS_BLOOD_PRESSURE_PLATE = create("dragons_blood_pressure_plate", new DragonsBloodPressurePlateBlock(copyOf(JUNIPER_PRESSURE_PLATE)), true);
	public static final Block DRAGONS_BLOOD_BUTTON = create("dragons_blood_button", new DragonsBloodButtonBlock(copyOf(JUNIPER_BUTTON)), true);
	public static final Block DRAGONS_BLOOD_TRAPDOOR = create("dragons_blood_trapdoor", new DragonsBloodTrapdoorBlock(copyOf(JUNIPER_TRAPDOOR)), true);
	public static final Block DRAGONS_BLOOD_DOOR = create("dragons_blood_door", new DragonsBloodDoorBlock(copyOf(JUNIPER_DOOR)), false);
	public static final Block DRAGONS_BLOOD_CHEST = create("dragons_blood_chest", new DragonsBloodChestBlock(copyOf(JUNIPER_CHEST), () -> BWBlockEntityTypes.DRAGONS_BLOOD_CHEST, false), true);
	public static final Block TRAPPED_DRAGONS_BLOOD_CHEST = create("trapped_dragons_blood_chest", new DragonsBloodChestBlock(copyOf(JUNIPER_CHEST), () -> BWBlockEntityTypes.DRAGONS_BLOOD_CHEST, true), true);
	private static final Identifier DRAGONS_BLOOD_SIGN_TEXTURE = new Identifier(Bewitchment.MODID, "entity/sign/dragons_blood");
	public static final TerraformSignBlock DRAGONS_BLOOD_SIGN = create("dragons_blood_sign", new TerraformSignBlock(DRAGONS_BLOOD_SIGN_TEXTURE, copyOf(JUNIPER_SIGN)), false);
	public static final Block DRAGONS_BLOOD_WALL_SIGN = create("dragons_blood_wall_sign", new TerraformWallSignBlock(DRAGONS_BLOOD_SIGN_TEXTURE, copyOf(JUNIPER_WALL_SIGN)), false);
	public static final Item DRAGONS_BLOOD_DOOR_ITEM = create("dragons_blood_door", new TallBlockItem(DRAGONS_BLOOD_DOOR, gen()));
	public static final Item DRAGONS_BLOOD_SIGN_ITEM = create("dragons_blood_sign", new SignItem(gen().maxCount(16), DRAGONS_BLOOD_SIGN, DRAGONS_BLOOD_WALL_SIGN));
	//other_plants
	public static final Block GLOWING_BRAMBLE = create("glowing_bramble", new BrambleBlock(of(Material.PLANT).sounds(BlockSoundGroup.GRASS).strength(2, 3).noCollision().ticksRandomly().luminance(15)), true);
	public static final Block ENDER_BRAMBLE = create("ender_bramble", new BrambleBlock(of(Material.PLANT).sounds(BlockSoundGroup.GRASS).strength(2, 3).noCollision().ticksRandomly()), true);
	public static final Block FRUITING_BRAMBLE = create("fruiting_bramble", new BrambleBlock.Fruiting(copyOf(ENDER_BRAMBLE)), true);
	public static final Block SCORCHED_BRAMBLE = create("scorched_bramble", new BrambleBlock(copyOf(ENDER_BRAMBLE)), true);
	public static final Block THICK_BRAMBLE = create("thick_bramble", new BrambleBlock(copyOf(ENDER_BRAMBLE)), true);
	public static final Block FLEETING_BRAMBLE = create("fleeting_bramble", new BrambleBlock(copyOf(ENDER_BRAMBLE)), true);
	//material_block
	public static final Block RAW_SILVER_BLOCK = create("raw_silver_block", new Block(copyOf(Blocks.RAW_GOLD_BLOCK)) {
		@Override
		public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
			if (!world.isClient && entity instanceof LivingEntity && BewitchmentAPI.isWeakToSilver((LivingEntity) entity)) {
				entity.damage(BWDamageSources.MAGIC_COPY, 1);
			}
			super.onSteppedOn(world, pos, state, entity);
		}
	}, true);
	public static final Block SILVER_BLOCK = create("silver_block", new Block(copyOf(Blocks.GOLD_BLOCK)) {
		@Override
		public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
			RAW_SILVER_BLOCK.onSteppedOn(world, pos, state, entity);
		}
	}, true);
	public static final Block SALT_BLOCK = create("salt_block", new Block(copyOf(Blocks.COAL_BLOCK)), true);
	public static final Block DRAGONS_BLOOD_RESIN_BLOCK = create("dragons_blood_resin_block", new Block(copyOf(Blocks.NETHER_WART_BLOCK)), true);
	public static final Block SILVER_ORE = create("silver_ore", new Block(copyOf(Blocks.GOLD_ORE)), true);
	public static final Block DEEPSLATE_SILVER_ORE = create("deepslate_silver_ore", new Block(copyOf(Blocks.DEEPSLATE_GOLD_ORE)), true);
	public static final Block SALT_ORE = create("salt_ore", new OreBlock(copyOf(Blocks.COAL_ORE), UniformIntProvider.create(0, 2)), true);
	public static final Block DEEPSLATE_SALT_ORE = create("deepslate_salt_ore", new OreBlock(copyOf(Blocks.DEEPSLATE_COAL_ORE), UniformIntProvider.create(0, 2)), true);
	//misc_block
	public static final Block HEDGEWITCH_WOOL = create("hedgewitch_wool", new Block(copyOf(Blocks.WHITE_WOOL)), true);
	public static final Block ALCHEMIST_WOOL = create("alchemist_wool", new Block(copyOf(HEDGEWITCH_WOOL)), true);
	public static final Block BESMIRCHED_WOOL = create("besmirched_wool", new Block(copyOf(HEDGEWITCH_WOOL)), true);
	public static final Block HEDGEWITCH_CARPET = create("hedgewitch_carpet", new CarpetBlock(copyOf(Blocks.WHITE_CARPET)), true);
	public static final Block ALCHEMIST_CARPET = create("alchemist_carpet", new CarpetBlock(copyOf(HEDGEWITCH_CARPET)), true);
	public static final Block BESMIRCHED_CARPET = create("besmirched_carpet", new CarpetBlock(copyOf(HEDGEWITCH_CARPET)), true);
	public static final Block IRON_CANDELABRA = create("iron_candelabra", new CandelabraBlock(copyOf(Blocks.IRON_BLOCK).luminance(blockState -> blockState.get(Properties.LIT) ? 15 : 0), (byte) 16), true);
	public static final Block GOLDEN_CANDELABRA = create("golden_candelabra", new CandelabraBlock(copyOf(Blocks.GOLD_BLOCK).luminance(blockState -> blockState.get(Properties.LIT) ? 15 : 0), (byte) 32), true);
	public static final Block SILVER_CANDELABRA = create("silver_candelabra", new CandelabraBlock(copyOf(SILVER_BLOCK).luminance(blockState -> blockState.get(Properties.LIT) ? 15 : 0), (byte) 32), true);
	public static final Block NETHERITE_CANDELABRA = create("netherite_candelabra", new CandelabraBlock(copyOf(Blocks.NETHERITE_BLOCK).luminance(blockState -> blockState.get(Properties.LIT) ? 15 : 0), (byte) 64), false);
	public static final Item NETHERITE_CANDELABRA_ITEM = create("netherite_candelabra", new BlockItem(NETHERITE_CANDELABRA, gen().fireproof()));
	public static final Block BLESSED_STONE = create("blessed_stone", new Block(copyOf(Blocks.BEDROCK).dropsNothing()), true);
	//block_entity
	public static final Block[] STONE_WITCH_ALTAR = createAltar("stone_witch_altar", copyOf(Blocks.STONE));
	public static final Block[] MOSSY_COBBLESTONE_WITCH_ALTAR = createAltar("mossy_cobblestone_witch_altar", copyOf(Blocks.MOSSY_COBBLESTONE));
	public static final Block[] PRISMARINE_WITCH_ALTAR = createAltar("prismarine_witch_altar", copyOf(Blocks.PRISMARINE));
	public static final Block[] NETHER_BRICK_WITCH_ALTAR = createAltar("nether_brick_witch_altar", copyOf(Blocks.NETHER_BRICKS));
	public static final Block[] BLACKSTONE_WITCH_ALTAR = createAltar("blackstone_witch_altar", copyOf(Blocks.BLACKSTONE));
	public static final Block[] GOLDEN_WITCH_ALTAR = createAltar("golden_witch_altar", copyOf(Blocks.GOLD_BLOCK));
	public static final Block[] END_STONE_WITCH_ALTAR = createAltar("end_stone_witch_altar", copyOf(Blocks.END_STONE));
	public static final Block[] OBSIDIAN_WITCH_ALTAR = createAltar("obsidian_witch_altar", copyOf(Blocks.OBSIDIAN));
	public static final Block[] PURPUR_WITCH_ALTAR = createAltar("purpur_witch_altar", copyOf(Blocks.PURPUR_BLOCK));
	public static final Block WITCH_CAULDRON = create("witch_cauldron", new WitchCauldronBlock(copyOf(Blocks.CAULDRON)), true);
	public static final Block CRYSTAL_BALL = create("crystal_ball", new CrystalBallBlock(copyOf(SILVER_BLOCK).sounds(BlockSoundGroup.GLASS)), true);
	public static final Block BRAZIER = create("brazier", new BrazierBlock(copyOf(SILVER_BLOCK).luminance(blockState -> blockState.get(Properties.LIT) ? 15 : 0)), true);
	public static final Block OAK_POPPET_SHELF = create("oak_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.OAK_PLANKS).nonOpaque()), true);
	public static final Block SPRUCE_POPPET_SHELF = create("spruce_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.SPRUCE_PLANKS).nonOpaque()), true);
	public static final Block BIRCH_POPPET_SHELF = create("birch_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.BIRCH_PLANKS).nonOpaque()), true);
	public static final Block JUNGLE_POPPET_SHELF = create("jungle_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.JUNGLE_PLANKS).nonOpaque()), true);
	public static final Block ACACIA_POPPET_SHELF = create("acacia_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.ACACIA_PLANKS).nonOpaque()), true);
	public static final Block DARK_OAK_POPPET_SHELF = create("dark_oak_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.DARK_OAK_PLANKS).nonOpaque()), true);
	public static final Block CRIMSON_POPPET_SHELF = create("crimson_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.CRIMSON_PLANKS).nonOpaque()), true);
	public static final Block WARPED_POPPET_SHELF = create("warped_poppet_shelf", new PoppetShelfBlock(copyOf(Blocks.WARPED_PLANKS).nonOpaque()), true);
	public static final Block JUNIPER_POPPET_SHELF = create("juniper_poppet_shelf", new PoppetShelfBlock(copyOf(JUNIPER_PLANKS).nonOpaque()), true);
	public static final Block CYPRESS_POPPET_SHELF = create("cypress_poppet_shelf", new PoppetShelfBlock(copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block ELDER_POPPET_SHELF = create("elder_poppet_shelf", new PoppetShelfBlock(copyOf(ELDER_PLANKS).nonOpaque()), true);
	public static final Block DRAGONS_BLOOD_POPPET_SHELF = create("dragons_blood_poppet_shelf", new PoppetShelfBlock(copyOf(DRAGONS_BLOOD_PLANKS).nonOpaque()), true);
	public static final Block WHITE_COFFIN = create("white_coffin", new CoffinBlock(DyeColor.WHITE, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block ORANGE_COFFIN = create("orange_coffin", new CoffinBlock(DyeColor.ORANGE, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block MAGENTA_COFFIN = create("magenta_coffin", new CoffinBlock(DyeColor.MAGENTA, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block LIGHT_BLUE_COFFIN = create("light_blue_coffin", new CoffinBlock(DyeColor.LIGHT_BLUE, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block YELLOW_COFFIN = create("yellow_coffin", new CoffinBlock(DyeColor.YELLOW, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block LIME_COFFIN = create("lime_coffin", new CoffinBlock(DyeColor.LIME, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block PINK_COFFIN = create("pink_coffin", new CoffinBlock(DyeColor.PINK, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block GRAY_COFFIN = create("gray_coffin", new CoffinBlock(DyeColor.GRAY, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block LIGHT_GRAY_COFFIN = create("light_gray_coffin", new CoffinBlock(DyeColor.LIGHT_GRAY, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block CYAN_COFFIN = create("cyan_coffin", new CoffinBlock(DyeColor.CYAN, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block PURPLE_COFFIN = create("purple_coffin", new CoffinBlock(DyeColor.PURPLE, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block BLUE_COFFIN = create("blue_coffin", new CoffinBlock(DyeColor.BLUE, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block BROWN_COFFIN = create("brown_coffin", new CoffinBlock(DyeColor.BROWN, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block GREEN_COFFIN = create("green_coffin", new CoffinBlock(DyeColor.GREEN, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block RED_COFFIN = create("red_coffin", new CoffinBlock(DyeColor.RED, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	public static final Block BLACK_COFFIN = create("black_coffin", new CoffinBlock(DyeColor.BLACK, copyOf(CYPRESS_PLANKS).nonOpaque()), true);
	//armor
	public static final Item HEDGEWITCH_HOOD = create("hedgewitch_hood", new ArmorItem(BWMaterials.HEDGEWITCH_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item HEDGEWITCH_HAT = create("hedgewitch_hat", new ArmorItem(BWMaterials.HEDGEWITCH_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item HEDGEWITCH_ROBES = create("hedgewitch_robes", new ArmorItem(BWMaterials.HEDGEWITCH_ARMOR, EquipmentSlot.CHEST, gen()));
	public static final Item HEDGEWITCH_PANTS = create("hedgewitch_pants", new ArmorItem(BWMaterials.HEDGEWITCH_ARMOR, EquipmentSlot.LEGS, gen()));
	public static final Item ALCHEMIST_HOOD = create("alchemist_hood", new ArmorItem(BWMaterials.ALCHEMIST_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item ALCHEMIST_HAT = create("alchemist_hat", new ArmorItem(BWMaterials.ALCHEMIST_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item ALCHEMIST_ROBES = create("alchemist_robes", new ArmorItem(BWMaterials.ALCHEMIST_ARMOR, EquipmentSlot.CHEST, gen()));
	public static final Item ALCHEMIST_PANTS = create("alchemist_pants", new ArmorItem(BWMaterials.ALCHEMIST_ARMOR, EquipmentSlot.LEGS, gen()));
	public static final Item BESMIRCHED_HOOD = create("besmirched_hood", new ArmorItem(BWMaterials.BESMIRCHED_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item BESMIRCHED_HAT = create("besmirched_hat", new ArmorItem(BWMaterials.BESMIRCHED_ARMOR, EquipmentSlot.HEAD, gen()));
	public static final Item BESMIRCHED_ROBES = create("besmirched_robes", new ArmorItem(BWMaterials.BESMIRCHED_ARMOR, EquipmentSlot.CHEST, gen()));
	public static final Item BESMIRCHED_PANTS = create("besmirched_pants", new ArmorItem(BWMaterials.BESMIRCHED_ARMOR, EquipmentSlot.LEGS, gen()));
	public static final Item HARBINGER = create("harbinger", new ArmorItem(BWMaterials.HARBINGER_ARMOR, EquipmentSlot.FEET, gen().rarity(Rarity.RARE).maxDamage(444).fireproof()));
	//trinket
	public static final Item NAZAR = create("nazar", new TrinketItem(gen().maxCount(1)));
	public static final Item SPECTER_BANGLE = create("specter_bangle", new SpecterBangleItem(gen().maxCount(1)));
	public static final Item PRICKLY_BELT = create("prickly_belt", new PricklyBeltItem(gen().maxCount(1)));
	public static final Item HELLISH_BAUBLE = create("hellish_bauble", new HellishBaubleItem(gen().maxCount(1).fireproof()));
	public static final Item DRUID_BAND = create("druid_band", new DruidBandItem(gen().maxCount(1)));
	public static final Item ZEPHYR_HARNESS = create("zephyr_harness", new ZephyrHarnessItem(gen().maxCount(1)));
	//tool
	public static final Item ATHAME = create("athame", new AthameItem(BWMaterials.SILVER_TOOL, 1, -2, gen()));
	public static final Item SILVER_ARROW = create("silver_arrow", new SilverArrowItem(gen()));
	public static final Item SCEPTER = create("scepter", new ScepterItem(gen().rarity(Rarity.RARE).maxDamage(64).fireproof()));
	public static final Item CADUCEUS = create("caduceus", new CaduceusItem(0, -3, ToolMaterials.NETHERITE, BlockTags.PICKAXE_MINEABLE, gen().rarity(Rarity.RARE).maxDamage(1998).fireproof()));
	public static final Item HORNED_SPEAR = create("horned_spear", new HornedSpearItem(ToolMaterials.NETHERITE, 2, -2.4f, gen().rarity(Rarity.RARE).maxDamage(1008).fireproof()));
	public static final Item CHALK = create("chalk", new ChalkItem(gen().maxDamage(128), GLYPH));
	public static final Item GOLDEN_CHALK = create("golden_chalk", new ChalkItem(gen().maxDamage(128), GOLDEN_GLYPH));
	public static final Item FIERY_CHALK = create("fiery_chalk", new ChalkItem(gen().maxDamage(128), FIERY_GLYPH));
	public static final Item ELDRITCH_CHALK = create("eldritch_chalk", new ChalkItem(gen().maxDamage(128), ELDRITCH_GLYPH));
	public static final Item TAGLOCK = create("taglock", new TaglockItem(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item WAYSTONE = create("waystone", new WaystoneItem(gen().maxDamage(3)));
	public static final Item JUNIPER_BROOM = create("juniper_broom", new BroomItem(gen().maxCount(1), BWEntityTypes.JUNIPER_BROOM));
	public static final Item CYPRESS_BROOM = create("cypress_broom", new BroomItem(gen().maxCount(1), BWEntityTypes.CYPRESS_BROOM));
	public static final Item ELDER_BROOM = create("elder_broom", new BroomItem(gen().maxCount(1), BWEntityTypes.ELDER_BROOM));
	public static final Item DRAGONS_BLOOD_BROOM = create("dragons_blood_broom", new DragonsBloodBroomItem(gen().maxCount(1), BWEntityTypes.DRAGONS_BLOOD_BROOM));
	public static final Item MENDING_SIGIL = create("mending_sigil", new SigilItem(gen(), BWSigils.MENDING));
	public static final Item CLEANSING_SIGIL = create("cleansing_sigil", new SigilItem(gen(), BWSigils.CLEANSING));
	public static final Item JUDGMENT_SIGIL = create("judgment_sigil", new SigilItem(gen(), BWSigils.JUDGMENT));
	public static final Item DECAY_SIGIL = create("decay_sigil", new SigilItem(gen(), BWSigils.DECAY));
	public static final Item SHRIEKING_SIGIL = create("shrieking_sigil", new SigilItem(gen(), BWSigils.SHRIEKING));
	public static final Item SENTINEL_SIGIL = create("sentinel_sigil", new SigilItem(gen(), BWSigils.SENTINEL));
	public static final Item SLIPPERY_SIGIL = create("slippery_sigil", new SigilItem(gen(), BWSigils.SLIPPERY));
	public static final Item SHADOWS_SIGIL = create("shadows_sigil", new SigilItem(gen(), BWSigils.SHADOWS));
	public static final Item EXTENDING_SIGIL = create("extending_sigil", new SigilItem(gen(), BWSigils.EXTENDING));
	public static final Item SMELLY_SIGIL = create("smelly_sigil", new SigilItem(gen(), BWSigils.SMELLY));
	public static final Item RUIN_SIGIL = create("ruin_sigil", new SigilItem(gen(), BWSigils.RUIN));
	public static final Item HEAVY_SIGIL = create("heavy_sigil", new SigilItem(gen(), BWSigils.HEAVY));
	public static final PoppetItem PROTECTION_POPPET = create("protection_poppet", new PoppetItem(gen().maxDamage(128), true));
	public static final PoppetItem DEATH_PROTECTION_POPPET = create("death_protection_poppet", new PoppetItem(gen().maxDamage(1), false));
	public static final PoppetItem VOODOO_POPPET = create("voodoo_poppet", new PoppetItem(gen().maxDamage(128), false));
	public static final PoppetItem VOODOO_PROTECTION_POPPET = create("voodoo_protection_poppet", new PoppetItem(gen().maxDamage(128), true));
	public static final PoppetItem MENDING_POPPET = create("mending_poppet", new PoppetItem(gen().maxDamage(1), true));
	public static final PoppetItem CURSE_POPPET = create("curse_poppet", new CursePoppetItem(gen().maxDamage(1), true));
	public static final PoppetItem VAMPIRIC_POPPET = create("vampiric_poppet", new PoppetItem(gen().maxDamage(128), false));
	public static final PoppetItem JUDGMENT_POPPET = create("judgment_poppet", new PoppetItem(gen().maxDamage(64), true));
	public static final PoppetItem FATIGUE_POPPET = create("fatigue_poppet", new PoppetItem(gen().maxDamage(64), true));
	public static final Item DEMONIC_CONTRACT = create("demonic_contract", new ContractItem(gen().rarity(Rarity.RARE).maxCount(1)));
	//material_item
	public static final Item CLEANSING_BALM = create("cleansing_balm", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item GRIM_ELIXIR = create("grim_elixir", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item AQUA_CERATE = create("aqua_cerate", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item FIERY_SERUM = create("fiery_serum", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item EARTH_ICHOR = create("earth_ichor", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item HEAVEN_EXTRACT = create("heaven_extract", new Item(gen().recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item RAW_SILVER = create("raw_silver", new Item(gen()));
	public static final Item SILVER_INGOT = create("silver_ingot", new Item(gen()));
	public static final Item SILVER_NUGGET = create("silver_nugget", new Item(gen()));
	public static final Item SALT = create("salt", new AliasedBlockItem(SALT_LINE, gen()));
	public static final Item ACONITE = create("aconite", new Item(gen()));
	public static final Item ACONITE_SEEDS = create("aconite_seeds", new AliasedBlockItem(ACONITE_CROP, gen()));
	public static final Item BELLADONNA = create("belladonna", new Item(gen()));
	public static final Item BELLADONNA_SEEDS = create("belladonna_seeds", new AliasedBlockItem(BELLADONNA_CROP, gen()));
	public static final Item GARLIC = create("garlic", new AliasedBlockItem(GARLIC_CROP, gen().food(FoodComponents.POTATO)));
	public static final Item MANDRAKE_ROOT = create("mandrake_root", new Item(gen()));
	public static final Item MANDRAKE_SEEDS = create("mandrake_seeds", new AliasedBlockItem(MANDRAKE_CROP, gen()));
	public static final Item OAK_BARK = create("oak_bark", new Item(gen()));
	public static final Item SPRUCE_BARK = create("spruce_bark", new Item(gen()));
	public static final Item BIRCH_BARK = create("birch_bark", new Item(gen()));
	public static final Item JUNIPER_BARK = create("juniper_bark", new Item(gen()));
	public static final Item CYPRESS_BARK = create("cypress_bark", new Item(gen()));
	public static final Item ELDER_BARK = create("elder_bark", new Item(gen()));
	public static final Item WOOD_ASH = create("wood_ash", new Item(gen()));
	public static final Item DRAGONS_BLOOD_RESIN = create("dragons_blood_resin", new Item(gen()));
	public static final Item SNAKE_TONGUE = create("snake_tongue", new Item(gen()));
	public static final Item ECTOPLASM = create("ectoplasm", new Item(gen()));
	public static final Item DEMON_HORN = create("demon_horn", new Item(gen().fireproof()));
	public static final Item DEMON_HEART = create("demon_heart", new Item(gen().food(BWFoodComponents.DEMON_HEART).rarity(Rarity.UNCOMMON).fireproof()));
	public static final Item BOTTLE_OF_BLOOD = create("bottle_of_blood", new BottleOfBloodItem(gen().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));
	public static final Item GRILLED_GARLIC = create("grilled_garlic", new Item(gen().food(FoodComponents.CARROT)));
	public static final Item GARLIC_BREAD = create("garlic_bread", new Item(gen().food(BWFoodComponents.GARLIC_BREAD)));
	public static final Item WITCHBERRY = create("witchberry", new Item(gen().food(BWFoodComponents.WITCHBERRY)));
	public static final Item WITCHBERRY_PIE = create("witchberry_pie", new Item(gen().food(BWFoodComponents.WITCHBERRY_PIE)));
	public static final Item WITCHBERRY_COOKIE = create("witchberry_cookie", new Item(gen().food(BWFoodComponents.WITCHBERRY_COOKIE)));
	public static final Item GROTESQUE_STEW = create("grotesque_stew", new GrotestqueStewItem(gen().food(BWFoodComponents.DEMON_HEART).maxCount(1).recipeRemainder(Items.BOWL)));
	//spawn_egg
	public static final Item OWL_SPAWN_EGG = create("owl_spawn_egg", new SpawnEggItem(BWEntityTypes.OWL, 0x7f3f00, 0xc0c0c0, gen()));
	public static final Item RAVEN_SPAWN_EGG = create("raven_spawn_egg", new SpawnEggItem(BWEntityTypes.RAVEN, 0x3f3f3f, 0x000000, gen()));
	public static final Item SNAKE_SPAWN_EGG = create("snake_spawn_egg", new SpawnEggItem(BWEntityTypes.SNAKE, 0x7f3f00, 0x3f3f3f, gen()));
	public static final Item TOAD_SPAWN_EGG = create("toad_spawn_egg", new SpawnEggItem(BWEntityTypes.TOAD, 0x3f3f00, 0x00c200, gen()));
	public static final Item GHOST_SPAWN_EGG = create("ghost_spawn_egg", new SpawnEggItem(BWEntityTypes.GHOST, 0xcacaca, 0x969696, gen()));
	public static final Item VAMPIRE_SPAWN_EGG = create("vampire_spawn_egg", new SpawnEggItem(BWEntityTypes.VAMPIRE, 0xddcca8, 0x7d0000, gen()));
	public static final Item WEREWOLF_SPAWN_EGG = create("werewolf_spawn_egg", new SpawnEggItem(BWEntityTypes.WEREWOLF, 0x9f9a96, 0x532f0f, gen()));
	public static final Item HELLHOUND_SPAWN_EGG = create("hellhound_spawn_egg", new SpawnEggItem(BWEntityTypes.HELLHOUND, 0xc82000, 0x802020, gen()));
	public static final Item DEMON_SPAWN_EGG = create("demon_spawn_egg", new SpawnEggItem(BWEntityTypes.DEMON, 0x802020, 0xc82000, gen()));
	public static final Item LEONARD_SPAWN_EGG = create("leonard_spawn_egg", new SpawnEggItem(BWEntityTypes.LEONARD, 0x5e3214, 0xa00303, gen()));
	public static final Item BAPHOMET_SPAWN_EGG = create("baphomet_spawn_egg", new SpawnEggItem(BWEntityTypes.BAPHOMET, 0xa00303, 0x5e3214, gen()));
	public static final Item LILITH_SPAWN_EGG = create("lilith_spawn_egg", new SpawnEggItem(BWEntityTypes.LILITH, 0x222621, 0xc9cbcd, gen()));
	public static final Item HERNE_SPAWN_EGG = create("herne_spawn_egg", new SpawnEggItem(BWEntityTypes.HERNE, 0x5d482d, 0x294e00, gen()));
	//books
	public static final Item BOOK_OF_SHADOWS = create("book_of_shadows", new BWBookItem(gen().maxCount(1)));

	private static <T extends Block> T create(String name, T block, boolean createItem) {
		BLOCKS.put(block, new Identifier(Bewitchment.MODID, name));
		if (createItem) {
			ITEMS.put(new BlockItem(block, gen()), BLOCKS.get(block));
		}
		return block;
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Bewitchment.MODID, name));
		return item;
	}

	private static Item.Settings gen() {
		return new Item.Settings().group(Bewitchment.BEWITCHMENT_GROUP);
	}

	private static Block[] createAltar(String name, FabricBlockSettings settings) {
		settings = settings.luminance(blockState -> blockState.get(Properties.LEVEL_15));
		Block[] altars = new Block[21];
		WitchAltarBlock unformed = create(name, new WitchAltarBlock(settings, null, false), true);
		altars[0] = unformed;
		altars[1] = create("moss_" + name, new WitchAltarBlock(settings, unformed, true), false);
		for (int i = 0; i < DyeColor.values().length; i++) {
			altars[i + 2] = create(DyeColor.byId(i).getName() + "_" + name, new WitchAltarBlock(settings, unformed, true), false);
		}
		altars[18] = create("hedgewitch_" + name, new WitchAltarBlock(settings, unformed, true), false);
		altars[19] = create("alchemist_" + name, new WitchAltarBlock(settings, unformed, true), false);
		altars[20] = create("besmirched_" + name, new WitchAltarBlock(settings, unformed, true), false);
		return altars;
	}

	public static void init() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
		FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
		fuelRegistry.add(JUNIPER_FENCE, 300);
		fuelRegistry.add(JUNIPER_FENCE_GATE, 300);
		fuelRegistry.add(CYPRESS_FENCE, 300);
		fuelRegistry.add(CYPRESS_FENCE_GATE, 300);
		fuelRegistry.add(ELDER_FENCE, 300);
		fuelRegistry.add(ELDER_FENCE_GATE, 300);
		fuelRegistry.add(DRAGONS_BLOOD_FENCE, 300);
		fuelRegistry.add(DRAGONS_BLOOD_FENCE_GATE, 300);
		fuelRegistry.add(BWTags.BARKS, 100);
		fuelRegistry.add(SCORCHED_BRAMBLE, 800);
		FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();
		flammableRegistry.add(STRIPPED_JUNIPER_LOG, 5, 5);
		flammableRegistry.add(STRIPPED_JUNIPER_WOOD, 5, 5);
		flammableRegistry.add(JUNIPER_LOG, 5, 5);
		flammableRegistry.add(JUNIPER_WOOD, 5, 5);
		flammableRegistry.add(JUNIPER_LEAVES, 30, 60);
		flammableRegistry.add(JUNIPER_PLANKS, 5, 20);
		flammableRegistry.add(JUNIPER_STAIRS, 5, 20);
		flammableRegistry.add(JUNIPER_SLAB, 5, 20);
		flammableRegistry.add(JUNIPER_FENCE, 5, 20);
		flammableRegistry.add(JUNIPER_FENCE_GATE, 5, 20);
		flammableRegistry.add(STRIPPED_CYPRESS_LOG, 5, 5);
		flammableRegistry.add(STRIPPED_CYPRESS_WOOD, 5, 5);
		flammableRegistry.add(CYPRESS_LOG, 5, 5);
		flammableRegistry.add(CYPRESS_WOOD, 5, 5);
		flammableRegistry.add(CYPRESS_LEAVES, 30, 60);
		flammableRegistry.add(CYPRESS_PLANKS, 5, 20);
		flammableRegistry.add(CYPRESS_STAIRS, 5, 20);
		flammableRegistry.add(CYPRESS_SLAB, 5, 20);
		flammableRegistry.add(CYPRESS_FENCE, 5, 20);
		flammableRegistry.add(CYPRESS_FENCE_GATE, 5, 20);
		flammableRegistry.add(STRIPPED_ELDER_LOG, 5, 5);
		flammableRegistry.add(STRIPPED_ELDER_WOOD, 5, 5);
		flammableRegistry.add(ELDER_LOG, 5, 5);
		flammableRegistry.add(ELDER_WOOD, 5, 5);
		flammableRegistry.add(ELDER_LEAVES, 30, 60);
		flammableRegistry.add(ELDER_PLANKS, 5, 20);
		flammableRegistry.add(ELDER_STAIRS, 5, 20);
		flammableRegistry.add(ELDER_SLAB, 5, 20);
		flammableRegistry.add(ELDER_FENCE, 5, 20);
		flammableRegistry.add(ELDER_FENCE_GATE, 5, 20);
		flammableRegistry.add(STRIPPED_DRAGONS_BLOOD_LOG, 5, 5);
		flammableRegistry.add(STRIPPED_DRAGONS_BLOOD_WOOD, 5, 5);
		flammableRegistry.add(DRAGONS_BLOOD_LOG, 5, 5);
		flammableRegistry.add(DRAGONS_BLOOD_WOOD, 5, 5);
		flammableRegistry.add(DRAGONS_BLOOD_LEAVES, 30, 60);
		flammableRegistry.add(DRAGONS_BLOOD_PLANKS, 5, 20);
		flammableRegistry.add(DRAGONS_BLOOD_STAIRS, 5, 20);
		flammableRegistry.add(DRAGONS_BLOOD_SLAB, 5, 20);
		flammableRegistry.add(DRAGONS_BLOOD_FENCE, 5, 20);
		flammableRegistry.add(DRAGONS_BLOOD_FENCE_GATE, 5, 20);
		flammableRegistry.add(HEDGEWITCH_WOOL, 30, 60);
		flammableRegistry.add(ALCHEMIST_WOOL, 30, 60);
		flammableRegistry.add(BESMIRCHED_WOOL, 30, 60);
		flammableRegistry.add(HEDGEWITCH_CARPET, 60, 20);
		flammableRegistry.add(ALCHEMIST_CARPET, 60, 20);
		flammableRegistry.add(BESMIRCHED_CARPET, 60, 20);
		CompostingChanceRegistry compostRegistry = CompostingChanceRegistry.INSTANCE;
		compostRegistry.add(JUNIPER_LEAVES, 0.3f);
		compostRegistry.add(JUNIPER_SAPLING, 0.3f);
		compostRegistry.add(CYPRESS_LEAVES, 0.3f);
		compostRegistry.add(CYPRESS_SAPLING, 0.3f);
		compostRegistry.add(ELDER_LEAVES, 0.3f);
		compostRegistry.add(ELDER_SAPLING, 0.3f);
		compostRegistry.add(DRAGONS_BLOOD_LEAVES, 0.3f);
		compostRegistry.add(DRAGONS_BLOOD_SAPLING, 0.3f);
		compostRegistry.add(ACONITE, 0.65f);
		compostRegistry.add(ACONITE_SEEDS, 0.3f);
		compostRegistry.add(BELLADONNA, 0.65f);
		compostRegistry.add(BELLADONNA_SEEDS, 0.3f);
		compostRegistry.add(GARLIC, 0.65f);
		compostRegistry.add(MANDRAKE_ROOT, 0.65f);
		compostRegistry.add(MANDRAKE_SEEDS, 0.3f);
		compostRegistry.add(WOOD_ASH, 0.3f);
		compostRegistry.add(DRAGONS_BLOOD_RESIN, 0.65f);
		compostRegistry.add(GRILLED_GARLIC, 0.85f);
		compostRegistry.add(GARLIC_BREAD, 1f);
		compostRegistry.add(WITCHBERRY, 0.65f);
		compostRegistry.add(WITCHBERRY_PIE, 1f);
		compostRegistry.add(WITCHBERRY_COOKIE, 0.85f);
	}
}
