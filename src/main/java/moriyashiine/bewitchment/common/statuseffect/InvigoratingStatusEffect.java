package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class InvigoratingStatusEffect extends StatusEffect {
	public InvigoratingStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof MagicAccessor) {
			((MagicAccessor) entity).fillMagic(15 * (amplifier + 1), false);
		}
	}
}
