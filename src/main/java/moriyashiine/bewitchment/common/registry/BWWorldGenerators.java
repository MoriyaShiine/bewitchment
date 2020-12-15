package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.impl.biome.modification.BiomeModificationImpl;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.HashMap;
import java.util.Map;

public class BWWorldGenerators {
	private static final Map<ConfiguredFeature<?, ?>, Identifier> CONFIGURED_FEATURES = new HashMap<>();
	
	private static final FeatureSize EMPTY_SIZE = new TwoLayersFeatureSize(0, 0, 0);
	
	public static final ConfiguredFeature<TreeFeatureConfig, ?> TREE_JUNIPER = create("tree_juniper", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.JUNIPER_LOG.getDefaultState()), new SimpleBlockStateProvider(BWObjects.JUNIPER_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0)), new ForkingTrunkPlacer(3, 1, 1), EMPTY_SIZE).ignoreVines().build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> TREE_CYPRESS = create("tree_cypress", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.CYPRESS_LOG.getDefaultState()), new SimpleBlockStateProvider(BWObjects.CYPRESS_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 4), new StraightTrunkPlacer(6, 1, 1), EMPTY_SIZE).ignoreVines().build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> TREE_ELDER = create("tree_elder", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.ELDER_LOG.getDefaultState()), new SimpleBlockStateProvider(BWObjects.ELDER_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 4), new StraightTrunkPlacer(4, 0, 1), EMPTY_SIZE).ignoreVines().build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> TREE_DRAGONS_BLOOD = create("tree_dragons_blood", Feature.TREE.configure(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.DRAGONS_BLOOD_LOG.getDefaultState().with(BWProperties.NATURAL, true)), new SimpleBlockStateProvider(BWObjects.DRAGONS_BLOOD_LEAVES.getDefaultState()), new MegaPineFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), UniformIntDistribution.of(3)), new StraightTrunkPlacer(5, 1, 1), EMPTY_SIZE).ignoreVines().build()));
	
	public static final ConfiguredFeature<?, ?> ORE_SILVER = create("ore_silver", Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, BWObjects.SILVER_ORE.getDefaultState(), Bewitchment.config.silverSize)).rangeOf(Bewitchment.config.silverMaxHeight).spreadHorizontally().repeat(Bewitchment.config.silverCount));
	public static final ConfiguredFeature<?, ?> ORE_SALT = create("ore_salt", Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, BWObjects.SALT_ORE.getDefaultState(), Bewitchment.config.saltSize)).rangeOf(Bewitchment.config.saltMaxHeight).spreadHorizontally().repeat(Bewitchment.config.saltCount));
	
	private static <F extends FeatureConfig> ConfiguredFeature<F, ?> create(String name, ConfiguredFeature<F, ?> configuredFeature) {
		CONFIGURED_FEATURES.put(configuredFeature, new Identifier(Bewitchment.MODID, name));
		return configuredFeature;
	}
	
	@SuppressWarnings("UnstableApiUsage")
	public static void init() {
		ConfiguredFeature<?, ?> TREE_JUNIPER_CHANCE = create("tree_juniper_chance", TREE_JUNIPER.decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))));
		ConfiguredFeature<?, ?> TREE_CYPRESS_CHANCE = create("tree_cypress_chance", TREE_CYPRESS.decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))));
		ConfiguredFeature<?, ?> TREE_ELDER_CHANCE = create("tree_elder_chance", TREE_ELDER.decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5))));
		CONFIGURED_FEATURES.keySet().forEach(configuredFeature -> Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, CONFIGURED_FEATURES.get(configuredFeature), configuredFeature));
		BiomeModificationImpl.INSTANCE.addModifier(CONFIGURED_FEATURES.get(TREE_JUNIPER), ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld().and(context -> context.getBiome().getCategory() == Biome.Category.SAVANNA), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, TREE_JUNIPER_CHANCE));
		BiomeModificationImpl.INSTANCE.addModifier(CONFIGURED_FEATURES.get(TREE_CYPRESS), ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld().and(context -> context.getBiome().getCategory() == Biome.Category.TAIGA || context.getBiome().getCategory() == Biome.Category.SWAMP), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, TREE_CYPRESS_CHANCE));
		BiomeModificationImpl.INSTANCE.addModifier(CONFIGURED_FEATURES.get(TREE_ELDER), ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld().and(context -> context.getBiome().getCategory() == Biome.Category.FOREST), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, TREE_ELDER_CHANCE));
		BiomeModificationImpl.INSTANCE.addModifier(CONFIGURED_FEATURES.get(ORE_SILVER), ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE_SILVER));
		BiomeModificationImpl.INSTANCE.addModifier(CONFIGURED_FEATURES.get(ORE_SALT), ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE_SALT));
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
