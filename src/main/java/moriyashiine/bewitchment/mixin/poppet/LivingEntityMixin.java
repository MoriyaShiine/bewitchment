package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public abstract boolean clearStatusEffects();
	
	@Shadow
	public abstract void setHealth(float health);
	
	@Shadow
	protected abstract float getSoundVolume();
	
	@Shadow
	protected abstract float getSoundPitch();
	
	@Shadow
	public int hurtTime;
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
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
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient && amount > 0 && hurtTime == 0 && (Object) this instanceof PlayerEntity && source == DamageSource.DROWN) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
			if (!poppet.isEmpty()) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
				if (!owner.getUuid().equals(getUuid())) {
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
					owner.damage(source, amount);
				}
			}
		}
	}
	
	@Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
	private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && !world.isClient) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.DEATH_PROTECTION_POPPET, this, null);
			if (!poppet.isEmpty()) {
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
			if (callbackInfo.getReturnValue() && this instanceof TransformationAccessor && ((CurseAccessor) this).hasCurse(BWCurses.SUSCEPTIBILITY) && ((TransformationAccessor) this).getTransformation() == BWTransformations.HUMAN) {
				if (source.getSource() instanceof VampireEntity || (BewitchmentAPI.isVampire(source.getSource(), true) && BewitchmentAPI.isPledged(world, BWPledges.LILITH, source.getSource().getUuid()))) {
					((TransformationAccessor) this).getTransformation().onRemoved((LivingEntity) (Object) this);
					((TransformationAccessor) this).setTransformation(BWTransformations.VAMPIRE);
					((TransformationAccessor) this).getTransformation().onAdded((LivingEntity) (Object) this);
					PlayerLookup.tracking(this).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, this));
					if ((Object) this instanceof PlayerEntity) {
						SpawnSmokeParticlesPacket.send((PlayerEntity) (Object) this, this);
					}
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_CURSE, getSoundCategory(), getSoundVolume(), getSoundPitch());
				}
				else if (source.getSource() instanceof WerewolfEntity || (BewitchmentAPI.isWerewolf(source.getSource(), false) && BewitchmentAPI.isPledged(world, BWPledges.HERNE, source.getSource().getUuid()))) {
					((TransformationAccessor) this).getTransformation().onRemoved((LivingEntity) (Object) this);
					((TransformationAccessor) this).setTransformation(BWTransformations.WEREWOLF);
					((TransformationAccessor) this).getTransformation().onAdded((LivingEntity) (Object) this);
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
					if ((Object) this instanceof PlayerEntity) {
						SpawnSmokeParticlesPacket.send((PlayerEntity) (Object) this, this);
					}
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_CURSE, getSoundCategory(), getSoundVolume(), getSoundPitch());
				}
			}
		}
	}
	
	@Inject(method = "addStatusEffect", at = @At("HEAD"))
	private void addStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient && !effect.isAmbient() && (Object) this instanceof PlayerEntity) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
			if (!poppet.isEmpty()) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
				if (!owner.getUuid().equals(getUuid())) {
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
					owner.addStatusEffect(effect);
				}
			}
		}
	}
}
