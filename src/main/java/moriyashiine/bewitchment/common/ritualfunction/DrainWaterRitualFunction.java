package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class DrainWaterRitualFunction extends RitualFunction {
	public DrainWaterRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		for (BlockPos water : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8, currentPos -> world.getFluidState(currentPos).getFluid() == Fluids.WATER)) {
			world.setBlockState(water, Blocks.AIR.getDefaultState());
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
