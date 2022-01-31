/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class VampireEntity extends BWHostileEntity {
	public static final TrackedData<Boolean> HAS_TARGET = DataTracker.registerData(VampireEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private boolean onFireFromSun = false;

	public VampireEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5).add(EntityAttributes.GENERIC_ARMOR, 8).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35);
	}

	public static boolean canSpawn(EntityType<VampireEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.toServerWorld().isNight() && BewitchmentAPI.getMoonPhase(world.toServerWorld().toServerWorld()) == 4 && HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			BWComponents.BLOOD_COMPONENT.maybeGet(this).ifPresent(bloodComponent -> {
				if (getHealth() < getMaxHealth() && (age + getId()) % 40 == 0 && bloodComponent.getBlood() > 0) {
					heal(1);
					if (random.nextFloat() < 1 / 4f) {
						bloodComponent.drainBlood(1, false);
					}
				}
			});
			if (!hasCustomName() && world.isDay() && !world.isRaining() && world.isSkyVisible(getBlockPos())) {
				setOnFireFor(8);
				onFireFromSun = true;
			}
		}
	}

	@Override
	protected boolean hasShiny() {
		return true;
	}

	@Override
	public int getVariants() {
		return 2;
	}

	@Override
	public EntityGroup getGroup() {
		return EntityGroup.UNDEAD;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_VAMPIRE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_VAMPIRE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_VAMPIRE_DEATH;
	}

	@Override
	protected boolean shouldDropLoot() {
		return super.shouldDropLoot() && !onFireFromSun;
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		dataTracker.set(HAS_TARGET, target != null);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		onFireFromSun = nbt.getBoolean("OnFireFromSun");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("OnFireFromSun", onFireFromSun);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(HAS_TARGET, false);
	}

	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity instanceof PlayerEntity || entity instanceof MerchantEntity || entity.getGroup() == EntityGroup.ILLAGER));
	}
}
