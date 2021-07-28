package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.ReviveEvents;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.interfaces.SubmergedInWaterAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
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
public abstract class LivingEntityMixin extends Entity implements SubmergedInWaterAccessor {
	private static final TrackedData<Boolean> SUBMERGED_IN_WATER = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public abstract boolean clearStatusEffects();
	
	@Shadow
	public abstract void setHealth(float health);
	
	@Shadow
	protected abstract float getSoundVolume();
	
	@Shadow
	public abstract float getSoundPitch();
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public boolean getSubmergedInWater() {
		return dataTracker.get(SUBMERGED_IN_WATER);
	}
	
	@Override
	public void setSubmergedInWater(boolean submergedInWater) {
		dataTracker.set(SUBMERGED_IN_WATER, submergedInWater);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getSubmergedInWater()) {
			setSubmergedInWater(false);
		}
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient) {
			Entity trueSource = source.getAttacker();
			Entity directSource = source.getSource();
			if (amount > 0 && (Object) this instanceof PlayerEntity && !BewitchmentAPI.isVampire(this, true)) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VAMPIRIC_POPPET, null, (PlayerEntity) (Object) this);
				if (!poppet.isEmpty()) {
					LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
					if (!BewitchmentAPI.isVampire(owner, true) && !getUuid().equals(owner.getUuid()) && owner.damage(BWDamageSources.VAMPIRE, amount)) {
						if (poppet.damage((int) (amount * (BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0.5f : 1)), random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
							poppet.decrement(1);
						}
						return 0;
					}
				}
			}
			if (source.isFire() || source == DamageSource.DROWN || source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.PROTECTION_POPPET, this, null);
				if (!poppet.isEmpty()) {
					if (poppet.damage((int) (amount * ((Object) this instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0.5f : 1)), random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
						poppet.decrement(1);
					}
					return 0;
				}
			}
			if (trueSource instanceof LivingEntity && BewitchmentAPI.isWeakToSilver((LivingEntity) trueSource)) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.JUDGMENT_POPPET, this, null);
				if (!poppet.isEmpty()) {
					if (poppet.damage((Object) this instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
						poppet.decrement(1);
					}
					amount /= 4;
				}
			}
			if (directSource instanceof LivingEntity) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.FATIGUE_POPPET, this, null);
				if (!poppet.isEmpty() && ((LivingEntity) directSource).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1)) && poppet.damage((Object) this instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
					poppet.decrement(1);
				}
			}
		}
		return amount;
	}
	
	@Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
	private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient) {
			if (!callbackInfo.getReturnValue()) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.DEATH_PROTECTION_POPPET, this, null);
				if (!poppet.isEmpty() && !ReviveEvents.CANCEL_REVIVE.invoker().shouldCancel((PlayerEntity) (Object) this, source, poppet)) {
					ReviveEvents.ON_REVIVE.invoker().onRevive((PlayerEntity) (Object) this, source, poppet);
					if (poppet.damage((Object) this instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
						poppet.decrement(1);
					}
					setHealth(1);
					clearStatusEffects();
					addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
					callbackInfo.setReturnValue(true);
				}
			}
			if (callbackInfo.getReturnValue() && (Object) this instanceof PlayerEntity && ((CurseAccessor) this).hasCurse(BWCurses.SUSCEPTIBILITY) && ((TransformationAccessor) this).getTransformation() == BWTransformations.HUMAN) {
				if (source.getSource() instanceof VampireEntity || (BewitchmentAPI.isVampire(source.getSource(), true) && BewitchmentAPI.isPledged((PlayerEntity) source.getSource(), BWPledges.LILITH))) {
					((TransformationAccessor) this).getTransformation().onRemoved((PlayerEntity) (Object) this);
					((TransformationAccessor) this).setTransformation(BWTransformations.VAMPIRE);
					((TransformationAccessor) this).getTransformation().onAdded((PlayerEntity) (Object) this);
					PlayerLookup.tracking(this).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, this));
					SpawnSmokeParticlesPacket.send((PlayerEntity) (Object) this, this);
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_CURSE, getSoundCategory(), getSoundVolume(), getSoundPitch());
				}
				else if (source.getSource() instanceof WerewolfEntity || (BewitchmentAPI.isWerewolf(source.getSource(), false) && BewitchmentAPI.isPledged((PlayerEntity) source.getSource(), BWPledges.HERNE))) {
					((TransformationAccessor) this).getTransformation().onRemoved((PlayerEntity) (Object) this);
					((TransformationAccessor) this).setTransformation(BWTransformations.WEREWOLF);
					((TransformationAccessor) this).getTransformation().onAdded((PlayerEntity) (Object) this);
					int variant = -1;
					if (source.getSource() instanceof WerewolfEntity) {
						variant = source.getSource().getDataTracker().get(BWHostileEntity.VARIANT);
					}
					else if (source.getSource() instanceof WerewolfAccessor) {
						variant = ((WerewolfAccessor) source.getSource()).getWerewolfVariant();
					}
					if (variant > -1) {
						((WerewolfAccessor) this).setWerewolfVariant(variant);
					}
					PlayerLookup.tracking(this).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, this));
					SpawnSmokeParticlesPacket.send((PlayerEntity) (Object) this, this);
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_CURSE, getSoundCategory(), getSoundVolume(), getSoundPitch());
				}
			}
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		setSubmergedInWater(nbt.getBoolean("SubmergedInWater"));
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putBoolean("SubmergedInWater", getSubmergedInWater());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(SUBMERGED_IN_WATER, false);
	}
}
