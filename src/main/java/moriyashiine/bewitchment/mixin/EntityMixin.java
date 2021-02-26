package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.common.entity.interfaces.InsanityTargetAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.WetAccessor;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin implements WetAccessor {
	private static final TrackedData<Integer> WET_TIMER = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.INTEGER);
	
	@Shadow
	public abstract UUID getUuid();
	
	@Shadow
	public abstract boolean isOnFire();
	
	@Shadow
	public World world;
	
	@Shadow
	@Final
	protected Random random;
	
	@Shadow
	@Final
	protected DataTracker dataTracker;
	
	@Override
	public int getWetTimer() {
		return dataTracker.get(WET_TIMER);
	}
	
	@Override
	public void setWetTimer(int wetTimer) {
		dataTracker.set(WET_TIMER, wetTimer);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getWetTimer() > 0) {
			setWetTimer(getWetTimer() - 1);
		}
	}
	
	@ModifyVariable(method = "setPose", at = @At("HEAD"))
	private EntityPose setPose(EntityPose pose) {
		if (((Object) this) instanceof PlayerEntity) {
			if (BewitchmentAPI.isVampire((Entity) (Object) this, false) || BewitchmentAPI.isWerewolf((Entity) (Object) this, false)) {
				if (pose == EntityPose.FALL_FLYING || pose == EntityPose.SWIMMING) {
					return EntityPose.STANDING;
				}
			}
		}
		return pose;
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient) {
			if (!callbackInfo.getReturnValue() && this instanceof MasterAccessor) {
				Entity attacker = source.getAttacker();
				if (attacker instanceof LivingEntity) {
					if (this instanceof MasterAccessor) {
						if (attacker.getUuid().equals(((MasterAccessor) this).getMasterUUID())) {
							callbackInfo.setReturnValue(true);
						}
					}
					if (attacker instanceof MasterAccessor) {
						if (getUuid().equals(((MasterAccessor) attacker).getMasterUUID())) {
							callbackInfo.setReturnValue(true);
						}
					}
				}
			}
		}
	}
	
	@Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
	private void isFireImmune(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && BewitchmentAPI.isVampire((Entity) (Object) this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "isWet", at = @At("RETURN"), cancellable = true)
	private void isWet(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "isTouchingWaterOrRain", at = @At("RETURN"), cancellable = true)
	private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "setOnFireFor", at = @At("HEAD"), cancellable = true)
	private void setOnFireFor(int seconds, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (seconds > 0 && !isOnFire() && (Object) this instanceof PlayerEntity) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
				if (!poppet.isEmpty()) {
					LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
					if (!owner.getUuid().equals(getUuid()) && !owner.isOnFire()) {
						if (poppet.damage(BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
							poppet.decrement(1);
						}
						ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
						if (!potentialPoppet.isEmpty()) {
							if (potentialPoppet.damage(owner instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) owner) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
								potentialPoppet.decrement(1);
							}
							return;
						}
						owner.setOnFireFor(seconds);
					}
				}
			}
			if (this instanceof InsanityTargetAccessor && ((InsanityTargetAccessor) this).getInsanityTargetUUID().isPresent()) {
				callbackInfo.cancel();
			}
		}
	}
	
	@Inject(method = "remove", at = @At("TAIL"))
	private void remove(CallbackInfo callbackInfo) {
		if (!world.isClient && this instanceof Pledgeable) {
			BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
			for (int i = worldState.specificPledges.size() - 1; i >= 0; i--) {
				Pair<UUID, UUID> pair = worldState.specificPledges.get(i);
				if (pair.getRight().equals(getUuid())) {
					BewitchmentAPI.unpledge(world, ((Pledgeable) this).getPledgeID(), pair.getLeft());
					worldState.specificPledges.remove(i);
					worldState.markDirty();
				}
			}
		}
	}
	
	@Inject(method = "fromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		setWetTimer(tag.getInt("WetTimer"));
	}
	
	@Inject(method = "toTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfoReturnable<Tag> callbackInfo) {
		tag.putInt("WetTimer", getWetTimer());
	}
	
	@Inject(method = "<init>", at = @At("RETURN"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(WET_TIMER, 0);
	}
}
