package moriyashiine.bewitchment.common.world;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.tree.LeaveVineTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class BWWorldGenerator {
	private static final ConfiguredFeature<?, ?> SWAMP_TREE_WITH_SPANISH_MOSS_CONFIG = Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(3), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(5, 3, 0), new TwoLayersFeatureSize(1, 0, 1))).maxWaterDepth(1).decorators(ImmutableList.of(LeaveVineTreeDecorator.INSTANCE)).build()).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT.configure(new CountConfig(1)));
	private static final ConfiguredFeature<?, ?> ORE_SILVER = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, BWObjects.silver_ore.getDefaultState(), Bewitchment.CONFIG.silverOreSize)).method_30377(Bewitchment.CONFIG.silverOreMaxHeight).spreadHorizontally().repeat(Bewitchment.CONFIG.silverOreCount);
	private static final ConfiguredFeature<?, ?> ORE_SALT = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, BWObjects.salt_ore.getDefaultState(), Bewitchment.CONFIG.saltOreSize)).method_30377(Bewitchment.CONFIG.saltOreMaxHeight).spreadHorizontally().repeat(Bewitchment.CONFIG.saltOreCount);
	
	private static final SpawnSettings.SpawnEntry OWL_SPAWN_ENTRY = new SpawnSettings.SpawnEntry(BWEntityTypes.owl, Bewitchment.CONFIG.owlWeight, Bewitchment.CONFIG.owlMinGroupCount, Bewitchment.CONFIG.owlMaxGroupCount);
	private static final SpawnSettings.SpawnEntry RAVEN_SPAWN_ENTRY = new SpawnSettings.SpawnEntry(BWEntityTypes.raven, Bewitchment.CONFIG.ravenWeight, Bewitchment.CONFIG.ravenMinGroupCount, Bewitchment.CONFIG.ravenMaxGroupCount);
	private static final SpawnSettings.SpawnEntry SNAKE_SPAWN_ENTRY = new SpawnSettings.SpawnEntry(BWEntityTypes.snake, Bewitchment.CONFIG.snakeWeight, Bewitchment.CONFIG.snakeMinGroupCount, Bewitchment.CONFIG.snakeMaxGroupCount);
	private static final SpawnSettings.SpawnEntry TOAD_SPAWN_ENTRY = new SpawnSettings.SpawnEntry(BWEntityTypes.toad, Bewitchment.CONFIG.toadWeight, Bewitchment.CONFIG.toadMinGroupCount, Bewitchment.CONFIG.toadMaxGroupCount);
	
	public static void init() {
		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
			if (Blocks.GRASS.getLootTableId().equals(id) || Blocks.TALL_GRASS.getLootTableId().equals(id) || Blocks.FERN.getLootTableId().equals(id) || Blocks.LARGE_FERN.getLootTableId().equals(id)) {
				supplier.withPool(FabricLootPoolBuilder.builder().rolls(ConstantLootTableRange.create(1)).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.aconite_seeds).build()).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.belladonna_seeds).build()).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.garlic).build()).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.mandrake_seeds).build()).build());
			}
			if (LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
				supplier.withPool(FabricLootPoolBuilder.builder().rolls(ConstantLootTableRange.create(1)).withFunction(SetCountLootFunction.builder(UniformLootTableRange.between(1, 2)).build()).withEntry(ItemEntry.builder(BWObjects.dragons_blood_sapling).weight(10).build()).build());
			}
		});
		Biome.BIOMES.forEach(BWWorldGenerator::registerStuffInBiome);
	}
	
	private static void registerStuffInBiome(Biome biome) {
		Biome.Category category = biome.getCategory();
		if (Bewitchment.CONFIG.owlBiomeCategories.contains(category.getName())) {
			biome.getSpawnSettings().getSpawnEntry(BWEntityTypes.owl.getSpawnGroup()).add(OWL_SPAWN_ENTRY);
		}
		if (Bewitchment.CONFIG.ravenBiomeCategories.contains(category.getName())) {
			biome.getSpawnSettings().getSpawnEntry(BWEntityTypes.raven.getSpawnGroup()).add(RAVEN_SPAWN_ENTRY);
		}
		if (Bewitchment.CONFIG.snakeBiomeCategories.contains(category.getName())) {
			biome.getSpawnSettings().getSpawnEntry(BWEntityTypes.snake.getSpawnGroup()).add(SNAKE_SPAWN_ENTRY);
		}
		if (Bewitchment.CONFIG.toadBiomeCategories.contains(category.getName())) {
			biome.getSpawnSettings().getSpawnEntry(BWEntityTypes.toad.getSpawnGroup()).add(TOAD_SPAWN_ENTRY);
		}
		//			if (category == Biome.Category.SAVANNA) {
		//				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, JuniperTree.FEATURE.createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(1, 0.05f, 1))));
		//			}
		//			if (category == Biome.Category.TAIGA || category == Biome.Category.SWAMP) {
		//				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, CypressTree.FEATURE.createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(1, 0.05f, 1))));
		//			}
		//			if (category == Biome.Category.FOREST) {
		//				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ElderTree.FEATURE.createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(1, 0.05f, 1))));
		//			}
		//			if (category == Biome.Category.SWAMP) {
		//				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, SWAMP_TREE_WITH_SPANISH_MOSS_CONFIG);
		//			}
		//			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE_SILVER);
		//			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ORE_SALT);
	}
}
