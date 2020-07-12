package moriyashiine.bewitchment.client.network.message;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SmokePuffMessage {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "smoke_puff");
	
	public static void send(PlayerEntity player, int entityId) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entityId);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		int entityId = buf.readInt();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				World world = MinecraftClient.getInstance().world;
				if (world != null) {
					Entity entity = world.getEntityById(entityId);
					if (entity != null) {
						for (int i = 0; i < 64; i++) {
							world.addParticle(ParticleTypes.SMOKE, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
						}
					}
				}
			}
		});
	}
}