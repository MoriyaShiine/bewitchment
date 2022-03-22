/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.SpawnSettings;

import java.util.List;

public class InsanityCurse extends Curse {
	public InsanityCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target.age % 20 == 0 && target.getRandom().nextFloat() < 1 / 100f) {
			List<SpawnSettings.SpawnEntry> entries = target.world.getBiome(target.getBlockPos()).value().getSpawnSettings().getSpawnEntries(SpawnGroup.MONSTER).getEntries();
			if (entries.isEmpty()) {
				return;
			}
			Entity entity = null;
			int tries = 0;
			while (tries < 16) {
				Entity potentialSpawn = entries.get(target.getRandom().nextInt(entries.size())).type.create(target.world);
				if (potentialSpawn != null && !potentialSpawn.getType().isIn(BWTags.INSANITY_BLACKLIST)) {
					entity = potentialSpawn;
					break;
				}
				tries++;
			}
			if (entity instanceof MobEntity mob) {
				BWUtil.attemptTeleport(entity, target.getBlockPos(), 24, false);
				mob.initialize((ServerWorldAccess) target.world, target.world.getLocalDifficulty(target.getBlockPos()), SpawnReason.EVENT, null, null);
				BWComponents.FAKE_MOB_COMPONENT.get(mob).setTarget(target.getUuid());
				entity.setSilent(true);
				target.world.spawnEntity(entity);
			}
		}
	}
}
