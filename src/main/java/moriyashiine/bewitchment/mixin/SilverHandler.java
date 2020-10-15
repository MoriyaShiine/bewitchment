package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.SilverArrowEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class SilverHandler extends Entity {
	public SilverHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = {"applyEnchantmentsToDamage"}, at = @At("HEAD"))
	private float modifyDamageOffense(float amount, DamageSource source) {
		if (isWeakToSilver(this) && !(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
			Entity attacker = source.getSource();
			if (isHoldingSilver(attacker, Hand.MAIN_HAND) || attacker instanceof SilverArrowEntity) {
				return amount + 4;
			}
		}
		return amount;
	}
	
	@ModifyVariable(method = {"damage"}, at = @At("HEAD"))
	private float modifyDamageDefense(float amount, DamageSource source) {
		if (!(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
			Entity attacker = source.getSource();
			if (attacker != null && isWeakToSilver(attacker)) {
				int armorPieces = getSilverArmor(this);
				if (armorPieces > 0) {
					attacker.damage(DamageSource.thorns(this), armorPieces);
				}
				return amount * (1 - (0.12f * armorPieces));
			}
		}
		return amount;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void damageEntitiesWeakToSilver(CallbackInfo callbackInfo) {
		if (!world.isClient && isWeakToSilver(this)) {
			int damage = getSilverArmor(this);
			if (isHoldingSilver(this, Hand.MAIN_HAND)) {
				damage++;
			}
			if (isHoldingSilver(this, Hand.OFF_HAND)) {
				damage++;
			}
			if (damage > 0) {
				damage(DamageSource.MAGIC, damage);
			}
		}
	}
	
	private boolean isHoldingSilver(Entity entity, Hand hand) {
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			return BWTags.SILVER_TOOLS.contains(livingEntity.getStackInHand(hand).getItem());
		}
		return false;
	}
	
	private boolean isWeakToSilver(Entity entity) {
		if (entity instanceof LivingEntity) {
			for (Predicate<LivingEntity> predicate : BewitchmentAPI.ADDITIONAL_SILVER_WEAKNESSES) {
				if (predicate.test((LivingEntity) entity)) {
					return true;
				}
			}
		}
		return BWTags.WEAK_TO_SILVER.contains(entity.getType());
	}
	
	private int getSilverArmor(Entity entity) {
		int fin = 0;
		for (ItemStack stack : entity.getArmorItems()) {
			if (BWTags.SILVER_ARMOR.contains(stack.getItem())) {
				fin++;
			}
		}
		return fin;
	}
}
