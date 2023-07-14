/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings({"ConstantConditions", "Convert2Lambda"})
public class SyncContractsPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_contracts");

	public static void send(PlayerEntity player) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		NbtCompound contracts = new NbtCompound();
		contracts.put("Contracts", BWComponents.CONTRACTS_COMPONENT.get(player).toNbtContract());
		buf.writeNbt(contracts);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		NbtCompound contractsCompound = buf.readNbt();
		client.execute(new Runnable() {
			@Override
			public void run() {
				if (client.player != null) {
					BWComponents.CONTRACTS_COMPONENT.maybeGet(client.player).ifPresent(contractsComponent -> {
						contractsComponent.getContracts().clear();
						NbtList contractsList = contractsCompound.getList("Contracts", NbtType.COMPOUND);
						for (int i = 0; i < contractsList.size(); i++) {
							NbtCompound contractCompound = contractsList.getCompound(i);
							contractsComponent.addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contractCompound.getString("Contract"))), contractCompound.getInt("Duration"), contractCompound.getInt("Cost")));
						}
					});
				}
			}
		});
	}
}
