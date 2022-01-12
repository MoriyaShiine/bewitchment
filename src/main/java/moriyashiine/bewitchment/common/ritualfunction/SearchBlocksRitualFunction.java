/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class SearchBlocksRitualFunction extends RitualFunction {
	public SearchBlocksRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		PlayerEntity closestPlayer = world.getClosestPlayer(glyphPos.getX() + 0.5, glyphPos.getY() + 0.5, glyphPos.getZ() + 0.5, 8, false);
		if (closestPlayer != null) {
			Block block = world.getBlockState(glyphPos.down(2)).getBlock();
			int blocks = BWUtil.getBlockPoses(effectivePos, catFamiliar ? 48 : 16, currentPos -> world.getBlockState(currentPos).getBlock() == block).size();
			closestPlayer.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.found_block" + (blocks == 1 ? "" : "s"), blocks, block.getName()), true);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
