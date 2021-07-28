package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ConstantConditions")
public class SyncBrazierBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_brazier_block_entity");
	
	public static void send(PlayerEntity player, BrazierBlockEntity blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(blockEntity.getPos().asLong());
		buf.writeItemStack(blockEntity.getStack(0));
		buf.writeItemStack(blockEntity.getStack(1));
		buf.writeItemStack(blockEntity.getStack(2));
		buf.writeItemStack(blockEntity.getStack(3));
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		ItemStack one = buf.readItemStack();
		ItemStack two = buf.readItemStack();
		ItemStack three = buf.readItemStack();
		ItemStack four = buf.readItemStack();
		client.execute(() -> {
			BlockEntity blockEntity = client.world.getBlockEntity(pos);
			if (blockEntity instanceof BrazierBlockEntity brazier) {
				brazier.setStack(0, one);
				brazier.setStack(1, two);
				brazier.setStack(2, three);
				brazier.setStack(3, four);
			}
		});
	}
}
