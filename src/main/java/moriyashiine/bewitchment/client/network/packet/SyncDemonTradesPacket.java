package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

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
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int syncId = buf.readInt();
		List<DemonEntity.DemonTradeOffer> offers = DemonEntity.DemonTradeOffer.fromPacket(buf);
		int traderId = buf.readInt();
		boolean discount = buf.readBoolean();
		client.execute(() -> {
			if (client.player != null) {
				ScreenHandler screenHandler = client.player.currentScreenHandler;
				if (syncId == screenHandler.syncId && screenHandler instanceof DemonScreenHandler) {
					((DemonScreenHandler) screenHandler).demonMerchant.setOffersClientside(offers);
					((DemonScreenHandler) screenHandler).demonMerchant.setCurrentCustomer(client.player);
					((DemonScreenHandler) screenHandler).demonMerchant.setDemonTraderClientside((LivingEntity) client.world.getEntityById(traderId));
					((DemonScreenHandler) screenHandler).demonMerchant.setDiscountClientside(discount);
				}
			}
		});
	}
}
