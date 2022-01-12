/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient && source.getAttacker() instanceof PlayerEntity player && BewitchmentAPI.isVampire(player, false)) {
			amount /= 8;
		}
		return amount;
	}

	@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
	private void getJumpVelocity(CallbackInfoReturnable<Float> callbackInfo) {
		if ((Object) this instanceof PlayerEntity && BewitchmentAPI.isWerewolf(this, false)) {
			callbackInfo.setReturnValue(callbackInfo.getReturnValue() * 1.5f);
		}
	}

	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<EntityGroup> callbackInfo) {
		if ((Object) this instanceof PlayerEntity) {
			if (BewitchmentAPI.isVampire(this, true)) {
				callbackInfo.setReturnValue(EntityGroup.UNDEAD);
			} else if (BewitchmentAPI.isWerewolf(this, true)) {
				callbackInfo.setReturnValue(BewitchmentAPI.DEMON);
			}
		}
	}

	@Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
	private void blockedByShield(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isWerewolf(source.getSource(), false)) {
			callbackInfo.setReturnValue(false);
		}
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isWerewolf(this, false) && fallDistance <= 6) {
			callbackInfo.setReturnValue(false);
		}
	}
}
