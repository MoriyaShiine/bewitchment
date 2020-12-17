package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WitchAltarBlockEntityRenderer extends BlockEntityRenderer<WitchAltarBlockEntity> {
	public WitchAltarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void render(WitchAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			BlockPos pos = entity.getPos();
			Direction direction = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING);
			float rotation = -direction.asRotation();
			boolean block;
			ItemStack sword = entity.getStack(0);
			if (!sword.isEmpty()) {
				block = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(sword.getItem()).hasDepth();
				matrices.push();
				matrices.translate(direction == Direction.NORTH || direction == Direction.EAST ? 0.25 : 0.75, block ? 0.98 : 1.0125, direction == Direction.NORTH || direction == Direction.WEST ? 0.75 : 0.25);
				if (!block) {
					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
				}
				else {
					matrices.translate(0, 0.145, 0);
					if (direction == Direction.NORTH) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
					}
					if (direction == Direction.EAST) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
					}
					if (direction == Direction.WEST) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270));
					}
				}
				matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vector3f.POSITIVE_Z : Vector3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
				float scale = block ? 0.5f : 1 / 3f;
				matrices.scale(scale, scale, scale);
				MinecraftClient.getInstance().getItemRenderer().renderItem(sword, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers);
				matrices.pop();
			}
			ItemStack pentacle = entity.getStack(1);
			if (!pentacle.isEmpty()) {
				block = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(pentacle.getItem()).hasDepth();
				matrices.push();
				matrices.translate(direction == Direction.NORTH || direction == Direction.SOUTH ? 0.5 : direction == Direction.EAST ? 0.75 : 0.25, block ? 1.025 : 1.01, direction == Direction.EAST || direction == Direction.WEST ? 0.5 : direction == Direction.NORTH ? 0.25 : 0.75);
				if (!block) {
					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
				}
				else {
					matrices.translate(0, 0.1, 0);
					if (direction == Direction.NORTH) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
					}
					if (direction == Direction.EAST) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
					}
					if (direction == Direction.WEST) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270));
					}
				}
				matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vector3f.POSITIVE_Z : Vector3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
				float scale = block ? 0.5f : 1 / 3f;
				matrices.scale(scale, scale, scale);
				MinecraftClient.getInstance().getItemRenderer().renderItem(pentacle, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers);
				matrices.pop();
			}
			ItemStack wand = entity.getStack(2);
			if (!wand.isEmpty()) {
				block = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(wand.getItem()).hasDepth();
				matrices.push();
				matrices.translate(direction == Direction.NORTH || direction == Direction.WEST ? 0.75 : 0.25, 1.01, direction == Direction.NORTH || direction == Direction.EAST ? 0.75 : 0.25);
				if (!block) {
					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
				}
				else {
					matrices.translate(0, 0.115, 0);
					if (direction == Direction.NORTH) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
					}
					if (direction == Direction.EAST) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
					}
					if (direction == Direction.WEST) {
						matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270));
					}
				}
				matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vector3f.POSITIVE_Z : Vector3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
				float scale = block ? 0.5f : 1 / 3f;
				matrices.scale(scale, scale, scale);
				MinecraftClient.getInstance().getItemRenderer().renderItem(wand, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers);
				matrices.pop();
			}
		}
	}
}
