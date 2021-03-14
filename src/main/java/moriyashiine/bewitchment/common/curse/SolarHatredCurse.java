package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
import net.minecraft.entity.LivingEntity;

public class SolarHatredCurse extends Curse {
	public SolarHatredCurse(Type type) {
		super(type);
	}
	
	@Override
	public void tick(LivingEntity target) {
		if (target instanceof RespawnTimerAccessor && ((RespawnTimerAccessor) target).getRespawnTimer() > 0) {
			return;
		}
		if (target.age % 400 == 0 && target.world.isDay() && target.world.isSkyVisible(target.getBlockPos())) {
			target.setOnFireFor(8);
		}
	}
}
