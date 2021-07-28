package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SpawnPortalParticlesPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "spawn_portal_particles");
	
	public static void send(PlayerEntity player, Entity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int id = buf.readInt();
		client.execute(() -> {
			ClientWorld world = MinecraftClient.getInstance().world;
			if (world != null) {
				Entity entity = world.getEntityById(id);
				if (entity != null) {
					for (int i = 0; i < 32; i++) {
						world.addParticle(ParticleTypes.PORTAL, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
					}
				}
			}
		});
	}
}
