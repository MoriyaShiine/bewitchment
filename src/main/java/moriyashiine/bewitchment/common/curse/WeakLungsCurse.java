/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import net.minecraft.entity.LivingEntity;

public class WeakLungsCurse extends Curse {
	public WeakLungsCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target.getAir() > -15) {
			target.setAir(-15);
		}
	}
}
