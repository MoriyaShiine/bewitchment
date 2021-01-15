package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.GrassBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class GrowRitualFunction extends RitualFunction {
	public GrowRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void tick(World world, BlockPos pos) {
		int radius = 3;
		if (!world.isClient) {
			if (world.getTime() % 20 == 0) {
				for (PassiveEntity passiveEntity : world.getEntitiesByClass(PassiveEntity.class, new Box(pos).expand(radius, 0, radius), PassiveEntity::isBaby)) {
					if (world.random.nextFloat() < 1 / 4f) {
						passiveEntity.growUp(world.random.nextInt(), true);
					}
				}
				for (BlockPos growable : BewitchmentAPI.getBlockPoses(pos, radius, currentPos -> !(world.getBlockState(currentPos).getBlock() instanceof GrassBlock) && world.getBlockState(currentPos).getBlock() instanceof Fertilizable && ((Fertilizable) world.getBlockState(currentPos).getBlock()).canGrow(world, world.random, currentPos, world.getBlockState(currentPos)))) {
					((Fertilizable) world.getBlockState(growable).getBlock()).grow((ServerWorld) world, world.random, growable, world.getBlockState(growable));
				}
			}
		}
		else {
			world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + MathHelper.nextDouble(world.random, -radius, radius), pos.getY() + 0.5, pos.getZ() + MathHelper.nextDouble(world.random, -radius, radius), 0, 0, 0);
		}
	}
}
