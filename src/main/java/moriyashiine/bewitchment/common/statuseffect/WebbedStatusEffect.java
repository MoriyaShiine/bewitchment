package moriyashiine.bewitchment.common.statuseffect;

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
			int radius = Math.min(3, amplifier + 1);
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						mutable.set(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
						if (entity.world.getBlockState(mutable).isAir()) {
							entity.world.setBlockState(mutable, BWObjects.TEMPORARY_COBWEB.getDefaultState());
						}
					}
				}
			}
		}
	}
}
