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
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WitchAltarBlockEntityRenderer implements BlockEntityRenderer<WitchAltarBlockEntity> {
	@Override
	public void render(WitchAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
			float rotation = -direction.asRotation();
			ItemStack sword = entity.getStack(0);
			if (!sword.isEmpty()) {
				matrices.push();
				if (BWTags.SKULLS.contains(sword.getItem())) {
					matrices.translate(direction == Direction.NORTH || direction == Direction.EAST ? 0.25 : 0.75, 1.125, direction == Direction.NORTH || direction == Direction.WEST ? 0.75 : 0.25);
					if (direction == Direction.NORTH) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
					}
					if (direction == Direction.EAST) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
					}
					if (direction == Direction.WEST) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270));
					}
					matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vec3f.POSITIVE_Z : Vec3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
					matrices.scale(0.5f, 0.5f, 0.5f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(sword, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				}
				else if (!(sword.getItem() instanceof BlockItem)) {
					matrices.translate(direction == Direction.NORTH || direction == Direction.EAST ? 0.25 : 0.75, 1.01, direction == Direction.NORTH || direction == Direction.WEST ? 0.75 : 0.25);
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
					matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vec3f.POSITIVE_Z : Vec3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
					matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(sword, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				}
				else {
					matrices.translate(direction == Direction.SOUTH || direction == Direction.WEST ? 0.5f : 0, 1, direction == Direction.NORTH || direction == Direction.WEST ? 0.5f : 0);
					matrices.scale(0.5f, 0.5f, 0.5f);
					BlockState state = ((BlockItem) sword.getItem()).getBlock().getDefaultState();
					if (state.getProperties().contains(Properties.HORIZONTAL_FACING)) {
						state = state.with(Properties.HORIZONTAL_FACING, direction.getOpposite());
					}
					if (state.getProperties().contains(Properties.FACING)) {
						state = state.with(Properties.FACING, Direction.UP);
					}
					MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(state), 1, 1, 1, light, overlay);
				}
				matrices.pop();
			}
			ItemStack pentacle = entity.getStack(1);
			if (!pentacle.isEmpty()) {
				matrices.push();
				double x = direction == Direction.NORTH || direction == Direction.SOUTH ? 0.5 : direction == Direction.EAST ? 0.75 : 0.25;
				double z = direction == Direction.EAST || direction == Direction.WEST ? 0.5 : direction == Direction.NORTH ? 0.25 : 0.75;
				if (BWTags.SKULLS.contains(pentacle.getItem())) {
					matrices.translate(x, 1.125, z);
					if (direction == Direction.NORTH) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
					}
					if (direction == Direction.EAST) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
					}
					if (direction == Direction.WEST) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270));
					}
					matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vec3f.POSITIVE_Z : Vec3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
					matrices.scale(0.5f, 0.5f, 0.5f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(pentacle, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				}
				else if (!(pentacle.getItem() instanceof BlockItem)) {
					matrices.translate(x, 1.01, z);
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
					matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vec3f.POSITIVE_Z : Vec3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
					matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(pentacle, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				}
				else {
					matrices.translate(direction == Direction.NORTH || direction == Direction.SOUTH ? 0.25f : direction == Direction.EAST ? 0.35f : 0.15f, 1, direction == Direction.EAST || direction == Direction.WEST ? 0.25f : direction == Direction.NORTH ? 0.15f : 0.35f);
					matrices.scale(0.5f, 0.5f, 0.5f);
					BlockState state = ((BlockItem) pentacle.getItem()).getBlock().getDefaultState();
					if (state.getProperties().contains(Properties.HORIZONTAL_FACING)) {
						state = state.with(Properties.HORIZONTAL_FACING, direction.getOpposite());
					}
					if (state.getProperties().contains(Properties.FACING)) {
						state = state.with(Properties.FACING, Direction.UP);
					}
					MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(state), 1, 1, 1, light, overlay);
				}
				matrices.pop();
			}
			ItemStack wand = entity.getStack(2);
			if (!wand.isEmpty()) {
				matrices.push();
				if (BWTags.SKULLS.contains(wand.getItem())) {
					matrices.translate(direction == Direction.NORTH || direction == Direction.WEST ? 0.75f : 0.25f, 1.125f, direction == Direction.NORTH || direction == Direction.EAST ? 0.75f : 0.25f);
					if (direction == Direction.NORTH) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
					}
					if (direction == Direction.EAST) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
					}
					if (direction == Direction.WEST) {
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270));
					}
					matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vec3f.POSITIVE_Z : Vec3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
					matrices.scale(0.5f, 0.5f, 0.5f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(wand, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				}
				else if (!(wand.getItem() instanceof BlockItem)) {
					matrices.translate(direction == Direction.NORTH || direction == Direction.WEST ? 0.75 : 0.25, 1.01, direction == Direction.NORTH || direction == Direction.EAST ? 0.75 : 0.25);
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
					matrices.multiply((direction == Direction.NORTH || direction == Direction.SOUTH ? Vec3f.POSITIVE_Z : Vec3f.NEGATIVE_Z).getDegreesQuaternion(rotation));
					matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(wand, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				}
				else {
					matrices.translate(direction == Direction.NORTH || direction == Direction.WEST ? 0.5f : 0, 1, direction == Direction.NORTH || direction == Direction.EAST ? 0.5f : 0);
					matrices.scale(0.5f, 0.5f, 0.5f);
					BlockState state = ((BlockItem) wand.getItem()).getBlock().getDefaultState();
					if (state.getProperties().contains(Properties.HORIZONTAL_FACING)) {
						state = state.with(Properties.HORIZONTAL_FACING, direction.getOpposite());
					}
					if (state.getProperties().contains(Properties.FACING)) {
						state = state.with(Properties.FACING, Direction.UP);
					}
					MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(state), 1, 1, 1, light, overlay);
				}
				matrices.pop();
			}
		}
	}
}
