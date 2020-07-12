package moriyashiine.bewitchment.common;

import io.github.cottonmc.cotton.config.ConfigManager;
import moriyashiine.bewitchment.common.misc.BWConfig;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWWorldGenerator;
import net.fabricmc.api.ModInitializer;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	@Override
	public void onInitialize() {
		ConfigManager.loadConfig(BWConfig.class);
		BWObjects.init();
		BWEntityTypes.init();
		BWRecipeTypes.init();
		BWSoundEvents.init();
		BWTreeDecoratorTypes.init();
		BWRitualTypes.init();
		BWWorldGenerator.init();
		BWUtil.registerAdditionalSilverWeakness(livingEntity -> BWUtil.isVampire(livingEntity) || BWUtil.isWerewolf(livingEntity));
	}
}