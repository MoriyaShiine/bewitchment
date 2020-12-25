package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public class WitchCauldronBlockEntityRenderer extends BlockEntityRenderer<WitchCauldronBlockEntity> {
	private static final List<RenderLayer> PORTAL_LAYERS = IntStream.range(1, 16).mapToObj((i) -> RenderLayer.getEndPortal(i + 1)).collect(Collectors.toList());
	private static final Sprite FLUID = MinecraftClient.getInstance().getBlockRenderManager().getModel(Blocks.WATER.getDefaultState()).getSprite();
	private static final float[] HEIGHT = {0, 0.25f, 0.4375f, 0.625f};
	
	public WitchCauldronBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public void render(WitchCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			BlockPos pos = entity.getPos();
			renderName(entity, pos, matrices, vertexConsumers, light);
			int level = world.getBlockState(pos).get(WitchCauldronBlock.LEVEL);
			if (level > 0) {
				matrices.push();
				matrices.translate(0, HEIGHT[level], 0);
				if (entity.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					renderPortal(entity, pos, matrices, vertexConsumers);
				}
				renderWater(entity, matrices, vertexConsumers.getBuffer(RenderLayer.getTranslucent()), light, overlay);
				matrices.pop();
			}
		}
	}
	
	private void renderName(WitchCauldronBlockEntity entity, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (entity.hasCustomName()) {
			if (pos.getSquaredDistance(dispatcher.camera.getPos(), true) <= 256) {
				matrices.push();
				matrices.translate(0.5, 1, 0.5);
				matrices.multiply(dispatcher.camera.getRotation());
				matrices.scale(-0.025f, -0.025f, 0.025f);
				Text name = entity.getCustomName();
				Matrix4f model = matrices.peek().getModel();
				int x = -dispatcher.getTextRenderer().getWidth(name) / 2;
				dispatcher.getTextRenderer().draw(name, x, 0, 0x20ffff, false, model, vertexConsumers, true, (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255f) << 24, light);
				dispatcher.getTextRenderer().draw(name, x, 0, -1, false, model, vertexConsumers, false, 0, light);
				matrices.pop();
			}
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	private void renderPortal(WitchCauldronBlockEntity entity, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
		matrices.push();
		double distance = pos.getSquaredDistance(dispatcher.camera.getPos(), true);
		int renderDepth = getDepthFromDistance(distance);
		Matrix4f matrix4f = matrices.peek().getModel();
		for (int i = 0; i < renderDepth; ++i) {
			float colorFactor = 2f / (18 - i);
			float r = (entity.getWorld().random.nextFloat() * 0.5f + 0.1f) * colorFactor;
			float g = (entity.getWorld().random.nextFloat() * 0.5f + 0.4f) * colorFactor;
			float b = (entity.getWorld().random.nextFloat() * 0.5f + 0.5f) * colorFactor;
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(PORTAL_LAYERS.get(i));
			vertexConsumer.vertex(matrix4f, 0.125f, 0, 0.875f).color(r, g, b, 1).next();
			vertexConsumer.vertex(matrix4f, 0.875f, 0, 0.875f).color(r, g, b, 1).next();
			vertexConsumer.vertex(matrix4f, 0.875f, 0, 0.125f).color(r, g, b, 1).next();
			vertexConsumer.vertex(matrix4f, 0.125f, 0, 0.125f).color(r, g, b, 1).next();
		}
		matrices.pop();
	}
	
	private void renderWater(WitchCauldronBlockEntity entity, MatrixStack matrices, VertexConsumer buffer, int light, int overlay) {
		matrices.push();
		Matrix4f mat = matrices.peek().getModel();
		float sizeFactor = 0.125f;
		float maxV = (FLUID.getMaxV() - FLUID.getMinV()) * sizeFactor;
		float minV = (FLUID.getMaxV() - FLUID.getMinV()) * (1 - sizeFactor);
		int red = (entity.color >> 16) & 0xff;
		int green = (entity.color >> 8) & 0xff;
		int blue = entity.color & 0xff;
		buffer.vertex(mat, sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).texture(FLUID.getMinU(), FLUID.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
		buffer.vertex(mat, 1 - sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).texture(FLUID.getMaxU(), FLUID.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
		buffer.vertex(mat, 1 - sizeFactor, 0, sizeFactor).color(red, green, blue, 255).texture(FLUID.getMaxU(), FLUID.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
		buffer.vertex(mat, sizeFactor, 0, sizeFactor).color(red, green, blue, 255).texture(FLUID.getMinU(), FLUID.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
		matrices.pop();
	}
	
	private int getDepthFromDistance(double distance) {
		if (distance > 36864) {
			return 1;
		}
		else if (distance > 25600) {
			return 3;
		}
		else if (distance > 16384) {
			return 5;
		}
		else if (distance > 9216) {
			return 7;
		}
		else if (distance > 4096) {
			return 9;
		}
		else if (distance > 1024) {
			return 11;
		}
		else if (distance > 576) {
			return 13;
		}
		else {
			return distance > 256 ? 14 : 15;
		}
	}
}
