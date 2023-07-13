/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.entity;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;

import java.util.Collection;
import java.util.UUID;

public interface Pledgeable {
	String getPledgeID();

	Collection<UUID> getPledgedPlayerUUIDs();

	EntityType<?> getMinionType();

	Collection<StatusEffectInstance> getMinionBuffs();

	int getTimeSinceLastAttack();

	void setTimeSinceLastAttack(int timeSinceLastAttack);

	default void summonMinions(MobEntity pledgeable) {
		if (!pledgeable.getWorld().isClient && pledgeable.getWorld().getEntitiesByType(getMinionType(), new Box(pledgeable.getBlockPos()).expand(32), entity -> entity instanceof MobEntity mobEntity && !entity.isRemoved() && pledgeable.getUuid().equals(BWComponents.MINION_COMPONENT.get(mobEntity).getMaster())).size() < 3) {
			for (int i = 0; i < MathHelper.nextInt(pledgeable.getRandom(), 2, 3); i++) {
				Entity entity = getMinionType().create(pledgeable.getWorld());
				if (entity instanceof MobEntity mobEntity) {
					BWUtil.attemptTeleport(entity, pledgeable.getBlockPos().up(), 3, true);
					mobEntity.initialize((ServerWorldAccess) pledgeable.getWorld(), pledgeable.getWorld().getLocalDifficulty(pledgeable.getBlockPos()), SpawnReason.EVENT, null, null);
					entity.setPitch(pledgeable.getRandom().nextFloat() * 360);
					mobEntity.setTarget(pledgeable.getTarget());
					BWComponents.MINION_COMPONENT.get(mobEntity).setMaster(pledgeable.getUuid());
					mobEntity.setPersistent();
					getMinionBuffs().forEach(mobEntity::addStatusEffect);
					pledgeable.getWorld().spawnEntity(entity);
				}
			}
		}
	}

	default void fromNbtPledgeable(NbtCompound tag) {
		NbtList pledgedPlayerUUIDsList = tag.getList("PledgedPlayerUUIDs", NbtType.COMPOUND);
		for (int i = 0; i < pledgedPlayerUUIDsList.size(); i++) {
			getPledgedPlayerUUIDs().add(pledgedPlayerUUIDsList.getCompound(i).getUuid("UUID"));
		}
		setTimeSinceLastAttack(tag.getInt("TimeSinceLastAttack"));
	}

	default void toNbtPledgeable(NbtCompound tag) {
		NbtList pledgedPlayerUUIDsList = new NbtList();
		for (UUID uuid : getPledgedPlayerUUIDs()) {
			NbtCompound pledgedPlayerUUIDCompound = new NbtCompound();
			pledgedPlayerUUIDCompound.putUuid("UUID", uuid);
			pledgedPlayerUUIDsList.add(pledgedPlayerUUIDCompound);
		}
		tag.put("PledgedPlayerUUIDs", pledgedPlayerUUIDsList);
		tag.putInt("TimeSinceLastAttack", getTimeSinceLastAttack());
	}
}
