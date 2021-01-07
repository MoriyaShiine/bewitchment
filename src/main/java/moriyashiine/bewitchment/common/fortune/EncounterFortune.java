package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EncounterFortune extends Fortune {
	public EncounterFortune(boolean positive) {
		super(positive);
	}
	
	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		Entity entity = BWTags.ENCOUNTER_FORTUNE.getRandom(world.random).create(world);
		if (entity != null) {
			for (int i = 0; i < 8; i++) {
				BlockPos pos = target.getBlockPos().add(MathHelper.nextInt(world.random, -3, 3), 0, MathHelper.nextInt(world.random, -3, 3));
				if (!world.getBlockState(pos).getMaterial().blocksMovement()) {
					if (entity instanceof MobEntity) {
						((MobEntity) entity).initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
						((MobEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
						((MobEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1));
						((MobEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, Integer.MAX_VALUE, 1));
						((MobEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 1));
					}
					entity.updatePositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, world.random.nextInt(360));
					world.spawnEntity(entity);
					return true;
				}
			}
		}
		return false;
	}
}
