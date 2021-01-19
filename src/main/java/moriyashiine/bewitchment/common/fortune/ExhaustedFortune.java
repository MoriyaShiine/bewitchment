package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ExhaustedFortune extends Fortune {
	public ExhaustedFortune(boolean positive) {
		super(positive);
	}
	
	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		MagicAccessor magicAccessor = MagicAccessor.of(target).orElse(null);
		return magicAccessor != null && magicAccessor.drainMagic(2500, false);
	}
}
