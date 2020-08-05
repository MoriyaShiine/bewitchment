package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class BWFoodComponents {
	public static final FoodComponent demon_heart = new FoodComponent.Builder().hunger(6).saturationModifier(0.5f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 3), 1).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 3), 1).statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 600, 2), 1).build();
	public static final FoodComponent garlic_bread = new FoodComponent.Builder().hunger(8).saturationModifier(0.6f).build();
}
