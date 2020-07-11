package moriyashiine.bewitchment.client.network.message;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.FocalChalkBlockEntity;
import moriyashiine.bewitchment.common.recipe.Ritual;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SyncFocalChalkBlockEntityMessage {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_focal_chalk_tile_entity");
	
	public static void send(PlayerEntity player, BlockPos pos, Ritual ritual, int time) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		buf.writeString(ritual.getId().toString());
		buf.writeInt(time);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		long longPos = buf.readLong();
		String recipeName = buf.readString();
		int time = buf.readInt();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				World world = MinecraftClient.getInstance().world;
				if (world != null) {
					BlockEntity blockEntity = world.getBlockEntity(BlockPos.fromLong(longPos));
					if (blockEntity instanceof FocalChalkBlockEntity) {
						FocalChalkBlockEntity chalk = ((FocalChalkBlockEntity) blockEntity);
						chalk.setRitual((Ritual) world.getRecipeManager().method_30027(BWRecipeTypes.ritual_type).stream().filter(ritual -> ritual.getId().toString().equals(recipeName)).findFirst().orElse(null));
						chalk.time = time;
					}
				}
			}
		});
	}
}