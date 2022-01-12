/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class PoppetShelfBlockEntityRenderer implements BlockEntityRenderer<PoppetShelfBlockEntity> {
	@Override
	public void render(PoppetShelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
		float rotation = -direction.asRotation();
		matrices.translate(0.5f, 0, 0.5f);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
		matrices.scale(1 / 5f, 1 / 5f, 1 / 5f);
		renderRow(entity.clientInventory.get(0), entity.clientInventory.get(1), entity.clientInventory.get(2), 0.8f, matrices, vertexConsumers, light, overlay);
		renderRow(entity.clientInventory.get(3), entity.clientInventory.get(4), entity.clientInventory.get(5), 0.5f, matrices, vertexConsumers, light, overlay);
		renderRow(entity.clientInventory.get(6), entity.clientInventory.get(7), entity.clientInventory.get(8), 0.2f, matrices, vertexConsumers, light, overlay);
	}

	private static void renderRow(ItemStack one, ItemStack two, ItemStack three, float yOffset, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		double xOffset = 0.32 * 5;
		double zOffset = 0.45 * 5;
		yOffset *= 5;
		matrices.translate(0, yOffset, zOffset);
		MinecraftClient.getInstance().getItemRenderer().renderItem(two, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
		matrices.translate(xOffset, 0, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(one, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
		matrices.translate(-xOffset * 2, 0, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(three, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
		matrices.pop();
	}
}
