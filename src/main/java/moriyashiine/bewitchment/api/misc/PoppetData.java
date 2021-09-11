package moriyashiine.bewitchment.api.misc;

import moriyashiine.bewitchment.client.network.packet.SyncPoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

public class PoppetData {
	public final ItemStack stack;
	public final Long longPos;
	public final Integer index;
	
	public PoppetData(ItemStack stack, Long longPos, Integer index) {
		this.stack = stack;
		this.longPos = longPos;
		this.index = index;
	}
	
	public void maybeSync(World world, boolean sync) {
		if (longPos != null) {
			BWWorldState worldState = BWWorldState.get(world);
			for (int i = worldState.poppetShelves.get(longPos).size() - 1; i >= 0; i--) {
				if (i == index) {
					worldState.poppetShelves.get(longPos).set(i, stack);
					boolean empty = true;
					for (ItemStack stack : worldState.poppetShelves.get(longPos)) {
						if (!stack.isEmpty()) {
							empty = false;
							break;
						}
					}
					if (empty) {
						worldState.poppetShelves.remove(longPos);
					}
					worldState.markDirty();
					if (sync) {
						BlockPos pos = BlockPos.fromLong(longPos);
						Collection<ServerPlayerEntity> trackingPlayers = PlayerLookup.tracking((ServerWorld) world, pos);
						if (!trackingPlayers.isEmpty()) {
							BlockEntity blockEntity = world.getBlockEntity(pos);
							if (blockEntity instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
								trackingPlayers.forEach(trackingPlayer -> SyncPoppetShelfBlockEntity.send(trackingPlayer, poppetShelfBlockEntity));
							}
						}
					}
				}
			}
		}
	}
}
