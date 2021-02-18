package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.interfaces.TrueInvisibleAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;

public class SpecterBangleItem extends TrinketItem {
	public SpecterBangleItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.FEET) && slot.equals(Slots.AGLET);
	}
	
	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (player.isSneaking() && BewitchmentAPI.usePlayerMagic(player, 1, true)) {
			if (!player.world.isClient) {
				if (player.getRandom().nextFloat() < 1 / 40f) {
					BewitchmentAPI.usePlayerMagic(player, 1, false);
				}
				if (!((TrueInvisibleAccessor) player).getTrueInvisible()) {
					((TrueInvisibleAccessor) player).setTrueInvisible(true);
				}
			}
			else if (player.getRandom().nextFloat() < 1 / 6f) {
				player.world.addParticle(ParticleTypes.SMOKE, player.getParticleX(1), player.getY(), player.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
