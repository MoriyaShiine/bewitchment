package moriyashiine.bewitchment.common.block.entity.interfaces;

import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.world.ServerWorld;
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
	
	default void fromTagLockable(CompoundTag tag) {
		ListTag entities = tag.getList("Entities", NbtType.STRING);
		for (int i = 0; i < entities.size(); i++) {
			getEntities().add(UUID.fromString(entities.getString(i)));
		}
		if (tag.contains("Owner")) {
			setOwner(tag.getUuid("Owner"));
		}
		setModeOnWhitelist(tag.getBoolean("ModeOnWhitelist"));
		setLocked(tag.getBoolean("Locked"));
	}
	
	default void toTagLockable(CompoundTag tag) {
		ListTag entities = new ListTag();
		for (int i = 0; i < getEntities().size(); i++) {
			entities.add(StringTag.of(getEntities().get(i).toString()));
		}
		tag.put("Entities", entities);
		if (getOwner() != null) {
			tag.putUuid("Owner", getOwner());
		}
		tag.putBoolean("ModeOnWhitelist", getModeOnWhitelist());
		tag.putBoolean("Locked", getLocked());
	}
	
	default ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (user.getUuid().equals(getOwner())) {
			ItemStack stack = user.getStackInHand(hand);
			if (!getLocked()) {
				if (BWTags.SILVER_INGOTS.contains(stack.getItem())) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof Lockable) {
						if (!world.isClient) {
							if (!(user instanceof PlayerEntity && ((PlayerEntity) user).isCreative())) {
								stack.decrement(1);
							}
							((Lockable) blockEntity).setModeOnWhitelist(true);
							((Lockable) blockEntity).setLocked(true);
							syncLockable(world, blockEntity);
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
		}
		else if (getLocked()) {
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
	
	default void syncLockable(World world, BlockEntity blockEntity) {
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(blockEntity).forEach(playerEntity -> {
				if (blockEntity instanceof BlockEntityClientSerializable) {
					SyncClientSerializableBlockEntity.send(playerEntity, (BlockEntityClientSerializable) blockEntity);
				}
			});
		}
	}
	
	static ActionResult onUse(World world, BlockPos pos, LivingEntity user, Hand hand) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof Lockable) {
			if (((Lockable) blockEntity).test(user)) {
				return ((Lockable) blockEntity).use(world, pos, user, hand);
			}
			return ActionResult.FAIL;
		}
		return ActionResult.PASS;
	}
}
