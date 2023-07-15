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

public class BWScreenHandlerTypes {
	public static final ScreenHandlerType<DemonScreenHandler> DEMON_SCREEN_HANDLER = new ScreenHandlerType<>((syncId, playerInventory) -> new DemonScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_FEATURES);
	public static final ScreenHandlerType<BaphometScreenHandler> BAPHOMET_SCREEN_HANDLER = new ScreenHandlerType<>((syncId, playerInventory) -> new BaphometScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_FEATURES);

	public static void init() {
		Registry.register(Registries.SCREEN_HANDLER, Bewitchment.id("demon_screen"), DEMON_SCREEN_HANDLER);
		Registry.register(Registries.SCREEN_HANDLER, Bewitchment.id("baphomet_screen"), BAPHOMET_SCREEN_HANDLER);
	}
}
