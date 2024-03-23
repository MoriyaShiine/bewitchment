/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class DrainWaterRitualFunction extends RitualFunction {
	public DrainWaterRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8)) {
			if (world.getWorldBorder().contains(foundPos) && world.getFluidState(foundPos).isIn(FluidTags.WATER)) {
				BlockState state = world.getBlockState(foundPos);
				if (state.getBlock() instanceof FluidDrainable fluidDrainable) {
					fluidDrainable.tryDrainFluid(world, foundPos, state);
				}
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
