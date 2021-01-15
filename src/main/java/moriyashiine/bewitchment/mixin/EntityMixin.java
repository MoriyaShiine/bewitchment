package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.MasterAccessor;
import moriyashiine.bewitchment.api.interfaces.Pledgeable;
import moriyashiine.bewitchment.api.interfaces.WetAccessor;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin implements WetAccessor {
	private int wetTimer = 0;
	
	@Shadow
	public abstract UUID getUuid();
	
	@Shadow
	public World world;
	
	@Shadow
	@Final
	protected Random random;
	
	@Shadow
	public abstract boolean isOnFire();
	
	@Override
	public int getWetTimer() {
		return wetTimer;
	}
	
	@Override
	public void setWetTimer(int wetTimer) {
		this.wetTimer = wetTimer;
	}
	
	@Inject(method = "isWet", at = @At("HEAD"), cancellable = true)
	private void isWet(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "isTouchingWaterOrRain", at = @At("HEAD"), cancellable = true)
	private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof LivingEntity) {
			Entity attacker = source.getAttacker();
			if (attacker instanceof LivingEntity) {
				MasterAccessor.of(this).ifPresent(masterAccessor -> {
					if (attacker.getUuid().equals(masterAccessor.getMasterUUID())) {
						callbackInfo.setReturnValue(true);
					}
				});
				MasterAccessor.of(attacker).ifPresent(masterAccessor -> {
					if (getUuid().equals(masterAccessor.getMasterUUID())) {
						callbackInfo.setReturnValue(true);
					}
				});
			}
		}
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (getWetTimer() > 0) {
			setWetTimer(getWetTimer() - 1);
		}
	}
	
	@Inject(method = "setOnFireFor", at = @At("HEAD"))
	private void setOnFireFor(int seconds, CallbackInfo callbackInfo) {
		if (seconds > 0 && !isOnFire() && (Object) this instanceof PlayerEntity) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
			if (!poppet.isEmpty()) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
				if (!owner.getUuid().equals(getUuid()) && !owner.isOnFire()) {
					if (poppet.damage(1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
						poppet.decrement(1);
					}
					ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
					if (!potentialPoppet.isEmpty()) {
						if (potentialPoppet.damage(1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
							potentialPoppet.decrement(1);
						}
						return;
					}
					owner.setOnFireFor(seconds);
				}
			}
		}
	}
	
	@Inject(method = "remove", at = @At("HEAD"))
	private void remove(CallbackInfo callbackInfo) {
		if (!world.isClient && this instanceof Pledgeable) {
			BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
			for (int i = worldState.specificPledges.size() - 1; i >= 0; i--) {
				Pair<UUID, UUID> pair = worldState.specificPledges.get(i);
				if (pair.getLeft().equals(getUuid())) {
					BewitchmentAPI.unpledge(world, ((Pledgeable) this).getPledgeUUID(), pair.getLeft());
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
}
