package moriyashiine.bewitchment.common;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWWorldGenerator;
import net.fabricmc.api.ModInitializer;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static BWConfig CONFIG;
	
	@Override
	public void onInitialize() {
		AutoConfig.register(BWConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(BWConfig.class).getConfig();
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
