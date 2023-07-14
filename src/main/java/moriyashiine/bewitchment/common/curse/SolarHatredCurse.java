/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SolarHatredCurse extends Curse {
	public SolarHatredCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target instanceof PlayerEntity player && BWComponents.RESPAWN_TIMER_COMPONENT.get(player).getRespawnTimer() > 0) {
			return;
		}
		if (target.age % 400 == 0 && target.getWorld().isDay() && target.getWorld().isSkyVisible(target.getBlockPos())) {
			target.setOnFireFor(8);
		}
	}
}
