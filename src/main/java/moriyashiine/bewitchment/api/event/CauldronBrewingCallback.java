package moriyashiine.bewitchment.api.event;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
/**
 * Fired whenever an ItemStack is created using a Witches Cauldron
 */
public interface CauldronBrewingCallback {
    Event<CauldronBrewingCallback> EVENT = EventFactory.createArrayBacked(CauldronBrewingCallback.class,
            (listeners) -> (user, cauldron, stack) -> {
                ItemStack original = stack;
                for (CauldronBrewingCallback listener : listeners) {
                    ItemStack result = listener.brew(user, cauldron, stack);

                    if (!result.equals(original)) {
                        return result;
                    }
                }
                return original;
            });

    ItemStack brew(PlayerEntity user, WitchCauldronBlockEntity cauldron, @Nullable ItemStack stack);
}
