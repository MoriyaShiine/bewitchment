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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SnakeEntity extends BWTameableEntity {
	public float curve = 0;
	public int tongueFlick = 0, attackTick = 0;
	
	public SnakeEntity(EntityType<? extends TameableEntity> type, World world) {
		super(type, world);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
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
	protected boolean hasShiny() {
		return true;
	}
	
	@Override
	public int getVariants() {
		return 5;
	}
	
	@Override
	protected boolean isTamingItem(ItemStack stack) {
		return stack.getItem() == Items.RABBIT;
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		SnakeEntity child = BWEntityTypes.SNAKE.create(world);
		if (child != null && entity instanceof SnakeEntity) {
			child.dataTracker.set(VARIANT, random.nextBoolean() ? dataTracker.get(VARIANT) : entity.getDataTracker().get(VARIANT));
		}
		return child;
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.CHICKEN;
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
			attackDamage.setBaseValue(1);
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
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
		tongueFlick = tag.getInt("TongueFlick");
		attackTick = tag.getInt("AttackTick");
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putFloat("Curve", curve);
		tag.putInt("TongueFlick", tongueFlick);
		tag.putInt("AttackTick", attackTick);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new SitGoal(this));
		goalSelector.add(2, new PounceAtTargetGoal(this, 0.25f));
		goalSelector.add(3, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(4, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.add(5, new AnimalMateGoal(this, 1));
		goalSelector.add(6, new WanderAroundFarGoal(this, 1));
		goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(7, new LookAroundGoal(this));
		targetSelector.add(0, new TrackOwnerAttackerGoal(this));
		targetSelector.add(1, new AttackWithOwnerGoal(this));
		targetSelector.add(2, new RevengeGoal(this));
		targetSelector.add(3, new FollowTargetIfTamedGoal<>(this, LivingEntity.class, false, entity -> entity instanceof ChickenEntity || entity instanceof RabbitEntity));
	}
	
	public void toggleAttack(boolean attacking) {
		if (attacking) {
			attackTick = 11;
			world.sendEntityStatus(this, (byte) 4);
		}
		else {
			attackTick = 2;
			world.sendEntityStatus(this, (byte) 5);
		}
	}
}
