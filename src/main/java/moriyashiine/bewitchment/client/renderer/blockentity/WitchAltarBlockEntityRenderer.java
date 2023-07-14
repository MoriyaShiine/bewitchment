/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WitchAltarBlockEntityRenderer implements BlockEntityRenderer<WitchAltarBlockEntity> {
	@Override
	public void render(WitchAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.translate(0.5, 1, 0.5);
		Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
		float rotation = direction.asRotation() + ((direction == Direction.WEST || direction == Direction.EAST) ? 180 : 0); // Offset rotation when facing WEST or EAST
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
		ItemStack sword = entity.getStack(0);
		ItemStack pentacle = entity.getStack(1);
		ItemStack wand = entity.getStack(2);
		matrices.push();
		renderStack(entity.getWorld(), matrices, vertexConsumers, light, overlay, 0.25f, -0.25f, sword, direction);
		matrices.pop();
		matrices.push();
		renderStack(entity.getWorld(), matrices, vertexConsumers, light, overlay, 0f, 0.25f, pentacle, direction);
		matrices.pop();
		matrices.push();
		renderStack(entity.getWorld(), matrices, vertexConsumers, light, overlay, -0.25f, -0.25f, wand, direction);
		matrices.pop();
	}

	private void renderStack(World world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float x, float z, ItemStack stack, Direction direction) {
		if (stack.isIn(BWTags.SKULLS)) {
			matrices.translate(x, 0.125, z);
			matrices.scale(0.5f, 0.5f, 0.5f);
		} else if (stack.getItem() instanceof BlockItem blockItem) {
			matrices.translate(x + 0.25, 0, z - 0.25);
			matrices.scale(0.5f, 0.5f, 0.5f);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
			BlockState state = blockItem.getBlock().getDefaultState();
			if (state.getProperties().contains(Properties.HORIZONTAL_FACING)) {
				state = state.with(Properties.HORIZONTAL_FACING, direction.getOpposite());
			}
			if (state.getProperties().contains(Properties.FACING)) {
				state = state.with(Properties.FACING, Direction.UP);
			}
			MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(state), 1, 1, 1, light, overlay);
			return;
		} else {
			matrices.translate(x, 0.01, z);
			matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
		}
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
	}
}
