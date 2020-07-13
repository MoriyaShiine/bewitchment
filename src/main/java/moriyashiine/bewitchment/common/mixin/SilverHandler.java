package moriyashiine.bewitchment.common.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.SilverArrowEntity;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class SilverHandler extends Entity {
	private static final Tag<EntityType<?>> WEAK_TO_SILVER = TagRegistry.entityType(new Identifier(Bewitchment.MODID, "weak_to_silver"));
	
	private static final Tag<Item> SILVER_TOOLS = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_tools"));
	private static final Tag<Item> SILVER_ARMOR = TagRegistry.item(new Identifier(Bewitchment.MODID, "silver_armor"));
	
	public SilverHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = {"damage"}, at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		Entity attacker = source.getSource();
		if (isWeakToSilver(this)) {
			if (isHoldingSilver(attacker, Hand.MAIN_HAND, true) || attacker instanceof SilverArrowEntity) {
				return amount * 1.5f;
			}
		}
		if (attacker != null && isWeakToSilver(attacker)) {
			byte armorPieces = getSilverArmor(this);
			attacker.damage(DamageSource.thorns(this), armorPieces);
			return amount * (1 - (0.15f * armorPieces));
		}
		return amount;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void damageEntitiesWeakToSilver(CallbackInfo callbackInfo) {
		if (!world.isClient && isWeakToSilver(this)) {
			byte damage = getSilverArmor(this);
			if (isHoldingSilver(this, Hand.MAIN_HAND, false)) {
				damage++;
			}
			if (isHoldingSilver(this, Hand.OFF_HAND, false)) {
				damage++;
			}
			if (damage > 0) {
				damage(DamageSource.MAGIC, damage);
			}
		}
	}
	
	private boolean isHoldingSilver(Entity entity, Hand hand, boolean checkSwinging) {
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			return SILVER_TOOLS.contains(livingEntity.getStackInHand(hand).getItem()) && (!checkSwinging || (livingEntity.handSwinging && livingEntity.preferredHand == hand));
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
		return WEAK_TO_SILVER.contains(entity.getType());
	}
	
	private byte getSilverArmor(Entity entity) {
		byte fin = 0;
		for (ItemStack stack : entity.getArmorItems()) {
			if (SILVER_ARMOR.contains(stack.getItem())) {
				fin++;
			}
		}
		return fin;
	}
}