package moriyashiine.bewitchment.client.network.message;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PlacedItemBlockEntity;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SyncPlacedItem {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_placed_item");
	
	public static void send(PlayerEntity player, BlockPos pos, ItemStack stack) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		buf.writeItemStack(stack);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		long longPos = buf.readLong();
		ItemStack stack = buf.readItemStack();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				World world = MinecraftClient.getInstance().world;
				if (world != null) {
					BlockEntity blockEntity = world.getBlockEntity(BlockPos.fromLong(longPos));
					if (blockEntity instanceof PlacedItemBlockEntity) {
						((PlacedItemBlockEntity) blockEntity).stack = stack;
					}
				}
			}
		});
	}
}