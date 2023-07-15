/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings({"ConstantConditions", "Convert2Lambda"})
public class SyncPoppetShelfPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MOD_ID, "sync_poppet_shelf");

	public static void send(PlayerEntity player, BlockPos pos) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		NbtCompound nbt = new NbtCompound();
		DefaultedList<ItemStack> inventory = BWWorldState.get(player.getWorld()).poppetShelves.get(pos.asLong());
		if (inventory != null) {
			Inventories.writeNbt(nbt, inventory);
		}
		buf.writeNbt(nbt);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
		NbtCompound nbt = buf.readNbt();
		if (!nbt.isEmpty()) {
			Inventories.readNbt(nbt, inventory);
		}
		client.execute(new Runnable() {
			@Override
			public void run() {
				if (client.world.getBlockEntity(pos) instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
					poppetShelfBlockEntity.clientInventory = inventory;
				}
			}
		});
	}
}
