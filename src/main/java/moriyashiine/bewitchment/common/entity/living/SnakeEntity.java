package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class SnakeEntity extends BWTameableEntity {
	public float curve = 0;
	public byte tongueFlick = 0, attackTick = 0;
	
	public SnakeEntity(EntityType<? extends TameableEntity> type, World world) {
		super(type, world);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
	}
	
	@Override
	public void tick() {
		super.tick();
		double length = getVelocity().length() * 3;
		if (submergedInWater || length > 0.25) {
			curve += length;
		}
		if (tongueFlick > 0) {
			tongueFlick--;
		}
		if (age % 40 == 0 && random.nextFloat() < 0.25) {
			tongueFlick = 10;
		}
		if (attackTick > 0) {
			attackTick--;
			tongueFlick = 0;
		}
	}
	
	@Override
	public byte getVariants() {
		return 5;
	}
	
	@Override
	protected boolean hasShiny() {
		return true;
	}
	
	@Override
	public PassiveEntity createChild(PassiveEntity mate) {
		SnakeEntity child = BWEntityTypes.snake.create(world);
		if (child != null && mate instanceof SnakeEntity) {
			child.dataTracker.set(VARIANT, random.nextBoolean() ? dataTracker.get(VARIANT) : mate.getDataTracker().get(VARIANT));
		}
		return null;
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.CHICKEN;
	}
	
	@Override
	protected boolean isTamingItem(ItemStack stack) {
		return stack.getItem() == Items.RABBIT;
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		boolean flag = super.tryAttack(target);
		if (flag) {
			toggleAttack(false);
			if (target instanceof LivingEntity) {
				((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
			}
		}
		return flag;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void handleStatus(byte id) {
		if (id == 4) {
			attackTick = 11;
		}
		if (id == 5) {
			attackTick = 2;
		}
		else {
			super.handleStatus(id);
		}
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		curve = tag.getFloat("Curve");
		tongueFlick = tag.getByte("TongueFlick");
		attackTick = tag.getByte("AttackTick");
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putFloat("Curve", curve);
		tag.putByte("TongueFlick", tongueFlick);
		tag.putByte("AttackTick", attackTick);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		EntityAttributeInstance maxHealth = getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		EntityAttributeInstance attackDamage = getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		if (tamed) {
			maxHealth.setBaseValue(20);
			attackDamage.setBaseValue(3);
			setHealth(getMaxHealth());
		}
		else {
			maxHealth.setBaseValue(8);
			attackDamage.setBaseValue(1.5);
		}
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(1, new SwimGoal(this));
		goalSelector.add(2, new SitGoal(this));
		goalSelector.add(3, new SnakePounceAtTargetGoal(this, 0.25f));
		goalSelector.add(4, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(5, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.add(6, new AnimalMateGoal(this, 1));
		goalSelector.add(7, new WanderAroundFarGoal(this, 1));
		goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(8, new LookAroundGoal(this));
		targetSelector.add(1, new TrackOwnerAttackerGoal(this));
		targetSelector.add(2, new AttackWithOwnerGoal(this));
		targetSelector.add(3, new RevengeGoal(this));
		targetSelector.add(4, new FollowTargetIfTamedGoal<>(this, LivingEntity.class, false, entity -> entity instanceof ChickenEntity || entity instanceof RabbitEntity));
	}
	
	private void toggleAttack(boolean attacking) {
		if (attacking) {
			attackTick = 11;
			world.sendEntityStatus(this, (byte) 4);
		}
		else {
			attackTick = 2;
			world.sendEntityStatus(this, (byte) 5);
		}
	}
	
	private static class SnakePounceAtTargetGoal extends Goal {
		private final SnakeEntity mob;
		private final float velocity;
		private LivingEntity target;
		
		public SnakePounceAtTargetGoal(SnakeEntity mob, float velocity) {
			this.mob = mob;
			this.velocity = velocity;
			setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
		}
		
		@Override
		public boolean canStart() {
			if (mob.hasPassengers()) {
				return false;
			}
			else {
				target = mob.getTarget();
				if (target == null) {
					return false;
				}
				else {
					double d = mob.squaredDistanceTo(target);
					if (d >= 4.0D && d <= 16.0D) {
						if (!mob.isOnGround()) {
							return false;
						}
						else {
							return mob.getRandom().nextInt(5) == 0;
						}
					}
					else {
						return false;
					}
				}
			}
		}
		
		@Override
		public boolean shouldContinue() {
			return !mob.isOnGround();
		}
		
		@Override
		public void start() {
			Vec3d motion = mob.getVelocity();
			Vec3d targetMotion = new Vec3d(target.getX() - mob.getX(), 0, target.getZ() - mob.getZ());
			if (targetMotion.lengthSquared() > 0) {
				targetMotion = targetMotion.normalize().multiply(0.4).add(motion.multiply(0.2));
			}
			mob.setVelocity(targetMotion.x, velocity, targetMotion.z);
			mob.toggleAttack(true);
		}
	}
}