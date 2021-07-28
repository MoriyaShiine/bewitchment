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
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class PoppetShelfBlockEntityRenderer implements BlockEntityRenderer<PoppetShelfBlockEntity> {
	@Override
	public void render(PoppetShelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
			float rotation = -direction.asRotation();
			renderRow(direction, rotation, entity.getStack(0), entity.getStack(1), entity.getStack(2), 0.8f, matrices, vertexConsumers, light, overlay);
			renderRow(direction, rotation, entity.getStack(3), entity.getStack(4), entity.getStack(5), 0.5f, matrices, vertexConsumers, light, overlay);
			renderRow(direction, rotation, entity.getStack(6), entity.getStack(7), entity.getStack(8), 0.2f, matrices, vertexConsumers, light, overlay);
		}
	}
	
	private static void renderRow(Direction direction, float rotation, ItemStack one, ItemStack two, ItemStack three, float yOffset, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!one.isEmpty()) {
			double xOffset = direction == Direction.NORTH ? 3 / 16f : direction == Direction.SOUTH ? 13 / 16f : direction == Direction.EAST ? 0.85 : 0.15;
			double zOffset = direction == Direction.NORTH ? 0.15 : direction == Direction.SOUTH ? 0.85 : direction == Direction.EAST ? 3 / 16f : 13 / 16f;
			matrices.push();
			matrices.translate(xOffset, yOffset, zOffset);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
			matrices.scale(1 / 5f, 1 / 5f, 1 / 5f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(one, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
			if (!two.isEmpty()) {
				xOffset = direction == Direction.NORTH || direction == Direction.SOUTH ? 8 / 16f : direction == Direction.EAST ? 0.85 : 0.15;
				zOffset = direction == Direction.NORTH ? 0.15 : direction == Direction.SOUTH ? 0.85 : direction == Direction.EAST ? 8 / 16f : 8 / 16f;
				matrices.push();
				matrices.translate(xOffset, yOffset, zOffset);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				matrices.scale(1 / 5f, 1 / 5f, 1 / 5f);
				MinecraftClient.getInstance().getItemRenderer().renderItem(two, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				matrices.pop();
				if (!three.isEmpty()) {
					xOffset = direction == Direction.NORTH ? 13 / 16f : direction == Direction.SOUTH ? 3 / 16f : direction == Direction.EAST ? 0.85 : 0.15;
					zOffset = direction == Direction.NORTH ? 0.15 : direction == Direction.SOUTH ? 0.85 : direction == Direction.EAST ? 13 / 16f : 3 / 16f;
					matrices.push();
					matrices.translate(xOffset, yOffset, zOffset);
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
					matrices.scale(1 / 5f, 1 / 5f, 1 / 5f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(three, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
					matrices.pop();
				}
			}
		}
	}
}
