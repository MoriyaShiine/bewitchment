package moriyashiine.bewitchment.client.renderer.blockentity;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public class WitchCauldronBlockEntityRenderer extends BlockEntityRenderer<WitchCauldronBlockEntity> {
	private static final Random RANDOM = new Random();
	public static final SpriteIdentifier FLUID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/water_still"));

	private static final List<RenderLayer> PORTAL_LAYERS = IntStream.range(1, 16).mapToObj((i) -> RenderLayer.getEndPortal(i + 1)).collect(ImmutableList.toImmutableList());
	private static final double[] HEIGHT = {0, 0.25, 0.4375,  0.625};

	public WitchCauldronBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public void render(WitchCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			BlockPos pos = entity.getPos();
			renderName(entity, pos, matrices, vertexConsumers, light); //into neat seperate methods, because that's nice :)
			int level = world.getBlockState(pos).get(WitchCauldronBlock.LEVEL);
			if (level > 0) {
				matrices.push();
				matrices.translate(0,  HEIGHT[level], 0);if (entity.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					renderPortal(entity, pos, matrices, vertexConsumers, light, overlay); //maybe sort of mix it with water?
				}
				renderWater(entity, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getTranslucent()), level, light, overlay);

				matrices.pop();
			}
		}
	}


	private void renderWater(WitchCauldronBlockEntity entity, BlockPos pos, MatrixStack matrices, VertexConsumer buffer, int level, int light, int overlay) {
		matrices.push();
		drawTranslucentPlane(buffer, matrices.peek().getModel(), FLUID.getSprite(), level, light, overlay, new int[]{(entity.color >> 16) & 0xff, (entity.color >> 8) & 0xff, entity.color & 0xff});
		matrices.pop();
	}

	private void renderPortal(WitchCauldronBlockEntity entity, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		double distance = pos.getSquaredDistance(this.dispatcher.camera.getPos(), true);
		int renderDepth = this.getDepthFromDistance(distance);
		Matrix4f matrix4f = matrices.peek().getModel();
		for(int d = 0; d < renderDepth; ++d) {
			this.drawPortalGlimpses(entity, 2.0F / (float)(18 - d), matrix4f, vertexConsumers.getBuffer(PORTAL_LAYERS.get(d)));
		}
		matrices.pop();
	}

	private static void drawTranslucentPlane(VertexConsumer buffer, Matrix4f mat, Sprite sprite, int level, int light, int overlay, int[] rgb) {
		float sizeFactor = 0.125F;
		float maxV = (sprite.getMaxV() - sprite.getMinV()) * sizeFactor;
		float minV = (sprite.getMaxV() - sprite.getMinV()) * (1 - sizeFactor);
		buffer.vertex(mat, sizeFactor, 0, 1 - sizeFactor).color(rgb[0], rgb[1], rgb[2], 255).texture(sprite.getMinU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
		buffer.vertex(mat, 1 - sizeFactor, 0, 1 - sizeFactor).color(rgb[0], rgb[1], rgb[2], 255).texture(sprite.getMaxU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
		buffer.vertex(mat, 1 - sizeFactor, 0, sizeFactor).color(rgb[0], rgb[1], rgb[2], 255).texture(sprite.getMaxU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
		buffer.vertex(mat, sizeFactor, 0, sizeFactor).color(rgb[0], rgb[1], rgb[2], 255).texture(sprite.getMinU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
	}

	private void drawPortalGlimpses(WitchCauldronBlockEntity entity, float colorFactor, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
		float r = (RANDOM.nextFloat() * 0.5F + 0.1F) * colorFactor;
		float g = (RANDOM.nextFloat() * 0.5F + 0.4F) * colorFactor;
		float b = (RANDOM.nextFloat() * 0.5F + 0.5F) * colorFactor;
		this.drawColoredPlane(entity, matrix4f, vertexConsumer, 0.125F,  r, g, b);
	}

	private void drawColoredPlane(WitchCauldronBlockEntity entity, Matrix4f matrix4f, VertexConsumer vertexConsumer, float margin, float r, float g, float b) {
		vertexConsumer.vertex(matrix4f, margin, 0, 1 - margin).color(r, g, b, 1.0F).next();
		vertexConsumer.vertex(matrix4f, 1 - margin, 0, 1 - margin).color(r, g, b, 1.0F).next();
		vertexConsumer.vertex(matrix4f, 1 - margin, 0, margin).color(r, g, b, 1.0F).next();
		vertexConsumer.vertex(matrix4f, margin, 0, margin).color(r, g, b, 1.0F).next();
	}

	protected int getDepthFromDistance(double distance) {
		if (distance > 36864.0D) {
			return 1;
		} else if (distance > 25600.0D) {
			return 3;
		} else if (distance > 16384.0D) {
			return 5;
		} else if (distance > 9216.0D) {
			return 7;
		} else if (distance > 4096.0D) {
			return 9;
		} else if (distance > 1024.0D) {
			return 11;
		} else if (distance > 576.0D) {
			return 13;
		} else {
			return distance > 256.0D ? 14 : 15;
		}
	}

	private void renderName(WitchCauldronBlockEntity entity, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
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
}
