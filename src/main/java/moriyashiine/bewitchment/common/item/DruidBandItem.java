/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class DruidBandItem extends TrinketItem {
	public DruidBandItem(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity player && !entity.world.isClient && entity.age % 10 == 0 & entity.world.getBlockState(entity.getBlockPos().down()).isIn(BWTags.NATURAL_TERRAIN)) {
			StatusEffectInstance speed = new StatusEffectInstance(StatusEffects.SPEED, 300, 0, true, false);
			StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0, true, false);
			boolean canApply = entity.canHaveStatusEffect(speed) && !entity.hasStatusEffect(StatusEffects.SPEED);
			canApply |= entity.canHaveStatusEffect(regeneration) && !entity.hasStatusEffect(StatusEffects.REGENERATION);
			if (canApply && BewitchmentAPI.drainMagic(player, 1, false)) {
				entity.addStatusEffect(speed);
				entity.addStatusEffect(regeneration);
			}
		}
	}
}
