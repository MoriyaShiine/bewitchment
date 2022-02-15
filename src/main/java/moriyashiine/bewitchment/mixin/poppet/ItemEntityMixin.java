/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
	@Shadow
	public abstract ItemStack getStack();

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (getStack().getItem() == BWObjects.VOODOO_POPPET && !Bewitchment.config.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, getStack());
			if (owner != null) {
				if (getVelocity().length() > 1 / 8f) {
					int damage = random.nextFloat() < 1 / 4f ? 1 : 0;
					if (getStack().damage(damage, random, null) && getStack().getDamage() >= getStack().getMaxDamage()) {
						getStack().decrement(1);
					}
					if (!BewitchmentAPI.hasVoodooProtection(owner, damage)) {
						owner.addVelocity(getVelocity().x / 2, getVelocity().y / 2, getVelocity().z / 2);
						owner.velocityModified = true;
					}
				}
				if (isSubmergedInWater()) {
					int damage = age % 20 == 0 ? 1 : 0;
					if (getStack().damage(damage, random, null) && getStack().getDamage() >= getStack().getMaxDamage()) {
						getStack().decrement(1);
					}
					if (!BewitchmentAPI.hasVoodooProtection(owner, damage)) {
						BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(owner).setSubmerged(true);
					}
				}
			}
		}
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getStack().getItem() == BWObjects.VOODOO_POPPET && !Bewitchment.config.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			if (!world.isClient) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, getStack());
				if (owner != null) {
					if (source.isFire() || source == DamageSource.LIGHTNING_BOLT) {
						remove(RemovalReason.DISCARDED);
						if (!BewitchmentAPI.hasVoodooProtection(owner, getStack().getMaxDamage() / 4)) {
							owner.setFireTicks(Integer.MAX_VALUE);
						}
					}
					if (source == DamageSource.CACTUS && owner.hurtTime == 0) {
						if (getStack().damage(1, random, null) && getStack().getDamage() >= getStack().getMaxDamage()) {
							getStack().decrement(1);
						}
						if (!BewitchmentAPI.hasVoodooProtection(owner, 1)) {
							owner.damage(DamageSource.CACTUS, 1);
						}
					}
				}
			}
			if (source.isFire() || source == DamageSource.LIGHTNING_BOLT || source == DamageSource.CACTUS) {
				callbackInfo.setReturnValue(false);
			}
		}
	}

	@Inject(method = "applyWaterBuoyancy", at = @At("HEAD"), cancellable = true)
	private void bewitchment$stopPoppetsFromFloating(CallbackInfo ci) {
		if (getStack().getItem() instanceof PoppetItem) {
			Vec3d velocity = getVelocity();
			setVelocity(velocity.x * 0.99, velocity.y * 0.9, velocity.z * 0.99);
			ci.cancel();
		}
	}
}
