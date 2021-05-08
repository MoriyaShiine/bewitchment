package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings("ConstantConditions")
public class SyncDemonTradesPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_demon_trades");
	
	public static void send(PlayerEntity player, DemonMerchant merchant, int syncId) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		DemonEntity.DemonTradeOffer.toPacket(merchant.getOffers(), buf);
		buf.writeInt(merchant.getDemonTrader().getEntityId());
		buf.writeBoolean(merchant.isDiscount());
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	//@Environment notoriously fails on this specific method even when it's empty so here we are
}
