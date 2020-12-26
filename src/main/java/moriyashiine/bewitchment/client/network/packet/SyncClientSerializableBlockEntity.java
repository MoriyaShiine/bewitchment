package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SyncClientSerializableBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_client_serializable_block_entity");
	
	public static void send(PlayerEntity player, BlockEntityClientSerializable blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		if (blockEntity instanceof BlockEntity) {
			buf.writeLong(((BlockEntity) blockEntity).getPos().asLong());
			buf.writeCompoundTag(blockEntity.toClientTag(new CompoundTag()));
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
		}
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		CompoundTag tag = buf.readCompoundTag();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				ClientWorld world = MinecraftClient.getInstance().world;
				if (world != null) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof BlockEntityClientSerializable) {
						BlockEntityClientSerializable clientSerializable = (BlockEntityClientSerializable) blockEntity;
						clientSerializable.fromClientTag(tag);
					}
				}
			}
		});
	}
}
