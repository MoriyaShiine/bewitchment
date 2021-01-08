package moriyashiine.bewitchment.api.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class Sigil {
	public final boolean active;
	
	public Sigil(boolean active) {
		this.active = active;
	}
	
	public boolean tick(ServerWorld world, BlockPos pos) {
		return false;
	}
	
	public boolean use(ServerWorld world, BlockPos pos, PlayerEntity user, Hand hand) {
		return false;
	}
}
