package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public final class ReviveEvents {
	public static final Event<OnRevive> ON_REVIVE = createArrayBacked(OnRevive.class, listeners -> (player, source, poppet) -> {
		for (OnRevive listener : listeners) {
			listener.onRevive(player, source, poppet);
		}
	});
	
	public static final Event<CancelRevive> CANCEL_REVIVE = createArrayBacked(CancelRevive.class, listeners -> (player, source, poppet) -> {
		for (CancelRevive listener : listeners) {
			if (listener.shouldCancel(player, source, poppet)) {
				return true;
			}
		}
		return false;
	});
	
	public interface OnRevive {
		void onRevive(PlayerEntity player, DamageSource source, ItemStack poppet);
	}
	
	public interface CancelRevive {
		boolean shouldCancel(PlayerEntity player, DamageSource source, ItemStack poppet);
	}
}