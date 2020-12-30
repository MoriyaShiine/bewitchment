package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.client.network.packet.SpawnPortalParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronTeleportPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "cauldron_teleport");
	
	public static void send(BlockPos cauldronPos, String message) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(cauldronPos.asLong());
		buf.writeString(message);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, buf);
	}
	
	public static void handle(PacketContext context, PacketByteBuf buf) {
		BlockPos cauldronPos = BlockPos.fromLong(buf.readLong());
		String message = buf.readString();
		//noinspection Convert2Lambda
		context.getTaskQueue().submit(new Runnable() {
			@SuppressWarnings("ConstantConditions")
			@Override
			public void run() {
				PlayerEntity player = context.getPlayer();
				World world = player.world;
				BWWorldState worldState = BWWorldState.get(world);
				BlockPos closest = null;
				for (long longPos : worldState.witchCauldrons) {
					BlockPos pos = BlockPos.fromLong(longPos);
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof WitchCauldronBlockEntity) {
						WitchCauldronBlockEntity cauldron = (WitchCauldronBlockEntity) blockEntity;
						if (cauldron.hasCustomName() && cauldron.getCustomName().asString().equals(message) && (closest == null || pos.getSquaredDistance(player.getPos(), true) < closest.getSquaredDistance(player.getPos(), true))) {
							closest = pos;
						}
					}
				}
				if (closest != null) {
					BlockEntity blockEntity = world.getBlockEntity(closest);
					if (blockEntity instanceof WitchCauldronBlockEntity) {
						BlockPos altarPos = ((UsesAltarPower) world.getBlockEntity(cauldronPos)).getAltarPos();
						if (altarPos != null) {
							WitchAltarBlockEntity altar = (WitchAltarBlockEntity) world.getBlockEntity(altarPos);
							if (altar != null && altar.drain((int) (Math.sqrt(closest.getSquaredDistance(player.getPos(), true)) * 2), false)) {
								if (!player.isSilent()) {
									world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
								}
								PlayerStream.watching(player).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, player));
								SpawnPortalParticlesPacket.send(player, player);
								player.teleport(closest.getX() + 0.5, closest.getY() + 0.5, closest.getZ() + 0.5);
								if (!player.isSilent()) {
									world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
								}
								PlayerStream.watching(player).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, player));
								SpawnPortalParticlesPacket.send(player, player);
								return;
							}
						}
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".insufficent_altar_power"), true);
					}
				}
				else {
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".invalid_cauldron", message), true);
				}
			}
		});
	}
}
