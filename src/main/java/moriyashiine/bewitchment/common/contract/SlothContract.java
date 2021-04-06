package moriyashiine.bewitchment.common.contract;

import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class SlothContract extends Contract {
	@Override
	public void tick(PlayerEntity target, boolean includeNegative) {
		if (target.age % 20 == 0) {
			if (target.isOnGround() && !target.isSprinting()) {
				target.heal(1);
			}
			if (includeNegative) {
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0, true, false));
			}
		}
	}
}
