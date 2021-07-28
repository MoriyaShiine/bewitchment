package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.misc.BWUtil;
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
		if (!pledgeable.world.isClient && pledgeable.world.getEntitiesByType(getMinionType(), new Box(pledgeable.getBlockPos()).expand(32), entity -> !entity.isRemoved() && pledgeable.getUuid().equals(((MasterAccessor) entity).getMasterUUID())).size() < 3) {
			for (int i = 0; i < MathHelper.nextInt(pledgeable.getRandom(), 2, 3); i++) {
				Entity entity = getMinionType().create(pledgeable.world);
				if (entity instanceof MobEntity mobEntity) {
					BWUtil.attemptTeleport(mobEntity, pledgeable.getBlockPos().up(), 3, true);
					mobEntity.initialize((ServerWorldAccess) pledgeable.world, pledgeable.world.getLocalDifficulty(pledgeable.getBlockPos()), SpawnReason.EVENT, null, null);
					mobEntity.setPitch(pledgeable.getRandom().nextFloat() * 360);
					mobEntity.setTarget(pledgeable.getTarget());
					((MasterAccessor) mobEntity).setMasterUUID(pledgeable.getUuid());
					mobEntity.setPersistent();
					getMinionBuffs().forEach(mobEntity::addStatusEffect);
					pledgeable.world.spawnEntity(mobEntity);
				}
			}
		}
	}
	
	default void fromNbtPledgeable(NbtCompound tag) {
		NbtList pledgedPlayerUUIDs = tag.getList("PledgedPlayerUUIDs", NbtType.COMPOUND);
		for (int i = 0; i < pledgedPlayerUUIDs.size(); i++) {
			getPledgedPlayerUUIDs().add(pledgedPlayerUUIDs.getCompound(i).getUuid("UUID"));
		}
		setTimeSinceLastAttack(tag.getInt("TimeSinceLastAttack"));
	}
	
	default void toNbtPledgeable(NbtCompound tag) {
		NbtList pledgedPlayerUUIDs = new NbtList();
		for (UUID uuid : getPledgedPlayerUUIDs()) {
			NbtCompound pledgedPlayerUUID = new NbtCompound();
			pledgedPlayerUUID.putUuid("UUID", uuid);
			pledgedPlayerUUIDs.add(pledgedPlayerUUID);
		}
		tag.put("PledgedPlayerUUIDs", pledgedPlayerUUIDs);
		tag.putInt("TimeSinceLastAttack", getTimeSinceLastAttack());
	}
}
