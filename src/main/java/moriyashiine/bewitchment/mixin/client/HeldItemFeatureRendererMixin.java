package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M> {
	public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}
	
	@Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 0, target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	private void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo callbackInfo) {
		if (entity instanceof LeonardEntity) {
			matrices.translate(-0.08, -0.2, 0);
		}
		else if (entity instanceof BaphometEntity) {
			matrices.translate(0, -0.6, 0);
		}
		else if (entity instanceof HerneEntity) {
			matrices.translate(-0.14, -0.6, 0);
		}
	}
}
