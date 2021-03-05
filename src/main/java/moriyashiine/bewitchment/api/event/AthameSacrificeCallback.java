package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
/**
 * Fired whenever an Athame kills an Entity
 */
public interface AthameSacrificeCallback {
    Event<AthameSacrificeCallback> EVENT = EventFactory.createArrayBacked(AthameSacrificeCallback.class,
            (listeners) -> (stack, target, attacker) -> {
                for (AthameSacrificeCallback listener : listeners) {
                    ActionResult result = listener.sacrifice(stack, target, attacker);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });


    ActionResult sacrifice(ItemStack stack, LivingEntity target, LivingEntity attacker);
}
