/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity.interfaces;

import moriyashiine.bewitchment.common.block.entity.ElderChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.LockableBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public interface Lockable {
	List<UUID> getEntities();

	UUID getOwner();

	void setOwner(UUID owner);

	boolean getModeOnWhitelist();

	void setModeOnWhitelist(boolean modeOnWhitelist);

	boolean getLocked();

	void setLocked(boolean locked);

	default void fromNbtLockable(NbtCompound nbt) {
		NbtList entitiesList = nbt.getList("Entities", NbtType.STRING);
		for (int i = 0; i < entitiesList.size(); i++) {
			getEntities().add(UUID.fromString(entitiesList.getString(i)));
		}
		if (nbt.contains("Owner")) {
			setOwner(nbt.getUuid("Owner"));
		}
		setModeOnWhitelist(nbt.getBoolean("ModeOnWhitelist"));
		setLocked(nbt.getBoolean("Locked"));
	}

	default void toNbtLockable(NbtCompound nbt) {
		NbtList entitiesList = new NbtList();
		for (int i = 0; i < getEntities().size(); i++) {
			entitiesList.add(NbtString.of(getEntities().get(i).toString()));
		}
		nbt.put("Entities", entitiesList);
		if (getOwner() != null) {
			nbt.putUuid("Owner", getOwner());
		}
		nbt.putBoolean("ModeOnWhitelist", getModeOnWhitelist());
		nbt.putBoolean("Locked", getLocked());
	}

	default ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (user.getUuid().equals(getOwner())) {
			ItemStack stack = user.getStackInHand(hand);
			if (!getLocked()) {
				if (BWTags.SILVER_INGOTS.contains(stack.getItem())) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof Lockable lockable) {
						if (!world.isClient) {
							if (!(user instanceof PlayerEntity player && player.isCreative())) {
								stack.decrement(1);
							}
							lockable.setModeOnWhitelist(true);
							lockable.setLocked(true);
							syncLockable(blockEntity);
							blockEntity.markDirty();
						}
						return ActionResult.SUCCESS;
					}
				}
			}
		}
		if (getLocked() && !test(user)) {
			return ActionResult.FAIL;
		}
		return ActionResult.PASS;
	}

	default boolean test(Entity entity) {
		if (entity.getUuid().equals(getOwner())) {
			return true;
		} else if (getLocked()) {
			if (!getEntities().isEmpty()) {
				if (getModeOnWhitelist()) {
					return getEntities().contains(entity.getUuid());
				}
				return !getEntities().contains(entity.getUuid());
			}
			return false;
		}
		return true;
	}

	default void syncLockable(BlockEntity blockEntity) {
		if (blockEntity instanceof LockableBlockEntity lockableBlockEntity) {
			lockableBlockEntity.sync();
		} else if (blockEntity instanceof ElderChestBlockEntity elderChestBlockEntity) {
			elderChestBlockEntity.sync();
		}
	}

	static ActionResult onUse(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (world.getBlockEntity(pos) instanceof Lockable lockable) {
			if (lockable.test(user)) {
				return lockable.use(world, pos, user, hand);
			}
			return ActionResult.FAIL;
		}
		return ActionResult.PASS;
	}
}
