package moriyashiine.bewitchment.api.client.event;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public interface RenderCauldronCallback {
    Event<RenderCauldronCallback> EVENT = EventFactory.createArrayBacked(RenderCauldronCallback.class,
            (listeners) -> (entity, tickDelta, matrices, vertexConsumers, light, overlay, level) -> {
                for (RenderCauldronCallback listener : listeners) {
                    ActionResult result = listener.render(entity, tickDelta, matrices, vertexConsumers, light, overlay, level);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult render(WitchCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, int level);
}
