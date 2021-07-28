package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
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
public class SyncTaglockHolderBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_taglock_holder_block_entity");
	
	public static void send(PlayerEntity player, BlockEntity blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(blockEntity.getPos().asLong());
		buf.writeItemStack(((TaglockHolder) blockEntity).getTaglockInventory().get(0));
		buf.writeItemStack(((TaglockHolder) blockEntity).getTaglockInventory().get(1));
		buf.writeItemStack(((TaglockHolder) blockEntity).getTaglockInventory().get(2));
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		ItemStack one = buf.readItemStack();
		ItemStack two = buf.readItemStack();
		ItemStack three = buf.readItemStack();
		client.execute(() -> {
			BlockEntity blockEntity = client.world.getBlockEntity(pos);
			if (blockEntity instanceof TaglockHolder taglockHolder) {
				taglockHolder.getTaglockInventory().set(0, one);
				taglockHolder.getTaglockInventory().set(1, two);
				taglockHolder.getTaglockInventory().set(2, three);
			}
		});
	}
}
