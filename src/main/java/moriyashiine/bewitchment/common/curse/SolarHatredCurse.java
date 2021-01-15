package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.interfaces.RespawnTimerAccessor;
import moriyashiine.bewitchment.api.registry.Curse;
import net.minecraft.entity.LivingEntity;

public class SolarHatredCurse extends Curse {
	public SolarHatredCurse(Type type) {
		super(type);
	}
	
	@Override
	public void tick(LivingEntity target) {
		RespawnTimerAccessor respawnTimerAccessor = RespawnTimerAccessor.of(target).orElse(null);
		if (respawnTimerAccessor != null && respawnTimerAccessor.getRespawnTimer() > 0) {
			return;
		}
		if (target.getFireTicks() <= 1 && target.world.isDay() && target.world.isSkyVisible(target.getBlockPos())) {
			target.setOnFireFor(1);
		}
	}
}
