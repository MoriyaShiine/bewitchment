package moriyashiine.bewitchment.common.contract;

import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.entity.player.PlayerEntity;

public class SlothContract extends Contract {
	@Override
	public void tick(PlayerEntity target) {
		if (target.age % 10 == 0 && target.isOnGround() && !target.isSprinting()) {
			target.heal(1);
		}
	}
}
