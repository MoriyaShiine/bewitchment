package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
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
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class VampireEntity extends BWHostileEntity {
	public static final TrackedData<Boolean> HAS_TARGET = DataTracker.registerData(VampireEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	public VampireEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4).add(EntityAttributes.GENERIC_ARMOR, 8).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && !hasCustomName() && world.isDay() && !world.isRaining() && world.isSkyVisibleAllowingSea(getBlockPos())) {
			setOnFireFor(8);
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
	public boolean damage(DamageSource source, float amount) {
		if (source.isFire()) {
			source = BWDamageSources.SUN;
			amount *= 2;
		}
		if (!isEffective(source)) {
			if (getHealth() - amount < 1) {
				BloodAccessor bloodAccessor = BloodAccessor.of(this).orElse(null);
				if (bloodAccessor != null) {
					while (getHealth() - amount < 1 && bloodAccessor.getBlood() > 0) {
						amount--;
						bloodAccessor.drainBlood(1, false);
					}
				}
			}
		}
		return super.damage(source, amount);
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		dataTracker.set(HAS_TARGET, target != null);
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
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity instanceof PlayerEntity || entity instanceof MerchantEntity || entity.getGroup() == EntityGroup.ILLAGER));
	}
	
	public static boolean isEffective(DamageSource source) {
		if (source.isOutOfWorld()) {
			return true;
		}
		Entity attacker = source.getSource();
		if (attacker != null) {
			if (BWTags.BOSSES.contains(attacker.getType()) || attacker instanceof VampireEntity || attacker instanceof WerewolfEntity) {
				return true;
			}
			else if (attacker instanceof LivingEntity && EnchantmentHelper.getLevel(Enchantments.SMITE, ((LivingEntity) attacker).getMainHandStack()) > 0) {
				return true;
			}
		}
		return source == BWDamageSources.SUN || BewitchmentAPI.isSourceFromSilver(source);
	}
	
	public static boolean canSpawn(EntityType<VampireEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && BewitchmentAPI.getMoonPhase(world) == 4;
	}
}
