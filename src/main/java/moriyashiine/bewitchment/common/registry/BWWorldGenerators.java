package moriyashiine.bewitchment.common.registry;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.function.Predicate;

public class BWWorldGenerators {
	private static final FeatureSize EMPTY_SIZE = new TwoLayersFeatureSize(0, 0, 0);
	
	public static final ConfiguredFeature<?, ?> JUNIPER_TREE = create("juniper_tree", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.JUNIPER_LOG.getDefaultState()), new ForkingTrunkPlacer(5, 0, 0), new SimpleBlockStateProvider(BWObjects.JUNIPER_LEAVES.getDefaultState()), new SimpleBlockStateProvider(BWObjects.JUNIPER_SAPLING.getDefaultState()), new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)), EMPTY_SIZE).ignoreVines().build()).applyChance(5), GenerationStep.Feature.VEGETAL_DECORATION, BiomeSelectors.foundInOverworld().and(context -> context.getBiome().getCategory() == Biome.Category.SAVANNA));
	public static final ConfiguredFeature<?, ?> CYPRESS_TREE = create("cypress_tree", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.CYPRESS_LOG.getDefaultState()), new StraightTrunkPlacer(6, 1, 1), new SimpleBlockStateProvider(BWObjects.CYPRESS_LEAVES.getDefaultState()), new SimpleBlockStateProvider(BWObjects.CYPRESS_SAPLING.getDefaultState()), new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4), EMPTY_SIZE).ignoreVines().build()).applyChance(5), GenerationStep.Feature.VEGETAL_DECORATION, BiomeSelectors.foundInOverworld().and(context -> context.getBiome().getCategory() == Biome.Category.TAIGA || context.getBiome().getCategory() == Biome.Category.SWAMP));
	public static final ConfiguredFeature<?, ?> ELDER_TREE = create("elder_tree", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.ELDER_LOG.getDefaultState()), new StraightTrunkPlacer(4, 0, 1), new SimpleBlockStateProvider(BWObjects.ELDER_LEAVES.getDefaultState()), new SimpleBlockStateProvider(BWObjects.ELDER_SAPLING.getDefaultState()), new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4), EMPTY_SIZE).ignoreVines().build()).applyChance(5), GenerationStep.Feature.VEGETAL_DECORATION, BiomeSelectors.foundInOverworld().and(context -> context.getBiome().getCategory() == Biome.Category.FOREST));
	public static final ConfiguredFeature<?, ?> DRAGONS_BLOOD_TREE = Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.DRAGONS_BLOOD_LOG.getDefaultState().with(BWProperties.NATURAL, true)), new StraightTrunkPlacer(5, 1, 1), new SimpleBlockStateProvider(BWObjects.DRAGONS_BLOOD_LEAVES.getDefaultState()), new SimpleBlockStateProvider(BWObjects.DRAGONS_BLOOD_SAPLING.getDefaultState()), new MegaPineFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(0), ConstantIntProvider.create(3)), EMPTY_SIZE).ignoreVines().build()).applyChance(5);
	
	public static final ConfiguredFeature<?, ?> SILVER_ORE = create("silver_ore", Feature.ORE.configure(new OreFeatureConfig(ImmutableList.of(OreFeatureConfig.createTarget(OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES, BWObjects.SILVER_ORE.getDefaultState()), OreFeatureConfig.createTarget(OreFeatureConfig.Rules.DEEPSLATE_ORE_REPLACEABLES, BWObjects.DEEPSLATE_SILVER_ORE.getDefaultState())), Bewitchment.config.silverSize)).uniformRange(YOffset.getBottom(), YOffset.fixed(Bewitchment.config.silverMaxHeight)).spreadHorizontally().repeat(Bewitchment.config.silverCount), GenerationStep.Feature.UNDERGROUND_ORES, BiomeSelectors.foundInOverworld());
	public static final ConfiguredFeature<?, ?> SALT_ORE = create("salt_ore", Feature.ORE.configure(new OreFeatureConfig(ImmutableList.of(OreFeatureConfig.createTarget(OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES, BWObjects.SALT_ORE.getDefaultState()), OreFeatureConfig.createTarget(OreFeatureConfig.Rules.DEEPSLATE_ORE_REPLACEABLES, BWObjects.DEEPSLATE_SALT_ORE.getDefaultState())), Bewitchment.config.saltSize)).uniformRange(YOffset.getBottom(), YOffset.fixed(Bewitchment.config.saltMaxHeight)).spreadHorizontally().repeat(Bewitchment.config.saltCount), GenerationStep.Feature.UNDERGROUND_ORES, BiomeSelectors.foundInOverworld());
	
	private static ConfiguredFeature<?, ?> create(String name, ConfiguredFeature<?, ?> feature, GenerationStep.Feature step, Predicate<BiomeSelectionContext> predicate) {
		RegistryKey<ConfiguredFeature<?, ?>> key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Bewitchment.MODID, name));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.getValue(), feature);
		BiomeModifications.addFeature(predicate, step, key);
		return feature;
	}
	
	private static void registerEntitySpawn(EntityType<?> type, Predicate<BiomeSelectionContext> predicate, int weight, int minGroupSize, int maxGroupSize) {
		BiomeModifications.addSpawn(predicate, type.getSpawnGroup(), type, weight, minGroupSize, maxGroupSize);
	}
	
	public static void init() {
		if (Bewitchment.config.owlWeight > 0) {
			registerEntitySpawn(BWEntityTypes.OWL, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.owlBiomeCategories.contains(context.getBiome().getCategory().getName())), Bewitchment.config.owlWeight, Bewitchment.config.owlMinGroupCount, Bewitchment.config.owlMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.OWL, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
		}
		if (Bewitchment.config.ravenWeight > 0) {
			registerEntitySpawn(BWEntityTypes.RAVEN, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.ravenBiomeCategories.contains(context.getBiome().getCategory().getName())), Bewitchment.config.ravenWeight, Bewitchment.config.ravenMinGroupCount, Bewitchment.config.ravenMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.RAVEN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
		}
		if (Bewitchment.config.snakeWeight > 0) {
			registerEntitySpawn(BWEntityTypes.SNAKE, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.snakeBiomeCategories.contains(context.getBiome().getCategory().getName())), Bewitchment.config.snakeWeight, Bewitchment.config.snakeMinGroupCount, Bewitchment.config.snakeMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.SNAKE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
		}
		if (Bewitchment.config.toadWeight > 0) {
			registerEntitySpawn(BWEntityTypes.TOAD, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.toadBiomeCategories.contains(context.getBiome().getCategory().getName())), Bewitchment.config.toadWeight, Bewitchment.config.toadMinGroupCount, Bewitchment.config.toadMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.TOAD, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
		}
		if (Bewitchment.config.ghostWeight > 0) {
			registerEntitySpawn(BWEntityTypes.GHOST, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(BWEntityTypes.GHOST.getSpawnGroup()).isEmpty()), Bewitchment.config.ghostWeight, Bewitchment.config.ghostMinGroupCount, Bewitchment.config.ghostMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.GHOST, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhostEntity::canSpawn);
		}
		if (Bewitchment.config.vampireWeight > 0) {
			registerEntitySpawn(BWEntityTypes.VAMPIRE, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(BWEntityTypes.VAMPIRE.getSpawnGroup()).isEmpty()).and(context -> context.getBiome().getCategory() == Biome.Category.TAIGA || context.getBiome().getCategory() == Biome.Category.PLAINS), Bewitchment.config.vampireWeight, Bewitchment.config.vampireMinGroupCount, Bewitchment.config.vampireMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.VAMPIRE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, serverWorldAccess, spawnReason, pos, random) -> MobEntity.canMobSpawn(type, serverWorldAccess, spawnReason, pos, random) && BewitchmentAPI.getMoonPhase(serverWorldAccess) == 4);
		}
		if (Bewitchment.config.werewolfWeight > 0) {
			registerEntitySpawn(BWEntityTypes.WEREWOLF, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(BWEntityTypes.WEREWOLF.getSpawnGroup()).isEmpty()).and(context -> context.getBiome().getCategory() == Biome.Category.TAIGA || context.getBiome().getCategory() == Biome.Category.FOREST), Bewitchment.config.werewolfWeight, Bewitchment.config.werewolfMinGroupCount, Bewitchment.config.werewolfMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.WEREWOLF, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, serverWorldAccess, spawnReason, pos, random) -> MobEntity.canMobSpawn(type, serverWorldAccess, spawnReason, pos, random) && BewitchmentAPI.getMoonPhase(serverWorldAccess) == 0);
		}
		if (Bewitchment.config.hellhoundWeight > 0) {
			registerEntitySpawn(BWEntityTypes.HELLHOUND, BiomeSelectors.foundInTheNether(), Bewitchment.config.hellhoundWeight, Bewitchment.config.hellhoundMinGroupCount, Bewitchment.config.hellhoundMaxGroupCount);
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.HELLHOUND, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, serverWorldAccess, spawnReason, pos, random) -> !BlockTags.WART_BLOCKS.contains(serverWorldAccess.getBlockState(pos.down()).getBlock()));
		}
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
			Identifier seeds = new Identifier(Bewitchment.MODID, "inject/seeds");
			Identifier nether_fortress = new Identifier(Bewitchment.MODID, "inject/nether_fortress");
			if (Blocks.GRASS.getLootTableId().equals(identifier) || Blocks.TALL_GRASS.getLootTableId().equals(identifier) || Blocks.FERN.getLootTableId().equals(identifier) || Blocks.LARGE_FERN.getLootTableId().equals(identifier)) {
				fabricLootSupplierBuilder.withPool(LootPool.builder().with(LootTableEntry.builder(seeds).weight(1)).build());
			}
			if (LootTables.NETHER_BRIDGE_CHEST.equals(identifier)) {
				fabricLootSupplierBuilder.withPool(LootPool.builder().with(LootTableEntry.builder(nether_fortress).weight(1)).build());
			}
		});
	}
}
