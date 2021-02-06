package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.BroomUserAccessor;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TogglePressingForwardAccessor {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "toggle_pressing_forward");
	
	public static void send(boolean pressingForward) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(pressingForward);
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		boolean pressingForward = buf.readBoolean();
		server.execute(() -> {
			if (pressingForward) {
				if (!player.isCreative() && BewitchmentAPI.getFamiliar(player) != BWEntityTypes.OWL) {
					if (player.hasStatusEffect(BWStatusEffects.INHIBITED) || !((MagicAccessor) player).drainMagic(1, true)) {
						return;
					}
				}
				if (player.age % 60 == 0) {
					((MagicAccessor) player).drainMagic(1, false);
				}
			}
			((BroomUserAccessor) player).setPressingForward(pressingForward);
		});
	}
}
