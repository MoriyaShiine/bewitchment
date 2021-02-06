package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class JuniperBroomEntity extends BroomEntity {
	private final DefaultedList<ItemStack> taglocks = DefaultedList.ofSize(3, ItemStack.EMPTY);
	
	public JuniperBroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		if (!world.isClient && passenger instanceof LivingEntity) {
			if (!passenger.getUuid().equals(getOwner())) {
				int firstEmpty = getFirstEmptyTaglock();
				if (firstEmpty > -1) {
					for (ItemStack taglock : taglocks) {
						if (passenger.getUuid().equals(TaglockItem.getTaglockUUID(taglock))) {
							return;
						}
					}
					TaglockItem.putTaglock(taglocks.get(firstEmpty), passenger);
				}
			}
		}
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		boolean client = world.isClient;
		if (player.isSneaking()) {
			if (!client && player.getUuid().equals(getOwner())) {
				ItemStack stack = player.getStackInHand(hand);
				if (stack.getItem() instanceof TaglockItem) {
					int firstEmpty = getFirstEmptySlot();
					if (firstEmpty > -1) {
						taglocks.set(firstEmpty, stack.split(1));
					}
				}
			}
			return ActionResult.success(client);
		}
		return super.interact(player, hand);
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		Inventories.fromTag(tag, taglocks);
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		Inventories.toTag(tag, taglocks);
	}
	
	@Override
	protected ItemStack getDroppedStack() {
		for (ItemStack stack : taglocks) {
			dropStack(stack);
		}
		return super.getDroppedStack();
	}
	
	private int getFirstEmptySlot() {
		for (int i = 0; i < taglocks.size(); i++) {
			if (taglocks.get(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}
	
	private int getFirstEmptyTaglock() {
		for (int i = 0; i < taglocks.size(); i++) {
			if (!taglocks.get(i).isEmpty() && !TaglockItem.hasTaglock(taglocks.get(i))) {
				return i;
			}
		}
		return -1;
	}
}
