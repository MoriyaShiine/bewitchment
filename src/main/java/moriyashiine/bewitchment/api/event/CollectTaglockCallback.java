package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * Fired when a player tries to get a taglock from another living
 */
public interface CollectTaglockCallback {
    Event<CollectTaglockCallback> EVENT = EventFactory.createArrayBacked(CollectTaglockCallback.class,
            (listeners) -> (user, target, visibility, bed, stack) -> {
                for (CollectTaglockCallback listener : listeners) {
                    ActionResult result = listener.collect(user, target, visibility, bed, stack);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult collect(LivingEntity user, LivingEntity target, boolean checkVisibility, boolean bed, ItemStack stack);
}
