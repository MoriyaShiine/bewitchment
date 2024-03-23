/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private DamageSource modifyDamage0(DamageSource source) {
		if (!getWorld().isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof LivingEntity livingAttacker && livingAttacker.hasStatusEffect(BWStatusEffects.ENCHANTED)) {
				return getWorld().getDamageSources().indirectMagic(livingAttacker, livingAttacker);
			}
		}
		return source;
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage1(float amount, DamageSource source) {
		if (!getWorld().isClient) {
			if (!source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && (hasStatusEffect(BWStatusEffects.ETHEREAL) || (source.getAttacker() instanceof LivingEntity livingAttacker && livingAttacker.hasStatusEffect(BWStatusEffects.ETHEREAL)))) {
				return 0;
			}
			if (source.getSource() instanceof LivingEntity livingSource && livingSource.hasStatusEffect(BWStatusEffects.ENCHANTED)) {
				amount /= 4;
				amount += livingSource.getStatusEffect(BWStatusEffects.ENCHANTED).getAmplifier();
			}
			if (hasStatusEffect(BWStatusEffects.MAGIC_SPONGE) && source.isIn(DamageTypeTags.WITCH_RESISTANT_TO)) {
				float magicAmount = (0.3f + (0.1f * getStatusEffect(BWStatusEffects.MAGIC_SPONGE).getAmplifier()));
				amount *= (1 - magicAmount);
				if ((Object) this instanceof PlayerEntity player) {
					BewitchmentAPI.fillMagic(player, (int) (amount * magicAmount), false);
				}
			}
		}
		return amount;
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!getWorld().isClient) {
			Entity directSource = source.getSource();
			if (hasStatusEffect(BWStatusEffects.DEFLECTION) && directSource != null && directSource.getType().isIn(EntityTypeTags.ARROWS)) {
				int amplifier = getStatusEffect(BWStatusEffects.DEFLECTION).getAmplifier() + 1;
				Vec3d velocity = directSource.getVelocity();
				directSource.setVelocity(velocity.getX() * 2 * amplifier, velocity.getY() * 2 * amplifier, velocity.getZ() * 2 * amplifier);
				callbackInfo.setReturnValue(false);
			} else if (amount > 0 && hurtTime == 0) {
				if (!hasStatusEffect(StatusEffects.STRENGTH) && !hasStatusEffect(StatusEffects.REGENERATION) && !hasStatusEffect(StatusEffects.RESISTANCE) && directSource instanceof LivingEntity livingSource && livingSource.hasStatusEffect(BWStatusEffects.LEECHING)) {
					livingSource.heal(amount * (livingSource.getStatusEffect(BWStatusEffects.LEECHING).getAmplifier() + 1) / 8);
				}
				if (directSource != null && hasStatusEffect(BWStatusEffects.THORNS) && source.isOf(DamageTypes.THORNS)) {
					directSource.damage(getWorld().getDamageSources().thorns(directSource), 2 * (getStatusEffect(BWStatusEffects.THORNS).getAmplifier() + 1));
				}
				if (hasStatusEffect(BWStatusEffects.VOLATILITY) && !source.isIn(DamageTypeTags.IS_EXPLOSION)) {
					for (LivingEntity entity : getWorld().getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(3), foundEntity -> foundEntity.isAlive() && !foundEntity.getUuid().equals(getUuid()))) {
						entity.damage(getWorld().getDamageSources().explosion(((LivingEntity) (Object) this), ((LivingEntity) (Object) this)), 4 * (getStatusEffect(BWStatusEffects.VOLATILITY).getAmplifier() + 1));
					}
					getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1, 1);
					PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnExplosionParticlesPacket.send(trackingPlayer, this));
					if (((Object) this) instanceof ServerPlayerEntity player) {
						SpawnExplosionParticlesPacket.send(player, this);
					}
					removeStatusEffect(BWStatusEffects.VOLATILITY);
				}
			}
		}
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "HEAD"))
	private void addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		if (effect.getEffectType() == BWStatusEffects.POLYMORPH && source instanceof AreaEffectCloudEntity) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(source).ifPresent(sourcePolymorphComponent -> {
				if (sourcePolymorphComponent.getUuid() != null) {
					BWComponents.POLYMORPH_COMPONENT.maybeGet(LivingEntity.class.cast(this)).ifPresent(entityPolymorphComponent -> {
						entityPolymorphComponent.setUuid(sourcePolymorphComponent.getUuid());
						entityPolymorphComponent.setName(sourcePolymorphComponent.getName());
					});
				}
			});
		}
	}

	@Inject(method = "canBreatheInWater", at = @At("RETURN"), cancellable = true)
	private void canBreatheInWater(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !getWorld().isClient && hasStatusEffect(BWStatusEffects.GILLS)) {
			callbackInfo.setReturnValue(true);
		}
	}

	@Inject(method = "isClimbing", at = @At("RETURN"), cancellable = true)
	private void isClimbing(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && hasStatusEffect(BWStatusEffects.CLIMBING) && horizontalCollision) {
			callbackInfo.setReturnValue(true);
		}
	}
}
