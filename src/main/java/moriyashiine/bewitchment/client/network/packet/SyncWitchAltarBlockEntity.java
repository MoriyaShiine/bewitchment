package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ConstantConditions")
public class SyncWitchAltarBlockEntity {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_witch_altar_block_entity");
	
	public static void send(PlayerEntity player, WitchAltarBlockEntity blockEntity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(blockEntity.getPos().asLong());
		buf.writeItemStack(blockEntity.getStack(0));
		buf.writeItemStack(blockEntity.getStack(1));
		buf.writeItemStack(blockEntity.getStack(2));
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		ItemStack sword = buf.readItemStack();
		ItemStack pentacle = buf.readItemStack();
		ItemStack wand = buf.readItemStack();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				WitchAltarBlockEntity altar = (WitchAltarBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
				altar.setStack(0, sword);
				altar.setStack(1, pentacle);
				altar.setStack(2, wand);
			}
		});
	}
}
