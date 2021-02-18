package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class DruidBandItem extends TrinketItem {
	public DruidBandItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.FEET) && slot.equals(Slots.AGLET);
	}
	
	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (!player.world.isClient && player.age % 10 == 0 && player.world.getBlockState(player.getBlockPos().down()).getBlock() instanceof Fertilizable) {
			StatusEffectInstance speed = new StatusEffectInstance(StatusEffects.SPEED, 200, 0, true, false);
			StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0, true, false);
			boolean canApply = player.canHaveStatusEffect(speed) && !player.hasStatusEffect(StatusEffects.SPEED);
			canApply |= player.canHaveStatusEffect(regeneration) && !player.hasStatusEffect(StatusEffects.REGENERATION);
			if (canApply && BewitchmentAPI.usePlayerMagic(player, 1, false)) {
				player.addStatusEffect(speed);
				player.addStatusEffect(regeneration);
			}
		}
	}
}
