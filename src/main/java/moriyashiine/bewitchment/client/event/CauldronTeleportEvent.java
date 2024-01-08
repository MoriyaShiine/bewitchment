package moriyashiine.bewitchment.client.event;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class CauldronTeleportEvent implements ClientSendMessageEvents.AllowChat {
	@Override
	public boolean allowSendChatMessage(String message) {
		if (!message.startsWith("/")) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			for (int i = 0; i < 1; i++) {
				if (player.getWorld().getBlockEntity(player.getBlockPos().down(i)) instanceof WitchCauldronBlockEntity witchCauldron && witchCauldron.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					moriyashiine.bewitchment.common.packet.CauldronTeleportPacket.send(witchCauldron.getPos(), message);
					return false;
				}
			}
		}
		return true;
	}
}
