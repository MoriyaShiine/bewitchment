package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class WisdomFortune extends Fortune {
	public WisdomFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		target.addExperience(551);
		return super.finish(world, target);
	}
}
