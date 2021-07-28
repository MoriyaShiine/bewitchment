package moriyashiine.bewitchment.common.entity.living.util;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BWHostileEntity extends HostileEntity {
	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(BWHostileEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	protected BWHostileEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		int variants = getVariants();
		if (variants > 1) {
			if (hasShiny()) {
				if (random.nextInt(8192) == 0) {
					dataTracker.set(VARIANT, 0);
				}
				else {
					dataTracker.set(VARIANT, random.nextInt(variants - 1) + 1);
				}
			}
			else {
				dataTracker.set(VARIANT, random.nextInt(variants));
			}
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		if (getVariants() > 1) {
			dataTracker.startTracking(VARIANT, 1);
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (getVariants() > 1) {
			dataTracker.set(VARIANT, nbt.getInt("Variant"));
		}
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (getVariants() > 1) {
			nbt.putInt("Variant", dataTracker.get(VARIANT));
		}
	}
	
	protected abstract boolean hasShiny();
	
	public abstract int getVariants();
}
