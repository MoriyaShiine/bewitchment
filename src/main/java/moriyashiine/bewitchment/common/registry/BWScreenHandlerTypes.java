package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.screenhandler.DistilleryScreenHandler;
import moriyashiine.bewitchment.common.screenhandler.SpinningWheelScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class BWScreenHandlerTypes {
	public static final ScreenHandlerType<DistilleryScreenHandler> distillery = ScreenHandlerRegistry.registerExtended(new Identifier(Bewitchment.MODID, "distillery"), (i, playerInventory, packetByteBuf) -> new DistilleryScreenHandler(i, playerInventory, packetByteBuf.readBlockPos()));
	public static final ScreenHandlerType<SpinningWheelScreenHandler> spinning_wheel = ScreenHandlerRegistry.registerExtended(new Identifier(Bewitchment.MODID, "spinning_wheel"), (i, playerInventory, packetByteBuf) -> new SpinningWheelScreenHandler(i, playerInventory, packetByteBuf.readBlockPos()));
}