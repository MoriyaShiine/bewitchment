package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
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
public class SyncPoppetShelfBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_poppet_shelf_block_entity");
	
	public static void send(PlayerEntity player, PoppetShelfBlockEntity blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(blockEntity.getPos().asLong());
		buf.writeItemStack(blockEntity.getStack(0));
		buf.writeItemStack(blockEntity.getStack(1));
		buf.writeItemStack(blockEntity.getStack(2));
		buf.writeItemStack(blockEntity.getStack(3));
		buf.writeItemStack(blockEntity.getStack(4));
		buf.writeItemStack(blockEntity.getStack(5));
		buf.writeItemStack(blockEntity.getStack(6));
		buf.writeItemStack(blockEntity.getStack(7));
		buf.writeItemStack(blockEntity.getStack(8));
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		ItemStack one = buf.readItemStack();
		ItemStack two = buf.readItemStack();
		ItemStack three = buf.readItemStack();
		ItemStack four = buf.readItemStack();
		ItemStack five = buf.readItemStack();
		ItemStack six = buf.readItemStack();
		ItemStack seven = buf.readItemStack();
		ItemStack eight = buf.readItemStack();
		ItemStack nine = buf.readItemStack();
		client.execute(() -> {
			BlockEntity blockEntity = client.world.getBlockEntity(pos);
			if (blockEntity instanceof PoppetShelfBlockEntity poppetShelf) {
				poppetShelf.setStack(0, one);
				poppetShelf.setStack(1, two);
				poppetShelf.setStack(2, three);
				poppetShelf.setStack(3, four);
				poppetShelf.setStack(4, five);
				poppetShelf.setStack(5, six);
				poppetShelf.setStack(6, seven);
				poppetShelf.setStack(7, eight);
				poppetShelf.setStack(8, nine);
			}
		});
	}
}
