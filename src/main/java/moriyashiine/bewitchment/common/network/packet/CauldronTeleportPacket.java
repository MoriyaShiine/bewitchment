package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class CauldronTeleportPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "cauldron_teleport");
	
	public static void send(BlockPos cauldronPos, String message) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(cauldronPos.asLong());
		buf.writeString(message);
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos cauldronPos = BlockPos.fromLong(buf.readLong());
		String message = buf.readString();
		server.execute(() -> {
			World world = player.world;
			BWWorldState worldState = BWWorldState.get(world);
			BlockPos closest = null;
			for (long longPos : worldState.witchCauldrons) {
				BlockPos pos = BlockPos.fromLong(longPos);
				WitchCauldronBlockEntity blockEntity = (WitchCauldronBlockEntity) world.getBlockEntity(pos);
				if (blockEntity.hasCustomName() && blockEntity.getCustomName().asString().equals(message) && (closest == null || pos.getSquaredDistance(player.getPos(), true) < closest.getSquaredDistance(player.getPos(), true))) {
					closest = pos;
				}
			}
			if (closest != null) {
				boolean pledgedToLeonard = BewitchmentAPI.isPledged(world, BWPledges.LEONARD_UUID, player.getUuid());
				boolean hasPower = false;
				if (!pledgedToLeonard) {
					BlockPos altarPos = ((UsesAltarPower) world.getBlockEntity(cauldronPos)).getAltarPos();
					if (altarPos != null) {
						WitchAltarBlockEntity altar = (WitchAltarBlockEntity) world.getBlockEntity(altarPos);
						if (altar != null && altar.drain((int) (Math.sqrt(closest.getSquaredDistance(player.getPos(), true)) * 2), false)) {
							hasPower = true;
						}
					}
				}
				if (pledgedToLeonard || hasPower) {
					BewitchmentAPI.teleport(player, closest.getX() + 0.5, closest.getY() - 0.5, closest.getZ() + 0.5, true);
				}
				else {
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".insufficent_altar_power", message), true);
				}
			}
			else {
				player.sendMessage(new TranslatableText(Bewitchment.MODID + ".invalid_cauldron", message), true);
			}
		});
	}
}
