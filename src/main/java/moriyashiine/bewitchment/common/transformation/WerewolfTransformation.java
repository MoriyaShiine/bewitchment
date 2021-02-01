package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.minecraft.entity.LivingEntity;

public class WerewolfTransformation extends Transformation {
	@Override
	public void onAdded(LivingEntity entity) {
		if (entity instanceof WerewolfAccessor) {
			((WerewolfAccessor) entity).setWerewolfVariant(entity.getRandom().nextInt(WerewolfEntity.getVariantsStatic()));
		}
	}
}
