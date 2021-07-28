package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings("ConstantConditions")
public class SyncHornedSpearEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_horned_spear_entity");
	
	public static void send(PlayerEntity player, HornedSpearEntity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		buf.writeItemStack(entity.spear);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int entityId = buf.readInt();
		ItemStack spear = buf.readItemStack();
		client.execute(() -> ((HornedSpearEntity) client.world.getEntityById(entityId)).spear = spear);
	}
}
