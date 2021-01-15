package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.interfaces.WetAccessor;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class MakeEntitiesWetRitualFunction extends RitualFunction {
	public MakeEntitiesWetRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		int radius = 2;
		world.getEntitiesByClass(Entity.class, new Box(pos).expand(radius), Entity::isAlive).forEach(entity -> WetAccessor.of(entity).ifPresent(wetAccessor -> wetAccessor.setWetTimer(6000)));
		super.start(world, pos, inventory);
	}
}
