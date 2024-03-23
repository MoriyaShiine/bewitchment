/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.BlockPos;

public class WebbedStatusEffect extends StatusEffect {
	public WebbedStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.getWorld().isClient) {
			for (BlockPos foundPos : BWUtil.getBlockPoses(entity.getBlockPos(), Math.min(3, amplifier + 1))) {
				if (entity.getWorld().getWorldBorder().contains(foundPos) && entity.getWorld().getBlockState(foundPos).isAir()) {
					entity.getWorld().setBlockState(foundPos, BWObjects.TEMPORARY_COBWEB.getDefaultState());
				}
			}
		}
	}
}
