package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ElderBroomEntity extends BroomEntity {
	private BlockPos originalPos = null;
	private String originalWorld = null;
	
	public ElderBroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		readFromTag(tag);
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		writeToTag(stack.getOrCreateTag());
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		originalPos = getBlockPos();
		originalWorld = world.getRegistryKey().toString();
	}
	
	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		Vec3d value = super.updatePassengerForDismount(passenger);
		if (originalPos != null && world.getRegistryKey().toString().equals(originalWorld)) {
			double x = originalPos.getX() + 0.5;
			double y = originalPos.getY();
			double z = originalPos.getZ() + 0.5;
			value = new Vec3d(x, y, z);
			BWUtil.teleport(this, x, y, z, false);
			originalPos = null;
			originalWorld = null;
		}
		return value;
	}
	
	private void readFromTag(CompoundTag tag) {
		if (tag.contains("OriginalPos")) {
			originalPos = BlockPos.fromLong(tag.getLong("OriginalPos"));
			originalWorld = tag.getString("OriginalWorld");
		}
	}
	
	private void writeToTag(CompoundTag tag) {
		if (originalPos != null) {
			tag.putLong("OriginalPos", originalPos.asLong());
			tag.putString("OriginalWorld", originalWorld);
		}
	}
}
