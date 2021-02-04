package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class DestroyLightsRitualFunction extends RitualFunction {
	public DestroyLightsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		for (BlockPos light : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8, currentPos -> world.getBlockState(currentPos).getLuminance() > 0 && world.getBlockState(currentPos).getHardness(world, currentPos) == 0)) {
			world.breakBlock(light, true);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
