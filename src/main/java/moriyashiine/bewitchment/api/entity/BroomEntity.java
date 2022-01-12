/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.entity;

import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BroomEntity extends Entity {
	public ItemStack stack = ItemStack.EMPTY;

	private float damage = 0;

	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;

	public BroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		stack = ItemStack.fromNbt(nbt.getCompound("Stack"));
		damage = nbt.getFloat("Damage");
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.put("Stack", stack.writeNbt(new NbtCompound()));
		nbt.putFloat("Damage", damage);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, 0);
	}

	@Override
	public void tick() {
		super.tick();
		if (isLogicalSideForUpdatingMovement()) {
			lerpSteps = 0;
			updateTrackedPosition(getX(), getY(), getZ());
		}
		if (lerpSteps > 0) {
			updatePosition(getX() + (lerpX - getX()) / lerpSteps, getY() + (lerpY - getY()) / lerpSteps, getZ() + (lerpZ - getZ()) / lerpSteps);
			setRotation((float) (getYaw() + MathHelper.wrapDegrees(lerpYaw - getYaw()) / lerpSteps), (float) (getPitch() + (lerpPitch - getPitch()) / lerpSteps));
			--lerpSteps;
		}
		if (isLogicalSideForUpdatingMovement()) {
			updateTrackedPosition(getX(), getY(), getZ());
			Entity passenger = getPrimaryPassenger();
			if (passenger instanceof PlayerEntity player) {
				setRotation(passenger.getYaw(), passenger.getPitch());
				if (BWComponents.BROOM_USER_COMPONENT.get(player).isPressingForward()) {
					addVelocity(passenger.getRotationVector().x / 8 * getSpeed(), passenger.getRotationVector().y / 8 * getSpeed(), passenger.getRotationVector().z / 8 * getSpeed());
					setVelocity(MathHelper.clamp(getVelocity().x, -getSpeed(), getSpeed()), MathHelper.clamp(getVelocity().y, -getSpeed(), getSpeed()), MathHelper.clamp(getVelocity().z, -getSpeed(), getSpeed()));
				}
			}
			move(MovementType.SELF, getVelocity());
			setVelocity(getVelocity().multiply(0.9));
		} else {
			setVelocity(Vec3d.ZERO);
		}
		if (!world.isClient && damage > 0) {
			damage -= 1 / 20f;
			damage = Math.max(damage, 0);
		}
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.shouldCancelInteraction()) {
			return ActionResult.PASS;
		} else if (!world.isClient) {
			return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
		}
		return super.interact(player, hand);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) {
			return false;
		}
		if (!world.isClient && !isRemoved()) {
			damage += amount;
			if (damage > 4) {
				dropStack(getDroppedStack());
				remove(RemovalReason.DISCARDED);
			}
		}
		return true;
	}

	@Nullable
	@Override
	public Entity getPrimaryPassenger() {
		return getPassengerList().isEmpty() ? null : getPassengerList().get(0);
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return getPrimaryPassenger() == null;
	}

	@Override
	public boolean collidesWith(Entity other) {
		return BoatEntity.canCollide(this, other);
	}

	@Override
	public boolean collides() {
		return !isRemoved();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		lerpX = x;
		lerpY = y;
		lerpZ = z;
		lerpYaw = yaw;
		lerpPitch = pitch;
		lerpSteps = 10;
	}

	public void init(ItemStack stack) {
	}

	protected ItemStack getDroppedStack() {
		return stack;
	}

	protected UUID getOwner() {
		return TaglockItem.getTaglockUUID(stack);
	}

	protected float getSpeed() {
		return 0.75f;
	}
}
