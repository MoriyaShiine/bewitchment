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
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		LeonardEntity entity = BWEntityTypes.LEONARD.create(world);
		if (entity != null) {
			entity.initialize(world, world.getLocalDifficulty(effectivePos), SpawnReason.EVENT, null, null);
			entity.updatePositionAndAngles(effectivePos.getX() + 0.5, effectivePos.getY(), effectivePos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
			world.spawnEntity(entity);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
