/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.player.PlayerEntity;

public class WerewolfTransformation extends Transformation {
	@Override
	public void onAdded(PlayerEntity player) {
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).setVariant(player.getRandom().nextInt(WerewolfEntity.getVariantsStatic()));
	}
}
