package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

/**
 * Called in GrotesqueStewItem when pledging to a demon
 */
public interface PledgeCallback {
    Event<PledgeCallback> EVENT = EventFactory.createArrayBacked(PledgeCallback.class,
            (listeners) -> (user, target, stack) -> {
                for (PledgeCallback listener : listeners) {
                    ActionResult result = listener.pledge(user, target, stack);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult pledge(LivingEntity user, LivingEntity target, @Nullable ItemStack stack);
}
