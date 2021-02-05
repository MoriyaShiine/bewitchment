package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MoveBroomPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "move_broom");
	
	public static void send(Entity broom) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(broom.getEntityId());
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int broomId = buf.readInt();
		server.execute(() -> {
			Entity broom = player.world.getEntityById(broomId);
			if (broom != null) {
				if (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.OWL || ((MagicAccessor) player).drainMagic(1, false)) {
					broom.setVelocity(broom.getVelocity().add(player.getRotationVector()));
					broom.velocityDirty = true;
				}
			}
		});
	}
}
