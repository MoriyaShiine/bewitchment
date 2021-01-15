package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.registry.BWParticleTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class SpawnBrazierParticlesPacket  {
	 public static Identifier ID = new Identifier(Bewitchment.MODID, "brazier_particles");

	 public static void send(PlayerEntity player, BrazierBlockEntity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(entity.incenseRecipe != null);
		buf.writeBoolean(entity.getCachedState().get(Properties.HANGING));
		buf.writeBlockPos(entity.getPos());
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		boolean incense = buf.readBoolean();
		boolean hanging = buf.readBoolean();
		BlockPos pos = buf.readBlockPos();

		client.execute(() -> client.world.addParticle(incense ? (ParticleEffect) BWParticleTypes.INCENSE_SMOKE : ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5 + MathHelper.nextDouble(client.world.random, -0.2, 0.2), pos.getY() + (hanging ? 0.4 : 1.25), pos.getZ() + 0.5 + MathHelper.nextDouble(client.world.random, -0.2, 0.2), 0, 0.05, 0));
	}
}
