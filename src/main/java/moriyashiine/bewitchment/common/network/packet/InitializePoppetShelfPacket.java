package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.client.network.packet.SyncPoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class InitializePoppetShelfPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "initialize_poppet_shelf");
	
	public static void send(BlockPos pos) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = buf.readBlockPos();
		server.execute(() -> {
			BlockEntity blockEntity = player.world.getBlockEntity(pos);
			if (blockEntity instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
				PlayerLookup.tracking(poppetShelfBlockEntity).forEach(trackingPlayer -> SyncPoppetShelfBlockEntity.send(trackingPlayer, poppetShelfBlockEntity));
			}
		});
	}
}
