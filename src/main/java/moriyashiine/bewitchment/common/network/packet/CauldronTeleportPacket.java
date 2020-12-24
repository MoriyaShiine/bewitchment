package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.client.network.packet.SpawnPortalParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronTeleportPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "cauldron_teleport");
	
	public static void send(String message) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(message);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		String message = buf.readString();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@Override
			public void run() {
				PlayerEntity player = context.getPlayer();
				World world = player.world;
				BWWorldState worldState = BWWorldState.get(world);
				for (long longPos : worldState.witchCauldrons) {
					BlockPos pos = BlockPos.fromLong(longPos);
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof WitchCauldronBlockEntity) {
						WitchCauldronBlockEntity cauldron = (WitchCauldronBlockEntity) blockEntity;
						//noinspection ConstantConditions
						if (cauldron.hasCustomName() && cauldron.getCustomName().asString().equals(message)) {
							world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
							PlayerStream.watching(player).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, player));
							SpawnPortalParticlesPacket.send(player, player);
							player.teleport(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
						}
					}
				}
			}
		});
	}
}
