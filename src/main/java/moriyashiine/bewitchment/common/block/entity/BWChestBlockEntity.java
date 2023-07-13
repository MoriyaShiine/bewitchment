/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BWChestBlockEntity extends ChestBlockEntity {
	public final Type type;
	public final boolean trapped;

	public BWChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, Type type, boolean trapped) {
		super(blockEntityType, blockPos, blockState);
		this.type = type;
		this.trapped = trapped;
	}

	public BWChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(BWBlockEntityTypes.BW_CHEST, blockPos, blockState, Type.JUNIPER, false);
	}

	@Override
	protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		super.onViewerCountUpdate(world, pos, state, oldViewerCount, newViewerCount);
		if (trapped && world != null) {
			world.updateNeighborsAlways(pos.down(), getCachedState().getBlock());
		}
	}

	public enum Type {
		JUNIPER, CYPRESS, ELDER, DRAGONS_BLOOD
	}
}
