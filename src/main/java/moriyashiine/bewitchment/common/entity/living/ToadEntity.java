package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class ToadEntity extends BWTameableEntity {
	public boolean isFromWednesdayRitual = false;
	
	public ToadEntity(EntityType<? extends TameableEntity> type, World world) {
		super(type, world);
		setPathfindingPenalty(PathNodeType.WATER, -1);
		setPathfindingPenalty(PathNodeType.WATER_BORDER, -1);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (isNavigating() && onGround) {
			jump();
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
		return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		ToadEntity child = BWEntityTypes.TOAD.create(world);
		if (child != null) {
			child.initialize(world, world.getLocalDifficulty(getBlockPos()), SpawnReason.BREEDING, null, null);
			UUID owner = getOwnerUuid();
			if (owner != null) {
				child.setOwnerUuid(owner);
				child.setTamed(true);
			}
			if (entity instanceof ToadEntity && random.nextFloat() < 95 / 100f) {
				child.dataTracker.set(VARIANT, random.nextBoolean() ? dataTracker.get(VARIANT) : entity.getDataTracker().get(VARIANT));
			}
		}
		return child;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_TOAD_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_TOAD_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_TOAD_DEATH;
	}
	
	@Override
	protected boolean shouldDropLoot() {
		return super.shouldDropLoot() && !isFromWednesdayRitual;
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.SPIDER_EYE;
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}
	
	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		EntityAttributeInstance maxHealth = getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		if (tamed) {
			maxHealth.setBaseValue(20);
			setHealth(getMaxHealth());
		}
		else {
			maxHealth.setBaseValue(8);
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		isFromWednesdayRitual = nbt.getBoolean("IsFromWednesdayRitual");
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("IsFromWednesdayRitual", isFromWednesdayRitual);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new SitGoal(this));
		goalSelector.add(2, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.add(3, new AnimalMateGoal(this, 1));
		goalSelector.add(4, new WanderAroundFarGoal(this, 1));
		goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(5, new LookAroundGoal(this));
	}
}
