/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class MisfortuneCurse extends Curse {
	public MisfortuneCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target.age % 20 == 0 && target.getRandom().nextFloat() < 1 / 100f) {
			target.addStatusEffect(getEffect(target.getRandom().nextInt(8)));
		}
	}

	private static StatusEffectInstance getEffect(int value) {
		return new StatusEffectInstance(value == 1 ? StatusEffects.POISON : value == 2 ? StatusEffects.WEAKNESS : value == 3 ? StatusEffects.SLOWNESS : value == 4 ? StatusEffects.BLINDNESS : value == 5 ? StatusEffects.NAUSEA : value == 6 ? StatusEffects.MINING_FATIGUE : value == 7 ? BWStatusEffects.CORROSION : BWStatusEffects.SINKING, 400);
	}
}
