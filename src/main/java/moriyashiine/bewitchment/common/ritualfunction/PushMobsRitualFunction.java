package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class PushMobsRitualFunction extends RitualFunction {
	public PushMobsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void tick(World world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
		if (!world.isClient) {
			int radius = catFamiliar ? 24 : 8;
			if (world.getTime() % 5 == 0) {
				for (HostileEntity hostileEntity : world.getEntitiesByClass(HostileEntity.class, new Box(effectivePos).expand(radius, 0, radius), LivingEntity::isAlive)) {
					double distanceX = effectivePos.getX() - hostileEntity.getX();
					double distanceZ = effectivePos.getZ() - hostileEntity.getZ();
					double max = MathHelper.absMax(distanceX, distanceZ);
					if (max >= 0) {
						max = MathHelper.sqrt((float) max);
						distanceX /= max;
						distanceZ /= max;
						distanceX *= Math.min(1, 1 / max);
						distanceZ *= Math.min(1, 1 / max);
						distanceX /= 2;
						distanceZ /= 2;
						hostileEntity.addVelocity(-distanceX, 0, -distanceZ);
					}
				}
			}
		}
	}
}
