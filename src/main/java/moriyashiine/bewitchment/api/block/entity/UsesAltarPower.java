/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.block.entity;

import net.minecraft.util.math.BlockPos;

public interface UsesAltarPower {
	BlockPos getAltarPos();

	void setAltarPos(BlockPos pos);
}
