/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class HellishBaubleItem extends TrinketItem {
	public HellishBaubleItem(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity player && !entity.getWorld().isClient && entity.age % 10 == 0 && entity.isOnFire()) {
			StatusEffectInstance fireResistance = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 300, 0, true, false);
			if (entity.canHaveStatusEffect(fireResistance) && !entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && BewitchmentAPI.drainMagic(player, 1, false)) {
				entity.addStatusEffect(fireResistance);
			}
		}
	}
}
