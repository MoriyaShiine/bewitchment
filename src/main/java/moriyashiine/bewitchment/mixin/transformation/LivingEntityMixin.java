package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
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
	
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private float modifyDamage0(float amount, DamageSource source) {
		if (source == BWDamageSources.SUN) {
			amount *= 2;
		}
		return amount;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage1(float amount, DamageSource source) {
		if (!world.isClient && source.getSource() instanceof PlayerEntity && BewitchmentAPI.isVampire(source.getSource(), false)) {
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
			}
			else if (BewitchmentAPI.isWerewolf(this, true)) {
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
