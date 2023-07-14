/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
@Mixin(MobEntityRenderer.class)
public abstract class MobEntityRendererMixin<T extends MobEntity, M extends EntityModel<T>> {
	@Inject(method = "shouldRender(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/client/render/Frustum;DDD)Z", at = @At("RETURN"), cancellable = true)
	private void shouldRender(T mobEntity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ()) {
			BWComponents.FAKE_MOB_COMPONENT.maybeGet(mobEntity).ifPresent(fakeMobComponent -> {
				if (fakeMobComponent.getTarget() != null && !MinecraftClient.getInstance().player.getUuid().equals(fakeMobComponent.getTarget())) {
					callbackInfo.setReturnValue(false);
				}
			});
		}
	}
}
