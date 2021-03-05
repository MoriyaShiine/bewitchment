package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * Called whenever a Poppet is used; includes active use of curse poppets
 */
public interface PoppetCallback {
    Event<PoppetCallback> EVENT = EventFactory.createArrayBacked(PoppetCallback.class,
            (listeners) -> (user, stack) -> {
                for (PoppetCallback listener : listeners) {
                    ActionResult result = listener.use(user, stack);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult use(LivingEntity directUser, ItemStack poppet);
}
