package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.client.network.packet.SyncPoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PoppetShelfBlockEntity extends BlockEntity {
	@Environment(EnvType.CLIENT)
	public DefaultedList<ItemStack> clientInventory = null;
	
	public PoppetShelfBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.POPPET_SHELF, pos, state);
	}
	
	public void onUse(World world, BlockPos pos, PlayerEntity player, Hand hand) {
		BWWorldState worldState = BWWorldState.get(world);
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof PoppetItem) {
			int firstEmpty = getFirstEmptySlot();
			if (firstEmpty != -1) {
				DefaultedList<ItemStack> inventory = worldState.poppetShelves.get(pos.asLong());
				inventory.set(firstEmpty, stack.split(1));
				worldState.markDirty();
				sync();
			}
		}
		else {
			DefaultedList<ItemStack> inventory = worldState.poppetShelves.get(pos.asLong());
			if (inventory != null) {
				ItemScatterer.spawn(world, pos, inventory);
				worldState.poppetShelves.remove(pos.asLong());
				worldState.markDirty();
				sync();
			}
		}
	}
	
	private int getFirstEmptySlot() {
		BWWorldState worldState = BWWorldState.get(world);
		DefaultedList<ItemStack> inventory = worldState.poppetShelves.get(pos.asLong());
		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				if (inventory.get(i).isEmpty()) {
					return i;
				}
			}
		}
		else {
			worldState.poppetShelves.put(pos.asLong(), DefaultedList.ofSize(9, ItemStack.EMPTY));
			worldState.markDirty();
			return 0;
		}
		return -1;
	}
	
	private void sync() {
		PlayerLookup.tracking(this).forEach(trackingPlayer -> SyncPoppetShelfBlockEntity.send(trackingPlayer, this));
	}
}
