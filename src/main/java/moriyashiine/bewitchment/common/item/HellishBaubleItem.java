package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class HellishBaubleItem extends TrinketItem {
	public HellishBaubleItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.CHEST) && slot.equals(Slots.NECKLACE);
	}
	
	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (!player.world.isClient && player.age % 20 == 0 && player.isOnFire() && BewitchmentAPI.usePlayerMagic(player, 2, false)) {
			player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, player.getSoundCategory(), 1, 1);
			player.extinguish();
		}
	}
}
