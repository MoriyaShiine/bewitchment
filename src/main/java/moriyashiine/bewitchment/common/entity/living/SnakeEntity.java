package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class SnakeEntity extends BWTameableEntity {
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
		if (tongueFlick > 0) {
			tongueFlick--;
		}
		if (age % 40 == 0 && random.nextFloat() < 0.25f) {
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
		if (child != null) {
			child.initialize(world, world.getLocalDifficulty(getBlockPos()), SpawnReason.BREEDING, null, null);
			UUID owner = getOwnerUuid();
			if (owner != null) {
				child.setOwnerUuid(owner);
				child.setTamed(true);
			}
			if (entity instanceof SnakeEntity && random.nextFloat() < 95 / 100f) {
				child.dataTracker.set(VARIANT, random.nextBoolean() ? dataTracker.get(VARIANT) : entity.getDataTracker().get(VARIANT));
			}
		}
		return child;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_SNAKE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_SNAKE_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_SNAKE_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
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
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new SitGoal(this));
		goalSelector.add(2, new PounceAtTargetGoal(this, 0.25f) {
			@Override
			public void start() {
				super.start();
				toggleAttack(true);
			}
		});
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
