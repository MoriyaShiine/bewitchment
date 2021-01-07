package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class DestroyLightsRitualFunction extends RitualFunction {
	public DestroyLightsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int radius = 8;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getLuminance() > 0 && world.getBlockState(mutable).getHardness(world, mutable) == 0) {
						world.breakBlock(mutable, true);
					}
				}
			}
		}
		super.start(world, pos, inventory);
	}
}
