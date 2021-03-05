package moriyashiine.bewitchment.api.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public interface PlayerRerenderCallback {
    Event<PlayerRerenderCallback> EVENT = EventFactory.createArrayBacked(PlayerRerenderCallback.class,
            (listeners) -> (player, renderEntity) -> {
                for (PlayerRerenderCallback listener : listeners) {
                    ActionResult result = listener.rerender(player, renderEntity);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult rerender(PlayerEntity player, LivingEntity renderEntity);
}
