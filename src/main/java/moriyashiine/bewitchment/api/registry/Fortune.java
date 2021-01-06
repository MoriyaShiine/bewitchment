package moriyashiine.bewitchment.api.registry;

import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.common.registry.BWFortunes;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Fortune {
	public final boolean positive;
	
	public Fortune(boolean positive) {
		this.positive = positive;
	}
	
	public boolean tick(ServerWorld world, PlayerEntity target) {
		if (this == BWFortunes.POWER) {
			if (target.getAttacker() != null) {
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600));
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600));
				return true;
			}
		}
		else if (this == BWFortunes.COURAGE) {
			if (target.getAttacker() != null) {
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1));
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1));
				return true;
			}
		}
		return false;
	}
	
	public boolean finish(ServerWorld world, PlayerEntity target) {
		if (this == BWFortunes.MERCHANT) {
			WanderingTraderEntity entity = EntityType.WANDERING_TRADER.create(world);
			if (entity != null) {
				for (int i = 0; i < 8; i++) {
					BlockPos pos = target.getBlockPos().add(MathHelper.nextInt(world.random, -3, 3), 0, MathHelper.nextInt(world.random, -3, 3));
					if (!world.getBlockState(pos).getMaterial().blocksMovement()) {
						entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
						entity.updatePositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, world.random.nextInt(360));
						world.spawnEntity(entity);
						break;
					}
				}
			}
		}
		else if (this == BWFortunes.WISDOM) {
			target.addExperienceLevels(30);
		}
		else if (this == BWFortunes.ILLNESS) {
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 1));
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 600, 1));
		}
		else if (this == BWFortunes.THUNDERBOLT) {
			LightningEntity entity = EntityType.LIGHTNING_BOLT.create(world);
			if (entity != null) {
				entity.updatePositionAndAngles(target.getX(), target.getY(), target.getZ(), 0, world.random.nextInt(360));
				world.spawnEntity(entity);
			}
		}
		else if (this == BWFortunes.CLUMSINESS) {
			return target.dropStack(target.getMainHandStack()) != null;
		}
		else if (this == BWFortunes.ENCOUNTER) {
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
						return false;
					}
				}
				return true;
			}
		}
		else if (this == BWFortunes.EXHAUSTED) {
			MagicAccessor magicAccessor = MagicAccessor.of(target).orElse(null);
			return magicAccessor == null || !magicAccessor.drainMagic(2500, false);
		}
		return false;
	}
	
	public static class Instance {
		public Fortune fortune;
		public int duration;
		
		public Instance(Fortune fortune, int duration) {
			
			this.fortune = fortune;
			this.duration = duration;
		}
	}
}
