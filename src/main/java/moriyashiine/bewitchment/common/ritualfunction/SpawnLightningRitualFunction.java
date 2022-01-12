package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SpawnLightningRitualFunction extends RitualFunction {
	public SpawnLightningRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		summonLightning(world, effectivePos);
		if (catFamiliar) {
			for (int i = 0; i < MathHelper.nextInt(world.random, 2, 4); i++) {
				summonLightning(world, effectivePos.add(MathHelper.nextInt(world.random, -4, 4), 0, MathHelper.nextInt(world.random, -4, 4)));
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}

	private void summonLightning(World world, BlockPos pos) {
		LightningEntity entity = EntityType.LIGHTNING_BOLT.create(world);
		if (entity != null) {
			entity.updatePositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
			world.spawnEntity(entity);
		}
	}
}
