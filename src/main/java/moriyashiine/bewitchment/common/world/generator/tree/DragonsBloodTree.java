package moriyashiine.bewitchment.common.world.generator.tree;

import com.mojang.datafixers.Dynamic;
import moriyashiine.bewitchment.common.block.BWProperties;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.Direction;
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

public class DragonsBloodTree extends Tree {
	private static final TreeFeatureConfig CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BWObjects.dragons_blood_log.getDefaultState().with(BWProperties.NATURAL, true)), new SimpleBlockStateProvider(BWObjects.dragons_blood_leaves.getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(7).heightRandA(2).ignoreVines().setSapling(BWObjects.dragons_blood_sapling).build();
	
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
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					if (!isSoil(reader, dirt.offset(direction), config.getSapling())) {
						return false;
					}
				}
				BlockPos.Mutable mutablePos = new BlockPos.Mutable(startingPos);
				for (int y = 0; y < height; y++) {
					if (!canBeReplacedByLogs(reader, mutablePos.setPos(startingPos.getX(), startingPos.getY() + y, startingPos.getZ()))) {
						return false;
					}
					if (y > height / 2) {
						int var = y - (height / 2);
						for (int x = -1; x <= 1; x++) {
							for (int z = -1; z <= 1; z++) {
								if (!canBeReplacedByLogs(reader, mutablePos.add(var * x, x == 0 || z == 0 ? -1 : 0, var * z))) {
									return false;
								}
							}
						}
					}
				}
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					if (!canBeReplacedByLogs(reader, startingPos.offset(direction))) {
						return false;
					}
				}
				for (int y = 0; y < height; y++) {
					mutablePos.setPos(startingPos.getX(), startingPos.getY() + y, startingPos.getZ());
					if (y == 0) {
						for (Direction direction : Direction.Plane.HORIZONTAL) {
							func_227216_a_(reader, random, mutablePos.offset(direction), logBlocks, box, config);
						}
					}
					func_227216_a_(reader, random, mutablePos, logBlocks, box, config);
					if (y > height / 2) {
						int var = y - (height / 2);
						for (int x = -1; x <= 1; x++) {
							for (int z = -1; z <= 1; z++) {
								func_227216_a_(reader, random, mutablePos.add(var * x, x == 0 || z == 0 ? -1 : 0, var * z), logBlocks, box, config);
							}
						}
					}
				}
				for (int y = height + 1; y > height / 2; y--) {
					int var = 0;
					if (y == height) {
						var = 4;
					}
					if (y == height + 1 || y == height - 1) {
						var = 3;
					}
					if (y == height - 2) {
						var = 2;
					}
					if (y == height - 3) {
						var = 1;
					}
					for (int x = -var; x <= var; x++) {
						for (int z = -var; z <= var; z++) {
							if (isAirOrLeaves(reader, mutablePos.setPos(startingPos.getX() + x, startingPos.getY() + y, startingPos.getZ() + z))) {
								func_227219_b_(reader, random, mutablePos, foliageBlocks, box, config);
							}
						}
					}
				}
				setDirtAt(reader, dirt, startingPos);
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					setDirtAt(reader, dirt.offset(direction), startingPos);
				}
				return true;
			}
			return false;
		}
	}
}