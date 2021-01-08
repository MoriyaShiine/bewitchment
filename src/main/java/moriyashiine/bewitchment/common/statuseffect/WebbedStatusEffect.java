package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.BlockPos;

public class WebbedStatusEffect extends StatusEffect {
	public WebbedStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (!entity.world.isClient) {
			for (BlockPos air : BewitchmentAPI.getBlockPoses(entity.getBlockPos(), Math.min(3, amplifier + 1), currentPos -> entity.world.getBlockState(currentPos).isAir())) {
				entity.world.setBlockState(air, BWObjects.TEMPORARY_COBWEB.getDefaultState());
			}
		}
	}
}
