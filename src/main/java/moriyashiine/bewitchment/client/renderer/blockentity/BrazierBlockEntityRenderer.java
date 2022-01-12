/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class BrazierBlockEntityRenderer implements BlockEntityRenderer<BrazierBlockEntity> {
	@Override
	public void render(BrazierBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		double yOffset = entity.getCachedState().get(Properties.HANGING) ? -0.815 : 0;
		matrices.translate(0.5, 0.925 + yOffset, 0.5);
		matrices.scale(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < 4; i++) {
			matrices.translate(0, 0.06, 0);
			matrices.push();
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((90 + (180 * i)) % 360));
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90 * i));
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(i), ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
		}
	}
}
