package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class HellishBaubleItem extends TrinketItem {
	public HellishBaubleItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity player && !player.world.isClient && player.age % 20 == 0 && player.isOnFire() && BewitchmentAPI.drainMagic(player, 1, false)) {
			player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, player.getSoundCategory(), 1, 1);
			player.extinguish();
		}
	}
}
