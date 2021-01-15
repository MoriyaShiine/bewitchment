package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class StartRainRitualFunction extends RitualFunction {
	public StartRainRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.no_rain";
	}
	
	@Override
	public boolean isValid(ServerWorld world, BlockPos pos, Inventory inventory) {
		return !world.isRaining();
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		world.setWeather(0, world.random.nextInt(6000) + 6000, true, false);
		super.start(world, pos, inventory);
	}
}
