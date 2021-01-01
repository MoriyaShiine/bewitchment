package moriyashiine.bewitchment.api.registry;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;

public class Contract {
	public void tick(LivingEntity target, boolean includeNegative) {
		ContractAccessor.of(target).ifPresent(contractAccessor -> {
			if (this == BWContracts.SLOTH && target.age % 20 == 0 && includeNegative) {
				if (target.isOnGround() && !target.isSprinting()) {
					target.heal(1);
				}
				if (includeNegative) {
					target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0, true, false));
				}
			}
			else if (this == BWContracts.PESTILENCE && includeNegative && target.age % 100 == 0) {
				target.world.getEntitiesByType(EntityType.VILLAGER, new Box(target.getBlockPos()).expand(8), villagerEntity -> true).forEach(villagerEntity -> {
					if (target.getRandom().nextFloat() < 1 / 40f) {
						ZombieVillagerEntity zombieVillager = villagerEntity.method_29243(EntityType.ZOMBIE_VILLAGER, false);
						if (zombieVillager != null) {
							zombieVillager.initialize((ServerWorldAccess) target.world, target.world.getLocalDifficulty(zombieVillager.getBlockPos()), SpawnReason.CONVERSION, new ZombieEntity.ZombieData(false, true), null);
							zombieVillager.setVillagerData(villagerEntity.getVillagerData());
							zombieVillager.setGossipData(villagerEntity.getGossip().serialize(NbtOps.INSTANCE).getValue());
							zombieVillager.setOfferData(villagerEntity.getOffers().toTag());
							zombieVillager.setXp(villagerEntity.getExperience());
							if (!villagerEntity.isSilent()) {
								target.world.syncWorldEvent(null, 1026, villagerEntity.getBlockPos(), 0);
							}
							villagerEntity.remove();
						}
					}
				});
			}
			else if (this == BWContracts.DEATH && target.damage(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE) && target.isDead()) {
				contractAccessor.removeContract(this);
			}
		});
	}
	
	public void finishUsing(LivingEntity user, boolean includeNegative) {
	}
	
	public static class Instance {
		public Contract contract;
		public int duration;
		
		public Instance(Contract contract, int duration) {
			
			this.contract = contract;
			this.duration = duration;
		}
	}
}
