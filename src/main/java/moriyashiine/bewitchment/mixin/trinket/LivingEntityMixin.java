package moriyashiine.bewitchment.mixin.trinket;

import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.item.PricklyBeltItem;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public int hurtTime;
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient) {
			Entity directSource = source.getSource();
			if (amount > 0 && hurtTime == 0) {
				if ((Object) this instanceof PlayerEntity && directSource instanceof LivingEntity) {
					Inventory trinketsInventory = TrinketsApi.getTrinketsInventory((PlayerEntity) (Object) this);
					if (trinketsInventory.containsAny(Collections.singleton(BWObjects.PRICKLY_BELT))) {
						ItemStack belt = null;
						for (int i = 0; i < trinketsInventory.size(); i++) {
							if (trinketsInventory.getStack(i).getItem() instanceof PricklyBeltItem) {
								belt = trinketsInventory.getStack(i);
								break;
							}
						}
						if (belt != null) {
							if (belt.hasTag() && belt.getTag().getInt("PotionUses") > 0) {
								boolean used = false;
								List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(belt);
								for (StatusEffectInstance effect : effects) {
									if (((StatusEffectAccessor) effect.getEffectType()).bw_getType() == StatusEffectType.HARMFUL) {
										if (!(((LivingEntity) directSource).hasStatusEffect(effect.getEffectType())) && BewitchmentAPI.drainMagic((PlayerEntity) (Object) this, 2, true) && ((LivingEntity) directSource).addStatusEffect(effect)) {
											used = true;
										}
									}
									else if (!hasStatusEffect(effect.getEffectType()) && BewitchmentAPI.drainMagic((PlayerEntity) (Object) this, 2, true) && addStatusEffect(effect)) {
										if (belt.getTag().contains("PolymorphUUID") && this instanceof PolymorphAccessor) {
											PolymorphAccessor polymorphAccessor = (PolymorphAccessor) this;
											polymorphAccessor.setPolymorphUUID(belt.getTag().getUuid("PolymorphUUID"));
											polymorphAccessor.setPolymorphName(belt.getTag().getString("PolymorphName"));
										}
										used = true;
									}
								}
								if (used) {
									belt.getTag().putInt("PotionUses", belt.getTag().getInt("PotionUses") - 1);
									if (belt.getTag().getInt("PotionUses") <= 0) {
										belt.getOrCreateTag().put("CustomPotionEffects", new ListTag());
										if (belt.getTag().contains("PolymorphUUID")) {
											belt.getTag().remove("PolymorphUUID");
											belt.getTag().remove("PolymorphName");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Inject(method = "damage", at = @At("RETURN"))
	private void damageReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && !world.isClient && source.getSource() instanceof PlayerEntity && ((PlayerEntity) source.getSource()).getMainHandStack().isEmpty() && TrinketsApi.getTrinketsInventory((PlayerEntity) source.getSource()).containsAny(Collections.singleton(BWObjects.ZEPHYR_HARNESS)) && BewitchmentAPI.drainMagic((PlayerEntity) source.getSource(), 1, false)) {
			addVelocity(0, 2 / 3f, 0);
		}
	}
}
