package moriyashiine.bewitchment.api.event;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

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

    ActionResult curse(PlayerEntity caster, Entity target, Curse curse, BrazierBlockEntity brazier);
}
