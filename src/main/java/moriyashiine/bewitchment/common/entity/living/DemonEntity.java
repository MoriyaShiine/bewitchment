package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DemonEntity extends BWHostileEntity {
	public static final TrackedData<Boolean> MALE = DataTracker.registerData(DemonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	public DemonEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0);
		setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0);
		experiencePoints = 20;
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6).add(EntityAttributes.GENERIC_ARMOR, 4).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			LivingEntity target = getTarget();
			if (target != null) {
				lookAtEntity(target, 360, 360);
				if ((age + getEntityId()) % 40 == 0) {
					SmallFireballEntity fireball = new SmallFireballEntity(world, this, target.getX() - getX(), target.getBodyY(0.5) - getBodyY(0.5), target.getZ() - getZ());
					fireball.updatePosition(fireball.getX(), getBodyY(0.5), fireball.getZ());
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_SHOOT, getSoundCategory(), getSoundVolume(), getSoundPitch());
					world.spawnEntity(fireball);
					swingHand(Hand.MAIN_HAND);
				}
			}
		}
	}
	
	@Override
	protected boolean hasShiny() {
		return true;
	}
	
	@Override
	public int getVariants() {
		return 5;
	}
	
	@Override
	public EntityGroup getGroup() {
		return BewitchmentAPI.DEMON;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_DEMON_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_DEMON_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_DEMON_DEATH;
	}
	
	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}
	
	@Override
	public boolean cannotDespawn() {
		return true;
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		boolean flag = super.tryAttack(target);
		if (flag && target instanceof LivingEntity) {
			target.setOnFireFor(6);
			target.addVelocity(0, 0.2, 0);
			swingHand(Hand.MAIN_HAND);
		}
		return flag;
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
		dataTracker.set(MALE, random.nextBoolean());
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		dataTracker.set(MALE, tag.getBoolean("Male"));
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("Male", dataTracker.get(MALE));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(MALE, true);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> BWUtil.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3 && (entity.getGroup() != BewitchmentAPI.DEMON || entity instanceof PlayerEntity)));
	}
}
