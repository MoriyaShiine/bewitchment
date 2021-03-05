package moriyashiine.bewitchment.api.event;

import moriyashiine.bewitchment.api.registry.Curse;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
/**
 * Fired whenever another entity is cursed using a Brazier
 */
public interface CurseCallback {
    Event<CurseCallback> EVENT = EventFactory.createArrayBacked(CurseCallback.class,
            (listeners) -> (caster, target, curse, brazier) -> {
                for (CurseCallback listener : listeners) {
                    ActionResult result = listener.curse(caster, target, curse, brazier);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    /**
     * @param source the source of the curse; usually either an ItemStack (curse poppet) or a BrazierBlockEntity
     */
    ActionResult curse(LivingEntity caster, Entity target, Curse curse, Object source);
}
