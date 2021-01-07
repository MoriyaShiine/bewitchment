package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class SummonLeonardRitualFunction extends RitualFunction {
	public SummonLeonardRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		LeonardEntity entity = BWEntityTypes.LEONARD.create(world);
		if (entity != null) {
			entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
			entity.updatePositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, world.random.nextInt(360));
			world.spawnEntity(entity);
		}
		super.start(world, pos, inventory);
	}
}
