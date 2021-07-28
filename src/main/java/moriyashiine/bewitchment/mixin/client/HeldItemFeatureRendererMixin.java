package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.entity.living.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3f;
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
	
	@Inject(method = "renderItem", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
	private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callbackInfo) {
		if (entity instanceof WerewolfEntity) {
			matrices.translate((arm == Arm.LEFT ? 0.75f : -0.75f) / 3.5f, 1 / 3f, -7 / 8f);
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45));
		}
		else if (entity instanceof DemonEntity) {
			matrices.translate((arm == Arm.LEFT ? 0.75f : -0.75f) / 24f, 0, -1 / 8f);
		}
		else if (entity instanceof LeonardEntity) {
			matrices.translate((arm == Arm.LEFT ? 0.75f : -0.75f) / 24f, 0, -5.75 / 16f);
		}
		else if (entity instanceof BaphometEntity) {
			matrices.translate((arm == Arm.LEFT ? 0.75f : -0.75f) / 24f, 0, -1 / 4f);
		}
		else if (entity instanceof LilithEntity) {
			matrices.translate((arm == Arm.LEFT ? 0.75f : -0.75f) / 24f, 0, -5.75 / 16f);
		}
		else if (entity instanceof HerneEntity) {
			matrices.translate((arm == Arm.LEFT ? 0.75f : -0.75f) / 24f, 0, -4 / 7f);
		}
	}
}
