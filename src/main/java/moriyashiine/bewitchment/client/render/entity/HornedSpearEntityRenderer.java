/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity;

import moriyashiine.bewitchment.client.model.entity.living.HerneEntityModel;
import moriyashiine.bewitchment.client.render.entity.living.GhostEntityRenderer;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class HornedSpearEntityRenderer extends EntityRenderer<HornedSpearEntity> {
	public HornedSpearEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(HornedSpearEntity entity) {
		return GhostEntityRenderer.EMPTY;
	}

	@Override
	public void render(HornedSpearEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		ItemStack spear = entity.spear;
		if (spear.isEmpty()) {
			spear = HerneEntityModel.HORNED_SPEAR;
		}
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 225));
		matrices.scale(1.5f, 1.5f, 1.5f);
		MinecraftClient.getInstance().getItemRenderer().renderItem(spear, ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
