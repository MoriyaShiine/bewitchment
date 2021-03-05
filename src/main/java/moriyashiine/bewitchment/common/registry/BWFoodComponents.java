package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class BWFoodComponents {
	public static final FoodComponent DEMON_HEART = new FoodComponent.Builder().hunger(8).saturationModifier(0.6f).meat().alwaysEdible().statusEffect(new StatusEffectInstance(BWStatusEffects.CORRUPTION, 1), 1).statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 800, 2), 1).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 800, 2), 1).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 800, 2), 1).build();
	
	public static final FoodComponent GARLIC_BREAD = new FoodComponent.Builder().hunger(10).saturationModifier(0.5f).build();
	
	public static final FoodComponent WITCHBERRY = new FoodComponent.Builder().hunger(3).saturationModifier(0.6f).alwaysEdible().build();
	public static final FoodComponent WITCHBERRY_PIE = new FoodComponent.Builder().hunger(8).saturationModifier(0.3f).alwaysEdible().build();
	public static final FoodComponent WITCHBERRY_COOKIE = new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).alwaysEdible().build();
}
