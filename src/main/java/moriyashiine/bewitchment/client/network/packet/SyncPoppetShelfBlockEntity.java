package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ConstantConditions")
public class SyncPoppetShelfBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_poppet_shelf_block_entity");
	
	public static void send(PlayerEntity player, PoppetShelfBlockEntity blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(blockEntity.getPos().asLong());
		BWWorldState worldState = BWWorldState.get(blockEntity.getWorld());
		if (worldState.poppetShelves.get(blockEntity.getPos().asLong()) != null) {
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(0));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(1));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(2));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(3));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(4));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(5));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(6));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(7));
			buf.writeItemStack(worldState.poppetShelves.get(blockEntity.getPos().asLong()).get(8));
		}
		else {
			for (int i = 0; i < 9; i++) {
				buf.writeItemStack(ItemStack.EMPTY);
			}
		}
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
				poppetShelf.clientInventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
				poppetShelf.clientInventory.set(0, one);
				poppetShelf.clientInventory.set(1, two);
				poppetShelf.clientInventory.set(2, three);
				poppetShelf.clientInventory.set(3, four);
				poppetShelf.clientInventory.set(4, five);
				poppetShelf.clientInventory.set(5, six);
				poppetShelf.clientInventory.set(6, seven);
				poppetShelf.clientInventory.set(7, eight);
				poppetShelf.clientInventory.set(8, nine);
			}
		});
	}
}
