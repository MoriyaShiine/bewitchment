package moriyashiine.bewitchment.api.client.renderer;

import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

@Environment(EnvType.CLIENT)
public abstract class BroomEntityRenderer<T extends BroomEntity> extends EntityRenderer<T> {
	private final BroomEntityModel model = new BroomEntityModel();
	
	public BroomEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher);
	}
	
	@Override
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.translate(0, -1, 0);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90 - yaw + 90));
		matrices.translate(0, 0, -0.35);
		model.setAngles(entity, yaw, 0, entity.age + entity.getEntityId(), 0, 0);
		model.render(matrices, vertexConsumers.getBuffer(model.getLayer(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
