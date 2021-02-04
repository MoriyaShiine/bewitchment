package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;

import java.util.Collection;

public interface Pledgeable {
	String getPledgeID();
	
	EntityType<?> getMinionType();
	
	Collection<StatusEffectInstance> getMinionBuffs();
	
	void setTimeSinceLastAttack(int timeSinceLastAttack);
	
	default void summonMinions(MobEntity pledgeableEntity) {
		if (!pledgeableEntity.world.isClient && pledgeableEntity.world.getEntitiesByType(getMinionType(), new Box(pledgeableEntity.getBlockPos()).expand(32), entity -> !entity.removed && pledgeableEntity.getUuid().equals(((MasterAccessor) entity).getMasterUUID())).size() < 3) {
			for (int i = 0; i < MathHelper.nextInt(pledgeableEntity.getRandom(), 2, 3); i++) {
				Entity entity = getMinionType().create(pledgeableEntity.world);
				if (entity instanceof MobEntity) {
					MobEntity mobEntity = (MobEntity) entity;
					BWUtil.attemptTeleport(mobEntity, pledgeableEntity.getBlockPos().up(), 3, true);
					mobEntity.initialize((ServerWorldAccess) pledgeableEntity.world, pledgeableEntity.world.getLocalDifficulty(pledgeableEntity.getBlockPos()), SpawnReason.EVENT, null, null);
					mobEntity.pitch = pledgeableEntity.getRandom().nextFloat() * 360;
					mobEntity.setTarget(pledgeableEntity.getTarget());
					((MasterAccessor) mobEntity).setMasterUUID(pledgeableEntity.getUuid());
					mobEntity.setPersistent();
					getMinionBuffs().forEach(mobEntity::addStatusEffect);
					pledgeableEntity.world.spawnEntity(mobEntity);
				}
			}
		}
	}
}
