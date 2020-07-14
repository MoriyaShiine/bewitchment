package moriyashiine.bewitchment.client.network.message;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.DistilleryBlockEntity;
import moriyashiine.bewitchment.common.recipe.DistillingRecipe;
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

public class SyncDistillingRecipeMessage {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_distilling_recipe");
	
	public static void send(PlayerEntity player, BlockPos pos, DistillingRecipe recipe) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		buf.writeString(recipe.getId().toString());
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		long longPos = buf.readLong();
		String recipeName = buf.readString();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				World world = MinecraftClient.getInstance().world;
				if (world != null) {
					BlockEntity blockEntity = world.getBlockEntity(BlockPos.fromLong(longPos));
					if (blockEntity instanceof DistilleryBlockEntity) {
						((DistilleryBlockEntity) blockEntity).setRecipe(world.getRecipeManager().method_30027(BWRecipeTypes.distilling_type).stream().filter(recipe -> recipe.getId().toString().equals(recipeName)).findFirst().orElse(null));
					}
				}
			}
		});
	}
}