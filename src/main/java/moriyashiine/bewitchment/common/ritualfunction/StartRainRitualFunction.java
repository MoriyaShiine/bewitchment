/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

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
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		world.setWeather(0, world.random.nextInt(6000) + (catFamiliar ? 18000 : 6000), true, catFamiliar);
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
