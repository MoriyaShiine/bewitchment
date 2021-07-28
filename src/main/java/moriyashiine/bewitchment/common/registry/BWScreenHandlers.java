package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.client.screen.BaphometScreenHandler;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class BWScreenHandlers {
	public static final ScreenHandlerType<DemonScreenHandler> DEMON_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Bewitchment.MODID, "demon_screen"), (syncId, inventory) -> new DemonScreenHandler(syncId));
	public static final ScreenHandlerType<DemonScreenHandler> BAPHOMET_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Bewitchment.MODID, "baphomet_screen"), (syncId, inventory) -> new BaphometScreenHandler(syncId));
	
}
