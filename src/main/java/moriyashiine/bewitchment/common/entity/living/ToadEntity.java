package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ToadEntity extends BWTameableEntity {
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
		if (child != null && entity instanceof ToadEntity) {
			child.dataTracker.set(VARIANT, random.nextBoolean() ? dataTracker.get(VARIANT) : entity.getDataTracker().get(VARIANT));
		}
		return child;
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.SPIDER_EYE;
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
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
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new SitGoal(this));
		goalSelector.add(2, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.add(3, new AnimalMateGoal(this, 1));
		goalSelector.add(4, new WanderAroundFarGoal(this, 1));
		goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(5, new LookAroundGoal(this));
	}
}
