package moriyashiine.bewitchment.mixin.trinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.component.PolymorphComponent;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
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
			if (amount > 0 && hurtTime == 0) {
				if ((Object) this instanceof PlayerEntity player && source.getSource() instanceof LivingEntity livingSource) {
					List<Pair<SlotReference, ItemStack>> component = TrinketsApi.getTrinketComponent(player).get().getEquipped(BWObjects.PRICKLY_BELT);
					if (!component.isEmpty()) {
						ItemStack belt = component.get(0).getRight();
						if (belt.hasNbt() && belt.getNbt().getInt("PotionUses") > 0) {
							boolean used = false;
							List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(belt);
							for (StatusEffectInstance effect : effects) {
								if (effect.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
									if (!livingSource.hasStatusEffect(effect.getEffectType()) && BewitchmentAPI.drainMagic(player, 2, true) && livingSource.addStatusEffect(effect)) {
										used = true;
									}
								}
								else if (!hasStatusEffect(effect.getEffectType()) && BewitchmentAPI.drainMagic(player, 2, true) && addStatusEffect(effect)) {
									if (belt.getNbt().contains("PolymorphUUID")) {
										PolymorphComponent.maybeGet(this).ifPresent(polymorphComponent -> {
											polymorphComponent.setUuid(belt.getNbt().getUuid("PolymorphUUID"));
											polymorphComponent.setName(belt.getNbt().getString("PolymorphName"));
										});
									}
									used = true;
								}
							}
							if (used) {
								belt.getNbt().putInt("PotionUses", belt.getNbt().getInt("PotionUses") - 1);
								if (belt.getNbt().getInt("PotionUses") <= 0) {
									belt.getOrCreateNbt().put("CustomPotionEffects", new NbtList());
									if (belt.getNbt().contains("PolymorphUUID")) {
										belt.getNbt().remove("PolymorphUUID");
										belt.getNbt().remove("PolymorphName");
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
		if (callbackInfo.getReturnValue() && !world.isClient && source.getSource() instanceof PlayerEntity player && player.getMainHandStack().isEmpty() && TrinketsApi.getTrinketComponent(player).get().isEquipped(BWObjects.ZEPHYR_HARNESS) && BewitchmentAPI.drainMagic(player, 1, false)) {
			addVelocity(0, 2 / 3f, 0);
		}
	}
}
