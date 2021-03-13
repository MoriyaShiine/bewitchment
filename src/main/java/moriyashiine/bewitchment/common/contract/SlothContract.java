package moriyashiine.bewitchment.common.contract;

import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class SlothContract extends Contract {
	public SlothContract(boolean doesBaphometOffer) {
		super(doesBaphometOffer);
	}
	
	public void tick(LivingEntity target, boolean includeNegative) {
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
