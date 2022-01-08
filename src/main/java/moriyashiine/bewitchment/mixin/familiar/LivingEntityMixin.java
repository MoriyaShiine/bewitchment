package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
	private StatusEffectInstance modifyStatusEffect(StatusEffectInstance effect) {
		if (!world.isClient && !effect.isAmbient() && effect.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL && (Object) this instanceof PlayerEntity player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD) {
			return new StatusEffectInstance(effect.getEffectType(), (int) (effect.getDuration() * (effect.getEffectType().isInstant() ? 1 : 1.5f)), effect.getAmplifier() + (effect.getEffectType().isInstant() ? 1 : 0), false, effect.shouldShowParticles(), effect.shouldShowIcon());
		}
		return effect;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient && BWComponents.FAMILIAR_COMPONENT.get(this).isFamiliar()) {
			amount /= 8;
		}
		return amount;
	}
	
	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof PlayerEntity player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.OWL) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "fall", at = @At("HEAD"), cancellable = true)
	private void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo callbackInfo) {
		if ((Object) this instanceof PlayerEntity player && onGround && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.OWL) {
			callbackInfo.cancel();
		}
	}
}
