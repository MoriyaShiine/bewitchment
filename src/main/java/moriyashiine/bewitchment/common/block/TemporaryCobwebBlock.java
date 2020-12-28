package moriyashiine.bewitchment.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class TemporaryCobwebBlock extends CobwebBlock {
	public TemporaryCobwebBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
	}
}
