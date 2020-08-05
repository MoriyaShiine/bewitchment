package moriyashiine.bewitchment.common;

import io.github.cottonmc.cotton.config.ConfigManager;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWWorldGenerator;
import net.fabricmc.api.ModInitializer;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static final BWConfig CONFIG = new BWConfig();
	
	@Override
	public void onInitialize() {
		ConfigManager.loadConfig(BWConfig.class);
		BWObjects.init();
		BWBlockEntityTypes.init();
		BWEntityTypes.init();
		BWRecipeTypes.init();
		BWSoundEvents.init();
		BWTreeDecoratorTypes.init();
		BWRitualTypes.init();
		BWWorldGenerator.init();
		BewitchmentAPI.registerAdditionalSilverWeakness(livingEntity -> BewitchmentAPI.isVampire(livingEntity) || BewitchmentAPI.isWerewolf(livingEntity));
	}
}
