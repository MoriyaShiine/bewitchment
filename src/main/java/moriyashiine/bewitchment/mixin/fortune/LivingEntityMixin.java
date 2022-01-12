/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.fortune;

import moriyashiine.bewitchment.api.component.FortuneComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWFortunes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient && amount > 0 && source.isProjectile() && source.getAttacker() instanceof PlayerEntity player) {
			FortuneComponent fortuneComponent = BWComponents.FORTUNE_COMPONENT.get(player);
			if (fortuneComponent.getFortune() != null && fortuneComponent.getFortune().fortune == BWFortunes.HAWKEYE) {
				fortuneComponent.getFortune().duration = 0;
				amount *= 3;
			}
		}
		return amount;
	}
}
