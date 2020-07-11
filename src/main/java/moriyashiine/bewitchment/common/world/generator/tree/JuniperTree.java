package moriyashiine.bewitchment.common.world.generator.tree;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class JuniperTree extends Tree {
	private static final TreeFeatureConfig CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.juniper_log.getDefaultState()), new SimpleBlockStateProvider(BWObjects.juniper_leaves.getDefaultState()), new AcaciaFoliagePlacer(2, 0)).baseHeight(3).heightRandA(1).heightRandB(1).trunkHeight(0).ignoreVines().setSapling(BWObjects.juniper_sapling).build();
	
	public static final ConfiguredFeature<TreeFeatureConfig, ?> FEATURE = Feature.ACACIA_TREE.withConfiguration(CONFIG);
	
	@Override
	@Nullable
	public ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(@Nonnull Random random, boolean flag) {
		return FEATURE;
	}
}