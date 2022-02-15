/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
	public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "applySplashPotion", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void applySplashPotion(List<StatusEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo callbackInfo, Box box) {
		if (Bewitchment.config.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			return;
		}
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, box);
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.getStack().getItem() == BWObjects.VOODOO_POPPET) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, itemEntity.getStack());
				if (owner != null && owner.isAffectedBySplashPotions()) {
					for (StatusEffectInstance effect : statusEffects) {
						if (itemEntity.getStack().damage(8, random, null) && itemEntity.getStack().getDamage() >= itemEntity.getStack().getMaxDamage()) {
							itemEntity.getStack().decrement(1);
						}
						if (!BewitchmentAPI.hasVoodooProtection(owner, 8)) {
							if (effect.getEffectType().isInstant()) {
								effect.getEffectType().applyInstantEffect(null, null, owner, effect.getAmplifier(), 0.5);
							} else {
								owner.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), effect.getDuration() / 2, effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles()));
							}
						}
					}
				}
			}
		}
	}
}
