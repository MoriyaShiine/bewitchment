package moriyashiine.bewitchment.common.registry;

import net.minecraft.item.FoodComponent;

public class BWFoodComponents {
	public static final FoodComponent WITCHBERRY = new FoodComponent.Builder().hunger(3).saturationModifier(0.6f).alwaysEdible().build();
	public static final FoodComponent WITCHBERRY_PIE = new FoodComponent.Builder().hunger(8).saturationModifier(0.3f).alwaysEdible().build();
	public static final FoodComponent WITCHBERRY_COOKIE = new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).alwaysEdible().build();
}
