package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.minecraft.entity.player.PlayerEntity;

public class WerewolfTransformation extends Transformation {
	@Override
	public void onAdded(PlayerEntity player) {
		((WerewolfAccessor) player).setWerewolfVariant(player.getRandom().nextInt(WerewolfEntity.getVariantsStatic()));
	}
}
