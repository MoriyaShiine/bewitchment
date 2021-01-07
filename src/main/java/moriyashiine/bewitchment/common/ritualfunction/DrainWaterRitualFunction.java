package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
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
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int radius = 6;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (world.getFluidState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getFluid() == Fluids.WATER) {
						world.setBlockState(mutable, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
		super.start(world, pos, inventory);
	}
}
