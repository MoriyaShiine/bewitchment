package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class SummonDemonRitualFunction extends RitualFunction {
	public SummonDemonRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		DemonEntity entity = BWEntityTypes.DEMON.create(world);
		if (entity != null) {
			entity.initialize(world, world.getLocalDifficulty(effectivePos), SpawnReason.EVENT, null, null);
			entity.updatePositionAndAngles(effectivePos.getX() + 0.5, effectivePos.getY(), effectivePos.getZ() + 0.5, 0, world.random.nextInt(360));
			world.spawnEntity(entity);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
