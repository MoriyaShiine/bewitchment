package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ClumsinessFortune extends Fortune {
	public ClumsinessFortune(boolean positive) {
		super(positive);
	}
	
	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		return target.dropItem(target.getMainHandStack(), false, true) != null;
	}
}
