package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface AllowVampireHeal {
	Event<AllowVampireHeal> EVENT = createArrayBacked(AllowVampireHeal.class, listeners -> (player, isPledgedToLilith) -> {
		for (AllowVampireHeal listener : listeners) {
			if (!listener.allowHeal(player, isPledgedToLilith)) {
				return false;
			}
		}
		return true;
	});
	
	boolean allowHeal(PlayerEntity player, boolean isPledgedToLilith);
}
