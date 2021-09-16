package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.misc.PoppetData;
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
		List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, box);
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.getStack().getItem() == BWObjects.VOODOO_POPPET) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, itemEntity.getStack());
				if (owner != null) {
					for (StatusEffectInstance effect : statusEffects) {
						if (itemEntity.getStack().damage(1, random, null) && itemEntity.getStack().getDamage() >= itemEntity.getStack().getMaxDamage()) {
							itemEntity.getStack().decrement(1);
						}
						PoppetData poppetData = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner);
						if (!poppetData.stack.isEmpty()) {
							boolean sync = false;
							if (poppetData.stack.damage(1, random, null) && poppetData.stack.getDamage() >= poppetData.stack.getMaxDamage()) {
								poppetData.stack.decrement(1);
								sync = true;
							}
							poppetData.maybeSync(world, sync);
							continue;
						}
						owner.addStatusEffect(effect);
					}
				}
			}
		}
	}
}
