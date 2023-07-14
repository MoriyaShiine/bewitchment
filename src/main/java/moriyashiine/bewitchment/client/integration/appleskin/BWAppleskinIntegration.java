/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.appleskin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.client.MinecraftClient;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.HUDOverlayEvent;

public class BWAppleskinIntegration implements AppleSkinApi {
	@Override
	public void registerEvents() {
		HUDOverlayEvent.Saturation.EVENT.register(saturation -> {
			if (BewitchmentAPI.isVampire(MinecraftClient.getInstance().player, true)) {
				saturation.isCanceled = true;
			}
		});
		HUDOverlayEvent.Exhaustion.EVENT.register(exhaustion -> {
			if (BewitchmentAPI.isVampire(MinecraftClient.getInstance().player, true)) {
				exhaustion.isCanceled = true;
			}
		});
	}
}
