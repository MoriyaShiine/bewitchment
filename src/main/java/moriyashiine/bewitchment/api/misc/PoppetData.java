/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.misc;

import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record PoppetData(ItemStack stack, Long longPos, Integer index) {
	public static final PoppetData EMPTY = new PoppetData(ItemStack.EMPTY, null, null);

	public void update(World world, boolean sync) {
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
						BlockEntity blockEntity = world.getBlockEntity(pos);
						if (blockEntity instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
							poppetShelfBlockEntity.sync();
						}
					}
				}
			}
		}
	}
}
