package moriyashiine.bewitchment.common.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;

public class SinkingStatusEffect extends StatusEffect {
	public SinkingStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		Vec3d velocity = entity.getVelocity();
		float amount = 0.05f * (amplifier + 1);
		if (!entity.isOnGround() && velocity.getY() < 0) {
			entity.setVelocity(velocity.multiply(1, 1 + amount, 1));
		}
		if (entity.isTouchingWater() || entity.isFallFlying()) {
			entity.setVelocity(velocity.add(0, -amount / 2, 0));
		}
	}
}
