/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract SoundCategory getSoundCategory();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F"), ordinal = 0, argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient) {
			amount = BWDamageSources.handleDamage(this, source, amount);
		}
		return amount;
	}

	@ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
	private float modifyExhaustion(float exhaustion) {
		if (!world.isClient) {
			if (BewitchmentAPI.isWerewolf(this, true)) {
				exhaustion *= 1.25f;
			}
			if (BewitchmentAPI.isWerewolf(this, false)) {
				exhaustion *= 2;
			}
		}
		return exhaustion;
	}

	@Inject(method = "getHurtSound", at = @At("HEAD"))
	private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> callbackInfo) {
		if (source == BWDamageSources.SUN) {
			world.playSound(null, getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, getSoundCategory(), getSoundVolume(), getSoundPitch());
		}
	}

	@Inject(method = "canFoodHeal", at = @At("RETURN"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}

	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eat(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null) {
				boolean vampire = BewitchmentAPI.isVampire(this, true);
				if (vampire || (BewitchmentAPI.isWerewolf(this, true) && !foodComponent.isMeat())) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1));
				}
				if (vampire && (stack.getItem() == BWObjects.GARLIC || stack.getItem() == BWObjects.GRILLED_GARLIC || stack.getItem() == BWObjects.GARLIC_BREAD)) {
					damage(BWDamageSources.MAGIC_COPY, Float.MAX_VALUE);
				}
			}
		}
	}
}
