package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SyncWitchAltarBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_witch_altar_block_entity");
	
	public static void send(PlayerEntity player, WitchAltarBlockEntity blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(blockEntity.getPos().asLong());
		buf.writeCompoundTag(blockEntity.getStack(0).toTag(new CompoundTag()));
		buf.writeCompoundTag(blockEntity.getStack(1).toTag(new CompoundTag()));
		buf.writeCompoundTag(blockEntity.getStack(2).toTag(new CompoundTag()));
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		ItemStack sword = ItemStack.fromTag(buf.readCompoundTag());
		ItemStack pentacle = ItemStack.fromTag(buf.readCompoundTag());
		ItemStack wand = ItemStack.fromTag(buf.readCompoundTag());
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				ClientWorld world = MinecraftClient.getInstance().world;
				if (world != null) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof WitchAltarBlockEntity) {
						WitchAltarBlockEntity altar = (WitchAltarBlockEntity) blockEntity;
						altar.setStack(0, sword);
						altar.setStack(1, pentacle);
						altar.setStack(2, wand);
					}
				}
			}
		});
	}
}
