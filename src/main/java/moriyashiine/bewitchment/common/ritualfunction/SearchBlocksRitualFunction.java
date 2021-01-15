package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.RitualFunction;
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
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, false);
		if (closestPlayer != null) {
			Block block = world.getBlockState(pos.down(2)).getBlock();
			int blocks = BewitchmentAPI.getBlockPoses(pos, 16, currentPos -> world.getBlockState(currentPos).getBlock() == block).size();
			closestPlayer.sendMessage(new TranslatableText("bewitchment.found_block" + (blocks == 1 ? "" : "s"), blocks, block.getName()), true);
		}
		super.start(world, pos, inventory);
	}
}
