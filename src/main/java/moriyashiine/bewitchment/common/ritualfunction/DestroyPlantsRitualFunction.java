package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class DestroyPlantsRitualFunction extends RitualFunction {
	public DestroyPlantsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int radius = 10;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					Block block = world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();
					if (block instanceof PlantBlock || block instanceof LeavesBlock) {
						world.breakBlock(mutable, true);
					}
				}
			}
		}
		super.start(world, pos, inventory);
	}
}
