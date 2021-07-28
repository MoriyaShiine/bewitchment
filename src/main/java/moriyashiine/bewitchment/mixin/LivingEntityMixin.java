package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.BloodSetEvents;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.common.entity.interfaces.CaduceusFireballAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
public abstract class LivingEntityMixin extends Entity implements BloodAccessor {
	private static final TrackedData<Integer> BLOOD = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract boolean isSleeping();
	
	@Shadow
	public abstract Iterable<ItemStack> getArmorItems();
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public int getBlood() {
		return dataTracker.get(BLOOD);
	}
	
	@Override
	public void setBlood(int blood) {
		BloodSetEvents.ON_BLOOD_SET.invoker().onSetBlood(this, blood);
		dataTracker.set(BLOOD, blood);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			LivingEntity livingEntity = (LivingEntity) (Object) this;
			int damage = 0;
			if (livingEntity.getMainHandStack().getItem() == BWObjects.GARLIC && BewitchmentAPI.isVampire(this, true)) {
				damage++;
			}
			if (livingEntity.getOffHandStack().getItem() == BWObjects.GARLIC && BewitchmentAPI.isVampire(this, true)) {
				damage++;
			}
			if (livingEntity.getMainHandStack().getItem() == BWObjects.ACONITE && BewitchmentAPI.isWerewolf(this, true)) {
				damage++;
			}
			if (livingEntity.getOffHandStack().getItem() == BWObjects.ACONITE && BewitchmentAPI.isWerewolf(this, true)) {
				damage++;
			}
			if (damage > 0) {
				damage(BWDamageSources.MAGIC_COPY, damage);
			}
			if (BWTags.HAS_BLOOD.contains(getType()) && !BewitchmentAPI.isVampire(this, true) && random.nextFloat() < (isSleeping() ? 1 / 50f : 1 / 500f)) {
				fillBlood(1, false);
			}
		}
	}
	
	@ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
	private StatusEffectInstance modifyStatusEffect(StatusEffectInstance effect) {
		if (!world.isClient && !effect.isAmbient() && !effect.getEffectType().isInstant() && effect.getEffectType().getType() == StatusEffectType.HARMFUL) {
			float durationMultiplier = 1;
			for (ItemStack stack : getArmorItems()) {
				durationMultiplier -= EnchantmentHelper.getLevel(BWEnchantments.MAGIC_PROTECTION, stack) / 32f;
			}
			if (durationMultiplier < 1) {
				return new StatusEffectInstance(effect.getEffectType(), (int) (effect.getDuration() * durationMultiplier), effect.getAmplifier(), false, effect.shouldShowParticles(), effect.shouldShowIcon());
			}
		}
		return effect;
	}
	
	@ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/LivingEntity;getHealth()F"))
	private float modifyDamage0(float amount, DamageSource source) {
		if (!world.isClient) {
			amount = BWDamageSources.handleDamage((LivingEntity) (Object) this, source, amount);
		}
		return amount;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage1(float amount, DamageSource source) {
		if (!world.isClient) {
			Entity trueSource = source.getAttacker();
			Entity directSource = source.getSource();
			if (directSource instanceof FireballEntity) {
				if (trueSource instanceof PlayerEntity && ((CaduceusFireballAccessor) directSource).getFromCaduceus()) {
					amount *= 1.5f;
				}
				if (trueSource instanceof BaphometEntity) {
					amount *= 3;
				}
			}
			if (directSource instanceof WitherSkullEntity && trueSource instanceof LilithEntity) {
				amount *= 3;
			}
			if (source.isMagic() && (Object) this instanceof LivingEntity) {
				int armorPieces = BWUtil.getArmorPieces((LivingEntity) (Object) this, stack -> {
					if (stack.getItem() instanceof ArmorItem) {
						ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
						return material == BWMaterials.HEDGEWITCH_ARMOR || material == BWMaterials.ALCHEMIST_ARMOR || material == BWMaterials.BESMIRCHED_ARMOR || material == BWMaterials.HARBINGER_ARMOR;
					}
					return false;
				});
				if (armorPieces > 0) {
					amount *= (1 - (0.2f * armorPieces));
				}
			}
			if ((getVehicle() != null && getVehicle().getType() == BWEntityTypes.CYPRESS_BROOM) || (source.getAttacker() != null && source.getAttacker().getVehicle() != null && source.getAttacker().getVehicle().getType() == BWEntityTypes.CYPRESS_BROOM)) {
				amount *= 0.2f;
			}
		}
		return amount;
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient) {
			Entity trueSource = source.getAttacker();
			if (trueSource instanceof LeonardEntity) {
				removeStatusEffect(BWStatusEffects.MAGIC_SPONGE);
			}
			if (trueSource instanceof BaphometEntity) {
				removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
			}
			if (trueSource instanceof LilithEntity) {
				removeStatusEffect(StatusEffects.STRENGTH);
			}
			if (trueSource instanceof HerneEntity) {
				removeStatusEffect(StatusEffects.RESISTANCE);
			}
			if (trueSource instanceof Pledgeable) {
				((Pledgeable) trueSource).setTimeSinceLastAttack(0);
			}
		}
	}
	
	@Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo callbackInfo) {
		if (this instanceof MasterAccessor && ((MasterAccessor) this).getMasterUUID() != null) {
			callbackInfo.cancel();
		}
	}
	
	@Inject(method = "isAffectedBySplashPotions", at = @At("RETURN"), cancellable = true)
	private void isAffectedBySplashPotions(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && this instanceof MasterAccessor && ((MasterAccessor) this).getMasterUUID() != null) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("Blood")) {
			setBlood(nbt.getInt("Blood"));
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putInt("Blood", getBlood());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(BLOOD, MAX_BLOOD);
	}
}
