package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToadEntity extends BWTameableEntity {
	public ToadEntity(EntityType<? extends TameableEntity> type, World world) {
		super(type, world);
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
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
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
		ToadEntity child = BWEntityTypes.toad.create(world);
		if (child != null && mate instanceof ToadEntity) {
			child.dataTracker.set(VARIANT, random.nextBoolean() ? dataTracker.get(VARIANT) : mate.getDataTracker().get(VARIANT));
		}
		return null;
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.SPIDER_EYE;
	}
	
	@Override
	protected boolean isTamingItem(ItemStack stack) {
		return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
	}
	
	@SuppressWarnings("ConstantConditions")
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
	protected void initGoals() {
		goalSelector.add(1, new SwimGoal(this));
		goalSelector.add(2, new SitGoal(this));
		goalSelector.add(3, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.add(4, new AnimalMateGoal(this, 1));
		goalSelector.add(5, new WanderAroundFarGoal(this, 1));
		goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(6, new LookAroundGoal(this));
	}
}