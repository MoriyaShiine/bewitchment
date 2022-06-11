/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.mixin.OrePlacedFeaturesAccessor;
import moriyashiine.bewitchment.mixin.SimpleBlockStateProviderAccessor;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;
import java.util.function.Predicate;

public class BWWorldGenerators {
	private static final FeatureSize EMPTY_SIZE = new TwoLayersFeatureSize(0, 0, 0);

	public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> JUNIPER_TREE = ConfiguredFeatures.register("bewitchment:juniper_tree", Feature.TREE, new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(BWObjects.JUNIPER_LOG.getDefaultState()), new ForkingTrunkPlacer(5, 0, 0), SimpleBlockStateProviderAccessor.callInit(BWObjects.JUNIPER_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)), EMPTY_SIZE).ignoreVines().build());
	public static final RegistryEntry<PlacedFeature> JUNIPER_TREE_WITH_CHANCE = PlacedFeatures.register("bewitchment:juniper_tree_with_chance", JUNIPER_TREE, VegetationPlacedFeatures.modifiersWithWouldSurvive(RarityFilterPlacementModifier.of(10), BWObjects.JUNIPER_SAPLING));
	public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> CYPRESS_TREE = ConfiguredFeatures.register("bewitchment:cypress_tree", Feature.TREE, new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(BWObjects.CYPRESS_LOG.getDefaultState()), new StraightTrunkPlacer(6, 1, 1), SimpleBlockStateProviderAccessor.callInit(BWObjects.CYPRESS_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4), EMPTY_SIZE).ignoreVines().build());
	public static final RegistryEntry<PlacedFeature> CYPRESS_TREE_WITH_CHANCE = PlacedFeatures.register("bewitchment:cypress_tree_with_chance", CYPRESS_TREE, VegetationPlacedFeatures.modifiersWithWouldSurvive(RarityFilterPlacementModifier.of(10), BWObjects.CYPRESS_SAPLING));
	public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> ELDER_TREE = ConfiguredFeatures.register("bewitchment:elder_tree", Feature.TREE, new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(BWObjects.ELDER_LOG.getDefaultState()), new StraightTrunkPlacer(4, 0, 1), SimpleBlockStateProviderAccessor.callInit(BWObjects.ELDER_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4), EMPTY_SIZE).ignoreVines().build());
	public static final RegistryEntry<PlacedFeature> ELDER_TREE_WITH_CHANCE = PlacedFeatures.register("bewitchment:elder_tree_with_chance", ELDER_TREE, VegetationPlacedFeatures.modifiersWithWouldSurvive(RarityFilterPlacementModifier.of(10), BWObjects.ELDER_SAPLING));
	public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> DRAGONS_BLOOD_TREE = ConfiguredFeatures.register("bewitchment:dragons_blood_tree", Feature.TREE, new TreeFeatureConfig.Builder(SimpleBlockStateProviderAccessor.callInit(BWObjects.DRAGONS_BLOOD_LOG.getDefaultState().with(BWProperties.NATURAL, true)), new StraightTrunkPlacer(5, 1, 1), SimpleBlockStateProviderAccessor.callInit(BWObjects.DRAGONS_BLOOD_LEAVES.getDefaultState()), new MegaPineFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(0), ConstantIntProvider.create(3)), EMPTY_SIZE).ignoreVines().build());

	public static final List<OreFeatureConfig.Target> SILVER_ORES = List.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, BWObjects.SILVER_ORE.getDefaultState()), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BWObjects.DEEPSLATE_SILVER_ORE.getDefaultState()));
	public static final List<OreFeatureConfig.Target> SALT_ORES = List.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, BWObjects.SALT_ORE.getDefaultState()), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BWObjects.DEEPSLATE_SALT_ORE.getDefaultState()));

	public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> SILVER_ORE = ConfiguredFeatures.register("bewitchment:silver_ore", Feature.ORE, new OreFeatureConfig(SILVER_ORES, 10));
	public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> SILVER_ORE_BURIED = ConfiguredFeatures.register("bewitchment:silver_ore_buried", Feature.ORE, new OreFeatureConfig(SILVER_ORES, 10, 0.5f));
	public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> SALT_ORE = ConfiguredFeatures.register("bewitchment:salt_ore", Feature.ORE, new OreFeatureConfig(SALT_ORES, 15));
	public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> SALT_ORE_BURIED = ConfiguredFeatures.register("bewitchment:salt_ore_buried", Feature.ORE, new OreFeatureConfig(SALT_ORES, 15, 0.5f));

	public static final RegistryEntry<PlacedFeature> SILVER_ORE_UPPER = PlacedFeatures.register("bewitchment:silver_ore_buried", SILVER_ORE_BURIED, OrePlacedFeaturesAccessor.callModifiersWithCount(4, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(32))));
	public static final RegistryEntry<PlacedFeature> SILVER_ORE_LOWER = PlacedFeatures.register("bewitchment:silver_ore_lower", SILVER_ORE_BURIED, OrePlacedFeaturesAccessor.callModifiers(CountPlacementModifier.of(UniformIntProvider.create(0, 1)), HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(-48))));

	public static final RegistryEntry<PlacedFeature> SALT_ORE_UPPER = PlacedFeatures.register("bewitchment:salt_ore_upper", SALT_ORE, OrePlacedFeaturesAccessor.callModifiersWithCount(30, HeightRangePlacementModifier.uniform(YOffset.fixed(136), YOffset.getTop())));
	public static final RegistryEntry<PlacedFeature> SALT_ORE_LOWER = PlacedFeatures.register("bewitchment:salt_ore_lower", SALT_ORE_BURIED, OrePlacedFeaturesAccessor.callModifiersWithCount(20, HeightRangePlacementModifier.trapezoid(YOffset.fixed(0), YOffset.fixed(192))));

	public static void init() {
		BiomeModification worldGen = BiomeModifications.create(new Identifier(Bewitchment.MODID, "world_features"));
		worldGen.add(ModificationPhase.ADDITIONS, BiomeSelectors.categories(Biome.Category.SAVANNA), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, JUNIPER_TREE_WITH_CHANCE.value()));
		worldGen.add(ModificationPhase.ADDITIONS, BiomeSelectors.categories(Biome.Category.TAIGA, Biome.Category.SWAMP), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, CYPRESS_TREE_WITH_CHANCE.value()));
		worldGen.add(ModificationPhase.ADDITIONS, BiomeSelectors.categories(Biome.Category.FOREST), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, ELDER_TREE_WITH_CHANCE.value()));
		worldGen.add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), context -> {
			if (Bewitchment.config.generateSilver) {
				context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_UPPER.value());
				context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_LOWER.value());
			}
			if (Bewitchment.config.generateSalt) {
				context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, SALT_ORE_UPPER.value());
				context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, SALT_ORE_LOWER.value());
			}
		});
		if (registerEntitySpawn(BWEntityTypes.OWL, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.owlBiomeCategories.contains(Biome.getCategory(context.getBiomeRegistryEntry()).getName())), Bewitchment.config.owlWeight, Bewitchment.config.owlMinGroupCount, Bewitchment.config.owlMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.OWL, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.RAVEN, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.ravenBiomeCategories.contains(Biome.getCategory(context.getBiomeRegistryEntry()).getName())), Bewitchment.config.ravenWeight, Bewitchment.config.ravenMinGroupCount, Bewitchment.config.ravenMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.RAVEN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.SNAKE, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.snakeBiomeCategories.contains(Biome.getCategory(context.getBiomeRegistryEntry()).getName())), Bewitchment.config.snakeWeight, Bewitchment.config.snakeMinGroupCount, Bewitchment.config.snakeMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.SNAKE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.TOAD, BiomeSelectors.foundInOverworld().and(context -> Bewitchment.config.toadBiomeCategories.contains(Biome.getCategory(context.getBiomeRegistryEntry()).getName())), Bewitchment.config.toadWeight, Bewitchment.config.toadMinGroupCount, Bewitchment.config.toadMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.TOAD, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.GHOST, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(BWEntityTypes.GHOST.getSpawnGroup()).isEmpty()), Bewitchment.config.ghostWeight, Bewitchment.config.ghostMinGroupCount, Bewitchment.config.ghostMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.GHOST, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhostEntity::canSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.VAMPIRE, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(BWEntityTypes.VAMPIRE.getSpawnGroup()).isEmpty()).and(context -> Biome.getCategory(context.getBiomeRegistryEntry()) == Biome.Category.TAIGA || Biome.getCategory(context.getBiomeRegistryEntry()) == Biome.Category.PLAINS), Bewitchment.config.vampireWeight, Bewitchment.config.vampireMinGroupCount, Bewitchment.config.vampireMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.VAMPIRE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, VampireEntity::canSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.WEREWOLF, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getSpawnSettings().getSpawnEntries(BWEntityTypes.WEREWOLF.getSpawnGroup()).isEmpty()).and(context -> Biome.getCategory(context.getBiomeRegistryEntry()) == Biome.Category.TAIGA || Biome.getCategory(context.getBiomeRegistryEntry()) == Biome.Category.FOREST), Bewitchment.config.werewolfWeight, Bewitchment.config.werewolfMinGroupCount, Bewitchment.config.werewolfMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.WEREWOLF, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WerewolfEntity::canSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.HELLHOUND, BiomeSelectors.foundInTheNether(), Bewitchment.config.hellhoundWeight, Bewitchment.config.hellhoundMinGroupCount, Bewitchment.config.hellhoundMaxGroupCount)) {
			SpawnRestrictionAccessor.callRegister(BWEntityTypes.HELLHOUND, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HellhoundEntity::canSpawn);
		}
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
			Identifier seeds = new Identifier(Bewitchment.MODID, "inject/seeds");
			Identifier nether_fortress = new Identifier(Bewitchment.MODID, "inject/nether_fortress");
			if (Blocks.GRASS.getLootTableId().equals(identifier) || Blocks.FERN.getLootTableId().equals(identifier) || Blocks.TALL_GRASS.getLootTableId().equals(identifier) || Blocks.LARGE_FERN.getLootTableId().equals(identifier)) {
				fabricLootSupplierBuilder.withPool(LootPool.builder().with(LootTableEntry.builder(seeds).weight(1)).build());
			}
			if (LootTables.NETHER_BRIDGE_CHEST.equals(identifier)) {
				fabricLootSupplierBuilder.withPool(LootPool.builder().with(LootTableEntry.builder(nether_fortress).weight(1)).build());
			}
		});
	}

	private static boolean registerEntitySpawn(EntityType<?> type, Predicate<BiomeSelectionContext> predicate, int weight, int minGroupSize, int maxGroupSize) {
		if (weight < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": weight " + weight + " cannot be negative.");
		} else if (minGroupSize < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": minGroupSize " + minGroupSize + " cannot be negative.");
		} else if (maxGroupSize < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": maxGroupSize " + maxGroupSize + " cannot be negative.");
		} else if (minGroupSize > maxGroupSize) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getTranslationKey() + ": minGroupSize " + minGroupSize + " cannot be greater than maxGroupSize " + maxGroupSize + ".");
		} else if (weight == 0 || minGroupSize == 0) {
			return false;
		}
		BiomeModifications.addSpawn(predicate, type.getSpawnGroup(), type, weight, minGroupSize, maxGroupSize);
		return true;
	}
}
