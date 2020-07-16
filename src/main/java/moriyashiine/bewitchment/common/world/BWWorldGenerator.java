package moriyashiine.bewitchment.common.world;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.world.generator.decorator.LeaveSpanishMossTreeDecorator;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class BWWorldGenerator {
	private static final TreeFeatureConfig SWAMP_TREE_WITH_SPANISH_MOSS_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(3, 0, 0, 0, 3), new StraightTrunkPlacer(5, 3, 0), new TwoLayersFeatureSize(1, 0, 1))).maxWaterDepth(1).decorators(ImmutableList.of(LeaveSpanishMossTreeDecorator.INSTANCE)).build();
	
	public static void init() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
			if (Blocks.GRASS.getLootTableId().equals(identifier) || Blocks.TALL_GRASS.getLootTableId().equals(identifier) || Blocks.FERN.getLootTableId().equals(identifier) || Blocks.LARGE_FERN.getLootTableId().equals(identifier)) {
				fabricLootSupplierBuilder.withPool(FabricLootPoolBuilder.builder().rolls(ConstantLootTableRange.create(1)).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.aconite_seeds).build()).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.belladonna_seeds).build()).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.garlic).build()).withCondition(RandomChanceWithLootingLootCondition.builder(0.125f, 2).build()).withEntry(ItemEntry.builder(BWObjects.mandrake_seeds).build()).build());
			}
			if (LootTables.NETHER_BRIDGE_CHEST.equals(identifier)) {
				fabricLootSupplierBuilder.withPool(FabricLootPoolBuilder.builder().rolls(ConstantLootTableRange.create(1)).withFunction(SetCountLootFunction.builder(UniformLootTableRange.between(1, 2)).build()).withEntry(ItemEntry.builder(BWObjects.dragons_blood_sapling).weight(10).build()).build());
			}
		});
		for (Biome biome : Registry.BIOME) {
			Biome.Category category = biome.getCategory();
			if (BWConfig.INSTANCE.owlBiomeCategories.contains(category.getName())) {
				addEntitySpawn(biome, BWEntityTypes.owl, BWConfig.INSTANCE.owlWeight, BWConfig.INSTANCE.owlMinGroupCount, BWConfig.INSTANCE.owlMaxGroupCount);
			}
			if (BWConfig.INSTANCE.ravenBiomeCategories.contains(category.getName())) {
				addEntitySpawn(biome, BWEntityTypes.raven, BWConfig.INSTANCE.ravenWeight, BWConfig.INSTANCE.ravenMinGroupCount, BWConfig.INSTANCE.ravenMaxGroupCount);
			}
			if (BWConfig.INSTANCE.snakeBiomeCategories.contains(category.getName())) {
				addEntitySpawn(biome, BWEntityTypes.snake, BWConfig.INSTANCE.snakeWeight, BWConfig.INSTANCE.snakeMinGroupCount, BWConfig.INSTANCE.snakeMaxGroupCount);
			}
			if (BWConfig.INSTANCE.toadBiomeCategories.contains(category.getName())) {
				addEntitySpawn(biome, BWEntityTypes.toad, BWConfig.INSTANCE.toadWeight, BWConfig.INSTANCE.toadMinGroupCount, BWConfig.INSTANCE.toadMaxGroupCount);
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
			if (category == Biome.Category.SWAMP) {
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.TREE.configure(SWAMP_TREE_WITH_SPANISH_MOSS_CONFIG).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(1, 0.05f, 1))));
			}
			addOres(biome);
		}
	}
	
	private static void addEntitySpawn(Biome biome, EntityType<?> entityType, int weight, int minGroupCount, int maxGroupCount) {
		biome.getEntitySpawnList(entityType.getSpawnGroup()).add(new Biome.SpawnEntry(entityType, weight, minGroupCount, maxGroupCount));
	}
	
	private static void addOres(Biome biome) {
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, BWObjects.silver_ore.getDefaultState(), BWConfig.INSTANCE.silverOreSize)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(BWConfig.INSTANCE.silverOreCount, 0, 0, BWConfig.INSTANCE.silverOreMaxHeight))));
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, BWObjects.salt_ore.getDefaultState(), BWConfig.INSTANCE.saltOreSize)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(BWConfig.INSTANCE.saltOreCount, 0, 0, BWConfig.INSTANCE.saltOreMaxHeight))));
	}
}