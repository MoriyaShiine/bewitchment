package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.block.CandelabraBlock;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

public class GhostEntity extends BWHostileEntity {
	public static final TrackedData<Boolean> HAS_TARGET = DataTracker.registerData(GhostEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	public GhostEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		moveControl = new GhostMoveControl(this);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8);
	}
	
	public static boolean canSpawn(EntityType<GhostEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return findPos(world, pos) == null && MobEntity.canMobSpawn(type, world, spawnReason, pos, random);
	}
	
	@Override
	public void tick() {
		noClip = true;
		super.tick();
		noClip = false;
		setNoGravity(true);
		if (age % 20 == 0 && getTarget() != null) {
			int type = dataTracker.get(VARIANT);
			if (type == 0) {
				type = random.nextInt(getVariants() - 1) + 1;
			}
			getTarget().addStatusEffect(getEffect(type, 100));
		}
		if (!world.isClient && !hasCustomName() && world.isDay() && !world.isRaining() && world.isSkyVisibleAllowingSea(getBlockPos())) {
			PlayerLookup.tracking(this).forEach(playerEntity -> SpawnSmokeParticlesPacket.send(playerEntity, this));
			remove(RemovalReason.DISCARDED);
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
		return EntityGroup.UNDEAD;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_GHOST_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_GHOST_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_GHOST_DEATH;
	}
	
	@Override
	public float getBrightnessAtEyes() {
		return 1;
	}
	
	@Override
	public boolean canHaveStatusEffect(StatusEffectInstance effect) {
		return false;
	}
	
	@Override
	public boolean isTouchingWater() {
		return false;
	}
	
	@Override
	public boolean isInLava() {
		return false;
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target) {
		if (findPos((ServerWorldAccess) world, getBlockPos()) != null) {
			target = null;
		}
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
		goalSelector.add(1, new FlyRandomlyGoal());
		goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(1, new RevengeGoal(this));
		targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
	}
	
	public static StatusEffectInstance getEffect(int type, int duration) {
		return new StatusEffectInstance(type == 1 ? StatusEffects.HUNGER : type == 2 ? StatusEffects.SLOWNESS : type == 3 ? BWStatusEffects.CORROSION : BWStatusEffects.SINKING, duration);
	}
	
	private class GhostMoveControl extends MoveControl {
		public GhostMoveControl(MobEntity entity) {
			super(entity);
		}
		
		@Override
		public void tick() {
			if (state == MoveControl.State.MOVE_TO && (getTarget() == null || (!canSee(getTarget())))) {
				Vec3d targetPosition = new Vec3d(targetX - getX(), targetY - getY(), targetZ - getZ());
				double distance = targetPosition.length();
				if (distance < getBoundingBox().getAverageSideLength()) {
					state = MoveControl.State.WAIT;
					setVelocity(getVelocity().multiply(0.5));
				}
				else {
					setVelocity(getVelocity().add(targetPosition.multiply(speed * 0.05 / distance)));
					if (getTarget() == null) {
						Vec3d velocity = getVelocity();
						//noinspection SuspiciousNameCombination
						setYaw(-((float) MathHelper.atan2(velocity.x, velocity.z)) * 57);
					}
					else {
						setYaw(-((float) MathHelper.atan2(getTarget().getX() - getX(), getTarget().getZ() - getZ())) * 57);
					}
					bodyYaw = getYaw();
				}
			}
		}
	}
	
	private class FlyRandomlyGoal extends Goal {
		public FlyRandomlyGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}
		
		@Override
		public boolean canStart() {
			if (getTarget() == null || !canSee(getTarget())) {
				MoveControl moveControl = getMoveControl();
				if (!moveControl.isMoving()) {
					return true;
				}
				else {
					double distanceX = moveControl.getTargetX() - getX();
					double distanceY = moveControl.getTargetY() - getY();
					double distanceZ = moveControl.getTargetZ() - getZ();
					double fin = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
					return fin < 1 || fin > 3600;
				}
			}
			return false;
		}
		
		@Override
		public boolean shouldContinue() {
			return false;
		}
		
		@Override
		public void start() {
			BlockPos target = findTarget(new BlockPos.Mutable(getX() + MathHelper.nextDouble(random, -8, 8), getY() + MathHelper.nextDouble(random, -4, 4), getZ() + MathHelper.nextDouble(random, -8, 8)), 0);
			if (target != null) {
				getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 0.2);
				lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, new Vec3d(target.getX(), target.getY(), target.getZ()));
			}
		}
		
		private BlockPos findTarget(BlockPos.Mutable target, int tries) {
			if (tries <= 8) {
				while (!World.isValid(target) && world.getBlockState(target).getMaterial().isSolid()) {
					target.set(target.getX(), target.getY() + 1, target.getZ());
				}
				while (!World.isValid(target) && !world.getBlockState(target).getMaterial().isSolid()) {
					target.set(target.getX(), target.getY() - 1, target.getZ());
				}
				target.set(target.getX(), target.getY() + random.nextInt(8), target.getZ());
				Pair<BlockPos, Integer> validSpot = findPos((ServerWorldAccess) world, target);
				if (validSpot != null) {
					int radius = validSpot.getRight();
					return findTarget(target.set(validSpot.getLeft().getX() + MathHelper.nextDouble(random, -radius, radius), validSpot.getLeft().getY() + MathHelper.nextDouble(random, -radius, radius), validSpot.getLeft().getZ() + MathHelper.nextDouble(random, -radius, radius)), ++tries);
				}
				return target.toImmutable();
			}
			return null;
		}
	}
	
	private static Pair<BlockPos, Integer> findPos(ServerWorldAccess world, BlockPos pos) {
		for (Long longPos : BWWorldState.get(world.toServerWorld()).potentialCandelabras) {
			BlockPos candelabraPos = BlockPos.fromLong(longPos);
			double distance = Math.sqrt(candelabraPos.getSquaredDistance(pos));
			if (distance <= Byte.MAX_VALUE) {
				int radius = -1;
				BlockEntity blockEntity = world.getBlockEntity(candelabraPos);
				BlockState state = world.getBlockState(candelabraPos);
				if (blockEntity instanceof WitchAltarBlockEntity witchAltar) {
					for (int i = 0; i < witchAltar.size(); i++) {
						Block block = Block.getBlockFromItem(witchAltar.getStack(i).getItem());
						if (block instanceof CandelabraBlock) {
							radius = ((CandelabraBlock) block).repellentRadius;
							break;
						}
					}
				}
				else if (state.getBlock() instanceof CandelabraBlock && state.get(Properties.LIT)) {
					radius = ((CandelabraBlock) state.getBlock()).repellentRadius;
				}
				if (distance <= radius) {
					return new Pair<>(candelabraPos, radius);
				}
			}
		}
		return null;
	}
}
