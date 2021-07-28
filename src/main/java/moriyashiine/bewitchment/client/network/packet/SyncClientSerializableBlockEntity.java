package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ConstantConditions")
public class SyncClientSerializableBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_client_serializable_block_entity");
	
	public static void send(PlayerEntity player, BlockEntityClientSerializable blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		if (blockEntity instanceof BlockEntity) {
			buf.writeLong(((BlockEntity) blockEntity).getPos().asLong());
			buf.writeNbt(blockEntity.toClientTag(new NbtCompound()));
			ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
		}
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		NbtCompound tag = buf.readNbt();
		client.execute(() -> {
			BlockEntity blockEntity = client.world.getBlockEntity(pos);
			if (blockEntity instanceof BlockEntityClientSerializable) {
				((BlockEntityClientSerializable) blockEntity).fromClientTag(tag);
			}
		});
	}
}
