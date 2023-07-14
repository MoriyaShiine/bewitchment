/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.client.screen.BaphometScreenHandler;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class BWScreenHandlerTypes {
	public static final ScreenHandlerType<DemonScreenHandler> DEMON_SCREEN_HANDLER = new ScreenHandlerType<>((syncId, playerInventory) -> new DemonScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_FEATURES);
	public static final ScreenHandlerType<BaphometScreenHandler> BAPHOMET_SCREEN_HANDLER = new ScreenHandlerType<>((syncId, playerInventory) -> new BaphometScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_FEATURES);

	public static void init() {
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(Bewitchment.MODID, "demon_screen"), DEMON_SCREEN_HANDLER);
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(Bewitchment.MODID, "baphomet_screen"), BAPHOMET_SCREEN_HANDLER);
	}
}
