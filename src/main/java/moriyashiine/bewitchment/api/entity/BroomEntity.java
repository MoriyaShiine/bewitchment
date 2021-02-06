package moriyashiine.bewitchment.api.entity;

import moriyashiine.bewitchment.common.entity.interfaces.PressingForwardAccessor;
import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
	
	public BroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected void initDataTracker() {
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		stack = ItemStack.fromTag(tag.getCompound("Stack"));
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		tag.put("Stack", stack.toTag(new CompoundTag()));
	}
	
	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, 0);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (isLogicalSideForUpdatingMovement()) {
			updateTrackedPosition(getX(), getY(), getZ());
			Entity passenger = getPrimaryPassenger();
			if (passenger instanceof PressingForwardAccessor) {
				setRotation(passenger.yaw, passenger.pitch);
				if (((PressingForwardAccessor) passenger).getPressingForward()) {
					addVelocity(passenger.getRotationVector().x / 8, passenger.getRotationVector().y / 8, passenger.getRotationVector().z / 8);
					setVelocity(MathHelper.clamp(getVelocity().x, -1, 1), MathHelper.clamp(getVelocity().y, -1, 1), MathHelper.clamp(getVelocity().z, -1, 1));
				}
			}
			move(MovementType.SELF, getVelocity());
			setVelocity(getVelocity().multiply(0.9));
		}
		else {
			setVelocity(Vec3d.ZERO);
		}
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.shouldCancelInteraction()) {
			return ActionResult.PASS;
		}
		else if (!world.isClient) {
			return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
		}
		return super.interact(player, hand);
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) {
			return false;
		}
		if (!world.isClient && !removed && !hasPassengers() && (stack.isEmpty() || (source.getAttacker() instanceof PlayerEntity && source.getAttacker().getUuid().equals(getOwner())))) {
			dropStack(getDroppedStack());
			remove();
		}
		return true;
	}
	
	@Nullable
	@Override
	public Entity getPrimaryPassenger() {
		return getPassengerList().isEmpty() ? null : getPassengerList().get(0);
	}
	
	@Override
	public double getMountedHeightOffset() {
		return 0.25f + MathHelper.sin((age + getEntityId()) / 4f) / 16f;
	}
	
	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return getPrimaryPassenger() == null;
	}
	
	@Override
	public boolean collidesWith(Entity other) {
		return BoatEntity.method_30959(this, other);
	}
	
	@Override
	public boolean collides() {
		return !removed;
	}
	
	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	protected boolean canClimb() {
		return false;
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}
	
	public void init(ItemStack stack) {
	}
	
	protected ItemStack getDroppedStack() {
		return stack;
	}
	
	protected UUID getOwner() {
		return TaglockItem.getTaglockUUID(stack);
	}
}
