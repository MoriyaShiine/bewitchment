/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.network.packet.SpawnSpecterBangleParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SpecterBangleItem extends TrinketItem {
	public SpecterBangleItem(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof PlayerEntity player && entity.isSneaking() && BewitchmentAPI.drainMagic(player, 1, true)) {
			if (entity.getRandom().nextFloat() < 1 / 40f) {
				BewitchmentAPI.drainMagic(player, 1, false);
			}
			BWComponents.FULL_INVISIBILITY_COMPONENT.maybeGet(player).ifPresent(fullInvisibilityComponent -> {
				if (!fullInvisibilityComponent.isFullInvisible()) {
					fullInvisibilityComponent.setFullInvisible(true);
					entity.setInvisible(true);
				}
			});
			if (entity.getRandom().nextFloat() < 1 / 32f) {
				PlayerLookup.tracking(player).forEach(trackingPlayer -> SpawnSpecterBangleParticlesPacket.send(trackingPlayer, player));
				SpawnSpecterBangleParticlesPacket.send(player, player);
			}
		}
	}
}
