/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class PoppetShelfBlockEntityRenderer implements BlockEntityRenderer<PoppetShelfBlockEntity> {
	@Override
	public void render(PoppetShelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
		float rotation = -direction.asRotation();
		matrices.translate(0.5f, 0, 0.5f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
		matrices.scale(1 / 5f, 1 / 5f, 1 / 5f);
		renderRow(entity.getWorld(), entity.clientInventory.get(0), entity.clientInventory.get(1), entity.clientInventory.get(2), 0.8f, matrices, vertexConsumers, light, overlay);
		renderRow(entity.getWorld(), entity.clientInventory.get(3), entity.clientInventory.get(4), entity.clientInventory.get(5), 0.5f, matrices, vertexConsumers, light, overlay);
		renderRow(entity.getWorld(), entity.clientInventory.get(6), entity.clientInventory.get(7), entity.clientInventory.get(8), 0.2f, matrices, vertexConsumers, light, overlay);
	}

	private static void renderRow(World world, ItemStack one, ItemStack two, ItemStack three, float yOffset, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		double xOffset = 0.32 * 5;
		double zOffset = 0.45 * 5;
		yOffset *= 5;
		matrices.translate(0, yOffset, zOffset);
		MinecraftClient.getInstance().getItemRenderer().renderItem(two, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
		matrices.translate(xOffset, 0, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(one, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
		matrices.translate(-xOffset * 2, 0, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(three, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
		matrices.pop();
	}
}
