package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class HurricaneRitualFunction extends RitualFunction {
	public HurricaneRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		for (BlockPos plant : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8, currentPos -> world.getBlockState(currentPos).getBlock() instanceof PlantBlock || world.getBlockState(currentPos).getBlock() instanceof LeavesBlock)) {
			world.breakBlock(plant, true);
		}
		for (Entity entity : world.getEntitiesByClass(Entity.class, new Box(effectivePos).expand(catFamiliar ? 24 : 8), Entity::isAlive)) {
			entity.addVelocity(0, 4, 0);
			if (entity instanceof PlayerEntity) {
				entity.velocityModified = true;
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
