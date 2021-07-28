package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.block.Fertilizable;
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
		if (entity instanceof PlayerEntity player && !player.world.isClient && player.age % 10 == 0 && player.world.getBlockState(player.getBlockPos().down()).getBlock() instanceof Fertilizable) {
			StatusEffectInstance speed = new StatusEffectInstance(StatusEffects.SPEED, 300, 0, true, false);
			StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0, true, false);
			boolean canApply = player.canHaveStatusEffect(speed) && !player.hasStatusEffect(StatusEffects.SPEED);
			canApply |= player.canHaveStatusEffect(regeneration) && !player.hasStatusEffect(StatusEffects.REGENERATION);
			if (canApply && BewitchmentAPI.drainMagic(player, 1, false)) {
				player.addStatusEffect(speed);
				player.addStatusEffect(regeneration);
			}
		}
	}
}
