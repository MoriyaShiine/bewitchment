/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TogglePressingForwardPacket {
	public static final Identifier ID = Bewitchment.id("toggle_pressing_forward");

	public static void send(boolean pressingForward) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(pressingForward);
		ClientPlayNetworking.send(ID, buf);
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			try { // attempt normal runtime of TogglePressingForward
				boolean pressingForward = buf.readBoolean();
				server.execute(() -> {
					if (pressingForward && BewitchmentAPI.getFamiliar(player) != BWEntityTypes.OWL) {
						if (!BewitchmentAPI.drainMagic(player, 1, true)) {
							return;
						}
						if (player.age % 60 == 0) {
							BewitchmentAPI.drainMagic(player, 1, false);
						}
					}
					BWComponents.BROOM_USER_COMPONENT.get(player).setPressingForward(pressingForward);
				});
			} catch (IndexOutOfBoundsException ignored) {
			} // Supress client crash when attempting to fly with 0 ME
			// TODO 2/28/2023 : This is only intended as a temporary fix! A better fix would be to change the way ME works so that instead of crashing on values below 0, it simply denies the action, allows ME to go negative, then resets ME to 0 since the action was not allowed // end TODO
		}
	}
}
