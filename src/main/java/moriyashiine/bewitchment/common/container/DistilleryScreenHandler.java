package moriyashiine.bewitchment.common.container;

import moriyashiine.bewitchment.common.block.entity.DistilleryBlockEntity;
import moriyashiine.bewitchment.common.registry.BWScreenHandlerTypes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class DistilleryScreenHandler extends ScreenHandler {
	public PropertyDelegate time;
	private DistilleryBlockEntity blockEntity;
	
	public DistilleryScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
		super(BWScreenHandlerTypes.distillery, syncId);
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(pos);
		if (blockEntity instanceof DistilleryBlockEntity) {
			this.blockEntity = (DistilleryBlockEntity) blockEntity;
			this.time = this.blockEntity.propertyDelegate;
			Inventory inventory = (Inventory) blockEntity;
			addSlot(new Slot(inventory, 0, 32, 26));
			addSlot(new Slot(inventory, 1, 32, 44));
			addSlot(new Slot(inventory, 2, 50, 26));
			addSlot(new Slot(inventory, 3, 50, 44));
			addSlot(new Slot(inventory, 4, 113, 34));
			for (int x = 0; x < 3; ++x) {
				for (int y = 0; y < 9; ++y) {
					addSlot(new Slot(playerInventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
				}
			}
			for (int h = 0; h < 9; ++h) {
				addSlot(new Slot(playerInventory, h, 8 + h * 18, 142));
			}
			addProperties(time);
		}
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack stack0 = slot.getStack();
			stack = stack0.copy();
			int containerSlots = slots.size() - player.inventory.main.size();
			if (index < containerSlots && !insertItem(stack0, containerSlots, slots.size(), true) || !insertItem(stack0, 0, containerSlots, false)) {
				stack = ItemStack.EMPTY;
			}
			if (stack0.getCount() == 0) {
				slot.setStack(ItemStack.EMPTY);
			}
			if (stack0.getCount() == stack.getCount()) {
				stack = ItemStack.EMPTY;
			}
			slot.onTakeItem(player, stack0);
		}
		return stack;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return blockEntity.canPlayerUse(player);
	}
}