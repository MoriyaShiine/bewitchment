package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class TurnToDayRitualFunction extends RitualFunction {
	public TurnToDayRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.night";
	}

	@Override
	public boolean isValid(ServerWorld world, BlockPos pos, Inventory inventory) {
		return world.isNight();
	}

	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		while (world.getTimeOfDay() % 24000 != 0) {
			world.setTimeOfDay(world.getTimeOfDay() + 1);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
