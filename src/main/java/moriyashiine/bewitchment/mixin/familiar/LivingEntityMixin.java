package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.interfaces.FamiliarAccessor;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements FamiliarAccessor {
	private static final TrackedData<Boolean> IS_FAMILIAR = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	@Shadow
	public abstract void heal(float amount);
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public boolean getFamiliar() {
		return dataTracker.get(IS_FAMILIAR);
	}
	
	@Override
	public void setFamiliar(boolean familiar) {
		dataTracker.set(IS_FAMILIAR, familiar);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (getFamiliar()) {
			if (!world.isClient) {
				if (age % 100 == 0) {
					heal(1);
				}
			}
			else if (random.nextFloat() < 0.25f) {
				world.addParticle(ParticleTypes.ENCHANT, getParticleX(getWidth()), getY() + MathHelper.nextFloat(random, 0, getHeight()), getParticleZ(getWidth()), 0, 0, 0);
			}
		}
	}
	
	@ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
	private StatusEffectInstance modifyStatusEffect(StatusEffectInstance effect) {
		if (!world.isClient && !effect.isAmbient() && effect.getEffectType().getType() == StatusEffectType.BENEFICIAL && (Object) this instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == BWEntityTypes.TOAD) {
			return new StatusEffectInstance(effect.getEffectType(), (int) (effect.getDuration() * (effect.getEffectType().isInstant() ? 1 : 1.5f)), effect.getAmplifier() + (effect.getEffectType().isInstant() ? 1 : 0), false, effect.shouldShowParticles(), effect.shouldShowIcon());
		}
		return effect;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient && getFamiliar()) {
			amount /= 8;
		}
		return amount;
	}
	
	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == BWEntityTypes.OWL) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "fall", at = @At("HEAD"), cancellable = true)
	private void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo callbackInfo) {
		if ((Object) this instanceof PlayerEntity && onGround && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == BWEntityTypes.OWL) {
			callbackInfo.cancel();
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		setFamiliar(nbt.getBoolean("IsFamiliar"));
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putBoolean("IsFamiliar", getFamiliar());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(IS_FAMILIAR, false);
	}
}
