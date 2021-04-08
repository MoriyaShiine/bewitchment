package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public final class BloodSuckEvents {
	public static final Event<OnBloodSuck> ON_BLOOD_SUCK = createArrayBacked(OnBloodSuck.class, listeners -> (player, target, bloodToGive) -> {
		for (OnBloodSuck listener : listeners) {
			listener.onBloodSuck(player, target, bloodToGive);
		}
	});
	
	public static final Event<SetBloodAmount> BLOOD_AMOUNT = createArrayBacked(SetBloodAmount.class, listeners -> (player, target, bloodToGive) -> {
		int result = bloodToGive;
		for (SetBloodAmount listener : listeners) {
			result = Math.max(result, listener.onBloodSuck(player, target, result));
		}
		return result;
	});
	
	public interface OnBloodSuck {
		void onBloodSuck(PlayerEntity player, LivingEntity target, int bloodToGive);
	}
	
	public interface SetBloodAmount {
		int onBloodSuck(PlayerEntity player, LivingEntity target, int currentBloodToGive);
	}
}