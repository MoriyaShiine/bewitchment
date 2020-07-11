package moriyashiine.bewitchment.common.world.generator.tree;

import com.mojang.datafixers.Dynamic;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class ElderTree extends Tree {
	private static final TreeFeatureConfig CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.elder_log.getDefaultState()), new SimpleBlockStateProvider(BWObjects.elder_leaves.getDefaultState()), new BlobFoliagePlacer(0, 0)).baseHeight(3).heightRandA(2).ignoreVines().setSapling(BWObjects.elder_sapling).build();
	
	public static final ConfiguredFeature<TreeFeatureConfig, ?> FEATURE = new Feature(TreeFeatureConfig::func_227338_a_).withConfiguration(CONFIG);
	
	@Override
	@Nullable
	public ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(@Nonnull Random random, boolean flag) {
		return FEATURE;
	}
	
	private static class Feature extends AbstractTreeFeature<TreeFeatureConfig> {
		public Feature(Function<Dynamic<?>, ? extends TreeFeatureConfig> function) {
			super(function);
		}
		
		@Override
		protected boolean place(@Nonnull IWorldGenerationReader reader, @Nonnull Random random, @Nonnull BlockPos startingPos, @Nonnull Set<BlockPos> logBlocks, @Nonnull Set<BlockPos> foliageBlocks, @Nonnull MutableBoundingBox box, @Nonnull TreeFeatureConfig config) {
			int height = config.baseHeight + random.nextInt(config.heightRandA);
			if (startingPos.getY() + height + 1 < reader.getMaxHeight()) {
				BlockPos dirt = startingPos.down();
				if (isSoil(reader, dirt, config.getSapling())) {
					BlockPos.Mutable mutablePos = new BlockPos.Mutable(startingPos);
					for (int y = 0; y < height; y++) {
						if (!canBeReplacedByLogs(reader, mutablePos.setPos(startingPos.getX(), startingPos.getY() + y, startingPos.getZ()))) {
							return false;
						}
					}
					for (int y = 0; y < height; y++) {
						func_227216_a_(reader, random, mutablePos.setPos(startingPos.getX(), startingPos.getY() + y, startingPos.getZ()), logBlocks, box, config);
						if (y > 0) {
							for (int x = -2; x <= 2; x++) {
								for (int z = -2; z <= 2; z++) {
									if (isAirOrLeaves(reader, mutablePos.setPos(startingPos.getX() + x, startingPos.getY() + y, startingPos.getZ() + z)) && (Math.abs(x) != 2 || Math.abs(z) != 2)) {
										func_227219_b_(reader, random, mutablePos, foliageBlocks, box, config);
									}
								}
							}
						}
					}
					for (int x = -1; x <= 1; x++) {
						for (int z = -1; z <= 1; z++) {
							func_227219_b_(reader, random, mutablePos.setPos(startingPos.getX() + x, startingPos.getY() + height, startingPos.getZ() + z), foliageBlocks, box, config);
						}
					}
					setDirtAt(reader, dirt, startingPos);
					return true;
				}
			}
			return false;
		}
	}
}