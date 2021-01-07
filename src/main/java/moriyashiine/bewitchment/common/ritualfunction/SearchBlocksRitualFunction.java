package moriyashiine.bewitchment.common.ritualfunction;

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
		Block block = world.getBlockState(pos.down(2)).getBlock();
		int found = 0;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int radius = 16;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock() == block) {
						found++;
					}
				}
			}
		}
		PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, false);
		if (closestPlayer != null) {
			closestPlayer.sendMessage(new TranslatableText("bewitchment.found_blocks", found, block.getName()), true);
		}
		super.start(world, pos, inventory);
	}
}
