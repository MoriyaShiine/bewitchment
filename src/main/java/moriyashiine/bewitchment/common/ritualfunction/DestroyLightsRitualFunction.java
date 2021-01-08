package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
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
		for (BlockPos light : BewitchmentAPI.getBlockPoses(pos, 8, currentPos -> world.getBlockState(currentPos).getLuminance() > 0 && world.getBlockState(currentPos).getHardness(world, currentPos) == 0)) {
			world.breakBlock(light, true);
		}
		super.start(world, pos, inventory);
	}
}
