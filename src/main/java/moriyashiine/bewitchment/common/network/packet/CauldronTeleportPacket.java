package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
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
		buf.writeBlockPos(cauldronPos);
		buf.writeString(message);
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos cauldronPos = buf.readBlockPos();
		String message = buf.readString(Short.MAX_VALUE);
		server.execute(() -> {
			World world = player.world;
			BWWorldState worldState = BWWorldState.get(world);
			BlockPos closest = null;
			for (Long longPos : worldState.witchCauldrons) {
				BlockPos pos = BlockPos.fromLong(longPos);
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof WitchCauldronBlockEntity cauldron && cauldron.hasCustomName() && cauldron.getCustomName().getString().equals(message) && (closest == null || pos.getSquaredDistance(player.getPos(), true) < closest.getSquaredDistance(player.getPos(), true))) {
					closest = pos;
				}
			}
			if (closest != null) {
				boolean hasPower = BewitchmentAPI.isPledged(player, BWPledges.LEONARD);
				if (!hasPower) {
					BlockPos altarPos = ((UsesAltarPower) world.getBlockEntity(cauldronPos)).getAltarPos();
					if (altarPos != null) {
						BlockEntity altarBE = world.getBlockEntity(altarPos);
						if (altarBE instanceof WitchAltarBlockEntity altar && altar.drain((int) Math.sqrt(closest.getSquaredDistance(player.getPos(), true)) / 2, false)) {
							hasPower = true;
						}
					}
				}
				if (hasPower) {
					BWUtil.teleport(player, closest.getX() + 0.5, closest.getY() - 0.5, closest.getZ() + 0.5, true);
				}
				else {
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.insufficent_altar_power", message), true);
				}
			}
			else {
				player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.invalid_cauldron", message), true);
			}
		});
	}
}
