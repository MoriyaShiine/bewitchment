package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class SilverHandler extends Entity {
	public SilverHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;getHealth()F"))
	private float modifyDamageOffense(float amount, DamageSource source) {
		if (BewitchmentAPI.isWeakToSilver((LivingEntity) (Object) this)) {
			if (BewitchmentAPI.isSourceFromSilver(source)) {
				return amount + 4;
			}
		}
		return amount;
	}
	
	@ModifyVariable(method = {"damage"}, at = @At("HEAD"))
	private float modifyDamageDefense(float amount, DamageSource source) {
		if (!(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
			Entity attacker = source.getSource();
			if (attacker instanceof LivingEntity) {
				LivingEntity livingAttacker = (LivingEntity) attacker;
				if (BewitchmentAPI.isWeakToSilver(livingAttacker)) {
					int armorPieces = BewitchmentAPI.getArmorPieces(livingAttacker, stack -> BWTags.SILVER_ARMOR.contains(stack.getItem()));
					if (armorPieces > 0) {
						attacker.damage(DamageSource.thorns(this), armorPieces);
					}
					return amount * (1 - (0.125f * armorPieces));
				}
			}
		}
		return amount;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void damageEntitiesWeakToSilver(CallbackInfo callbackInfo) {
		//noinspection ConstantConditions
		if (!world.isClient && (Object) this instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) (Object) this;
			if (BewitchmentAPI.isWeakToSilver(livingEntity)) {
				int damage = BewitchmentAPI.getArmorPieces(livingEntity, stack -> BWTags.SILVER_ARMOR.contains(stack.getItem()));
				if (BewitchmentAPI.isHoldingSilver(livingEntity, Hand.MAIN_HAND)) {
					damage++;
				}
				if (BewitchmentAPI.isHoldingSilver(livingEntity, Hand.OFF_HAND)) {
					damage++;
				}
				if (damage > 0) {
					damage(DamageSource.MAGIC, damage);
				}
			}
		}
	}
}
