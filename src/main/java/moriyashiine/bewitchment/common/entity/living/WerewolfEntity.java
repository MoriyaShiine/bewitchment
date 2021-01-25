package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.DespawnAccessor;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WerewolfEntity extends BWHostileEntity {
	private boolean despawns = false;
	
	public WerewolfEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && despawns && age % 20 == 0 && BewitchmentAPI.getMoonPhase(world) != 0) {
			VillagerEntity entity = EntityType.VILLAGER.create(world);
			if (entity != null) {
				entity.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
				DespawnAccessor.of(entity).ifPresent(despawnAccessor -> despawnAccessor.setDespawnTimer(1200));
				world.spawnEntity(entity);
				remove();
			}
		}
	}
	
	@Override
	protected boolean hasShiny() {
		return true;
	}
	
	@Override
	public int getVariants() {
		return 7;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_WEREWOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_DEATH;
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (!VampireEntity.isEffective(source, false)) {
			amount /= 6;
		}
		return super.damage(source, amount);
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (dataTracker.get(VARIANT) != 0) {
			switch (world.getBiome(getBlockPos()).getCategory()) {
				case FOREST:
					dataTracker.set(VARIANT, random.nextBoolean() ? 1 : 2);
					break;
				case TAIGA:
					dataTracker.set(VARIANT, random.nextBoolean() ? 3 : 4);
					break;
				case ICY:
					dataTracker.set(VARIANT, random.nextBoolean() ? 5 : 6);
					break;
				default:
					dataTracker.set(VARIANT, random.nextInt(getVariants() - 1) + 1);
					break;
			}
		}
		despawns = spawnReason == SpawnReason.NATURAL;
		return data;
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		despawns = tag.getBoolean("Despawns");
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("Despawns", despawns);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));
	}
	
	public static boolean canSpawn(EntityType<WerewolfEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && world.getLevelProperties().getTime() > 24000 && BewitchmentAPI.getMoonPhase(world) == 0;
	}
}
