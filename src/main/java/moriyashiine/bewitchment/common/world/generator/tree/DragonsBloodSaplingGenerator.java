package moriyashiine.bewitchment.common.world.generator.tree;

import moriyashiine.bewitchment.common.registry.BWWorldGenerators;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DragonsBloodSaplingGenerator extends SaplingGenerator {
	@Override
	protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
		return BWWorldGenerators.TREE_DRAGONS_BLOOD;
	}
}
