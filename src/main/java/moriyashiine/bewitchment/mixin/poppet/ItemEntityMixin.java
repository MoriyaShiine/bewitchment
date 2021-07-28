package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.interfaces.SubmergedInWaterAccessor;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
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
		if (getStack().getItem() == BWObjects.VOODOO_POPPET) {
			LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, getStack());
			if (owner != null) {
				if (getVelocity().length() > 1 / 8f) {
					int damage = random.nextFloat() < 1 / 4f ? 1 : 0;
					if (getStack().damage(damage, random, null) && getStack().getDamage() >= getStack().getMaxDamage()) {
						getStack().decrement(1);
					}
					ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
					if (!potentialPoppet.isEmpty()) {
						if (potentialPoppet.damage(damage, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
							potentialPoppet.decrement(1);
						}
						return;
					}
					owner.addVelocity(getVelocity().x / 2, getVelocity().y / 2, getVelocity().z / 2);
					owner.velocityModified = true;
				}
				if (isSubmergedInWater()) {
					((SubmergedInWaterAccessor) owner).setSubmergedInWater(true);
				}
			}
		}
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getStack().getItem() == BWObjects.VOODOO_POPPET) {
			if (!world.isClient) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, getStack());
				if (owner != null) {
					if (source.isFire() || source == DamageSource.LIGHTNING_BOLT) {
						remove(RemovalReason.DISCARDED);
						ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
						if (!potentialPoppet.isEmpty()) {
							if (potentialPoppet.damage(getStack().getMaxDamage() / 2, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
								potentialPoppet.decrement(1);
							}
							return;
						}
						owner.setFireTicks(Integer.MAX_VALUE);
					}
					if (source == DamageSource.CACTUS && owner.hurtTime == 0) {
						if (getStack().damage(1, random, null) && getStack().getDamage() >= getStack().getMaxDamage()) {
							getStack().decrement(1);
						}
						ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
						if (!potentialPoppet.isEmpty()) {
							if (potentialPoppet.damage(1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
								potentialPoppet.decrement(1);
							}
							return;
						}
						owner.damage(DamageSource.CACTUS, 1);
					}
				}
			}
			if (source.isFire() || source == DamageSource.LIGHTNING_BOLT || source == DamageSource.CACTUS) {
				callbackInfo.setReturnValue(false);
			}
		}
	}
}
