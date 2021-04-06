package moriyashiine.bewitchment.common.contract;

import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;

public class PestilenceContract extends Contract {
	@Override
	public void tick(PlayerEntity target, boolean includeNegative) {
		if (includeNegative && target.age % 100 == 0) {
			target.world.getEntitiesByType(EntityType.VILLAGER, new Box(target.getBlockPos()).expand(4), villagerEntity -> true).forEach(villagerEntity -> {
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
	}
}
