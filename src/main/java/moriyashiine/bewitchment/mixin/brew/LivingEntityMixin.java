package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.network.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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
	@Nullable
	public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);
	
	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract void heal(float amount);
	
	@Shadow
	public int hurtTime;
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private DamageSource modifyDamage0(DamageSource source) {
		if (!world.isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof LivingEntity && ((LivingEntity) attacker).hasStatusEffect(BWStatusEffects.ENCHANTED)) {
				return attacker instanceof PlayerEntity ? new BWDamageSources.MagicPlayer(attacker) : new BWDamageSources.MagicMob(attacker);
			}
		}
		return source;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage1(float amount, DamageSource source) {
		if (!world.isClient) {
			Entity trueSource = source.getAttacker();
			Entity directSource = source.getSource();
			if (!source.isOutOfWorld() && (hasStatusEffect(BWStatusEffects.ETHEREAL) || (trueSource instanceof LivingEntity && ((LivingEntity) trueSource).hasStatusEffect(BWStatusEffects.ETHEREAL)))) {
				return 0;
			}
			if (directSource instanceof LivingEntity && ((LivingEntity) directSource).hasStatusEffect(BWStatusEffects.ENCHANTED)) {
				amount /= 4;
				amount += ((LivingEntity) directSource).getStatusEffect(BWStatusEffects.ENCHANTED).getAmplifier();
			}
			if (hasStatusEffect(BWStatusEffects.MAGIC_SPONGE) && source.isMagic()) {
				float magicAmount = (0.3f + (0.1f * getStatusEffect(BWStatusEffects.MAGIC_SPONGE).getAmplifier()));
				amount *= (1 - magicAmount);
				if ((Object) this instanceof PlayerEntity) {
					BewitchmentAPI.fillMagic((PlayerEntity) (Object) this, (int) (amount * magicAmount), false);
				}
			}
		}
		return amount;
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient) {
			Entity directSource = source.getSource();
			if (hasStatusEffect(BWStatusEffects.DEFLECTION) && directSource != null && EntityTypeTags.ARROWS.contains(directSource.getType())) {
				int amplifier = getStatusEffect(BWStatusEffects.DEFLECTION).getAmplifier() + 1;
				Vec3d velocity = directSource.getVelocity();
				directSource.setVelocity(velocity.getX() * 2 * amplifier, velocity.getY() * 2 * amplifier, velocity.getZ() * 2 * amplifier);
				callbackInfo.setReturnValue(false);
			}
			else if (amount > 0 && hurtTime == 0) {
				if (!hasStatusEffect(StatusEffects.STRENGTH) && !hasStatusEffect(StatusEffects.REGENERATION) && !hasStatusEffect(StatusEffects.RESISTANCE) && directSource instanceof LivingEntity && ((LivingEntity) directSource).hasStatusEffect(BWStatusEffects.LEECHING)) {
					((LivingEntity) directSource).heal(amount * (((LivingEntity) directSource).getStatusEffect(BWStatusEffects.LEECHING).getAmplifier() + 1) / 8);
				}
				if (hasStatusEffect(BWStatusEffects.THORNS) && !(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
					directSource.damage(DamageSource.thorns(directSource), 2 * (getStatusEffect(BWStatusEffects.THORNS).getAmplifier() + 1));
				}
				if (hasStatusEffect(BWStatusEffects.VOLATILITY) && !source.isExplosive()) {
					for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(3), LivingEntity::isAlive)) {
						entity.damage(DamageSource.explosion(((LivingEntity) (Object) this)), 4 * (getStatusEffect(BWStatusEffects.VOLATILITY).getAmplifier() + 1));
					}
					world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1, 1);
					PlayerLookup.tracking(this).forEach(playerEntity -> SpawnExplosionParticlesPacket.send(playerEntity, this));
					if (((Object) this) instanceof PlayerEntity) {
						SpawnExplosionParticlesPacket.send((PlayerEntity) (Object) this, this);
					}
					removeStatusEffect(BWStatusEffects.VOLATILITY);
				}
			}
		}
	}
	
	@Inject(method = "canBreatheInWater", at = @At("RETURN"), cancellable = true)
	private void canBreatheInWater(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && !world.isClient && hasStatusEffect(BWStatusEffects.GILLS)) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "isClimbing", at = @At("RETURN"), cancellable = true)
	private void isClimbing(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && hasStatusEffect(BWStatusEffects.CLIMBING) && horizontalCollision) {
			callbackInfo.setReturnValue(true);
		}
	}
}
