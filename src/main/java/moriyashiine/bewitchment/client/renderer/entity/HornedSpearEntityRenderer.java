package moriyashiine.bewitchment.client.renderer.entity;

import moriyashiine.bewitchment.client.model.entity.living.HerneEntityModel;
import moriyashiine.bewitchment.client.renderer.entity.living.GhostEntityRenderer;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class HornedSpearEntityRenderer extends EntityRenderer<HornedSpearEntity> {
	public HornedSpearEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}
	
	@Override
	public Identifier getTexture(HornedSpearEntity silverArrowEntity) {
		return GhostEntityRenderer.EMPTY;
	}
	
	@Override
	public void render(HornedSpearEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		ItemStack spear = entity.spear;
		if (spear.isEmpty()) {
			spear = HerneEntityModel.HORNED_SPEAR;
		}
		matrices.push();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 225));
		matrices.scale(1.5f, 1.5f, 1.5f);
		MinecraftClient.getInstance().getItemRenderer().renderItem(spear, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
