package moriyashiine.bewitchment.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings("ConstantConditions")
public class SyncContractsPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "sync_contracts");
	
	public static void send(PlayerEntity player) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		CompoundTag contracts = new CompoundTag();
		contracts.put("Contracts", ((ContractAccessor) player).toTagContract());
		buf.writeCompoundTag(contracts);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}
	
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		CompoundTag contractsTag = buf.readCompoundTag();
		client.execute(() -> {
			if (client.player != null) {
				((ContractAccessor) client.player).getContracts().clear();
				ListTag contracts = contractsTag.getList("Contracts", NbtType.COMPOUND);
				for (int i = 0; i < contracts.size(); i++) {
					CompoundTag contract = contracts.getCompound(i);
					((ContractAccessor) client.player).addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contract.getString("Contract"))), contract.getInt("Duration"), contract.getInt("Cost")));
				}
			}
		});
	}
}
