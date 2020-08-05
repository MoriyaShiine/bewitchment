package moriyashiine.bewitchment.api.interfaces;

import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MagicUser {
	default boolean drainMagic(World world, int amount, boolean simulate) {
		BlockPos altarPos = getAltarPos();
		if (altarPos != null) {
			BlockEntity blockEntity = world.getBlockEntity(altarPos);
			if (blockEntity instanceof WitchAltarBlockEntity) {
				boolean flag = ((WitchAltarBlockEntity) blockEntity).drainMagic(amount, simulate);
				if (flag && !simulate) {
					blockEntity.markDirty();
				}
				return flag;
			}
		}
		return false;
	}
	
	default void fromTagMagicUser(CompoundTag tag) {
		if (tag.contains("AltarPos")) {
			setAltarPos(BlockPos.fromLong(tag.getLong("AltarPos")));
		}
	}
	
	default void toTagMagicUser(CompoundTag tag) {
		if (getAltarPos() != null) {
			tag.putLong("AltarPos", getAltarPos().asLong());
		}
		else if (tag.contains("AltarPos")) {
			tag.remove("AltarPos");
		}
	}
	
	BlockPos getAltarPos();
	
	void setAltarPos(BlockPos altarPos);
}
