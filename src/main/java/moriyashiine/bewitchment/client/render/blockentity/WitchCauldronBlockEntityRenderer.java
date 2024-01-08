/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.blockentity;

import moriyashiine.bewitchment.client.misc.SpriteIdentifiers;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.registry.BWParticleTypes;
import moriyashiine.bewitchment.common.registry.BWProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WitchCauldronBlockEntityRenderer implements BlockEntityRenderer<WitchCauldronBlockEntity> {
	private static final float[] HEIGHT = {0, 0.25f, 0.4375f, 0.625f};

	@Override
	public void render(WitchCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			BlockPos pos = entity.getPos();
			renderName(entity.name, pos, matrices, vertexConsumers, light);
			int level = entity.getCachedState().get(BWProperties.LEVEL);
			if (level > 0) {
				matrices.push();
				matrices.translate(0, HEIGHT[level], 0);
				if (entity.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					renderPortal(matrices, vertexConsumers);
				}
				renderWater(entity.color, matrices, vertexConsumers, light, overlay, SpriteIdentifiers.CAULDRON_WATER.getSprite());
				matrices.pop();
				if (entity.heatTimer >= 60 && !MinecraftClient.getInstance().isPaused()) {
					float fluidHeight = 0;
					float width = 0.35f;
					switch (entity.getCachedState().get(BWProperties.LEVEL)) {
						case 1 -> fluidHeight = 0.225f;
						case 2 -> {
							fluidHeight = 0.425f;
							width = 0.3f;
						}
						case 3 -> fluidHeight = 0.625f;
					}
					if (fluidHeight > 0) {
						if (entity.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
							world.addParticle(new DustParticleEffect(new Vector3f(((entity.color >> 16) & 0xff) / 255f, ((entity.color >> 8) & 0xff) / 255f, (entity.color & 0xff) / 255f), 1), pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), 0, 0, 0);
						}
						if (entity.mode == WitchCauldronBlockEntity.Mode.OIL_CRAFTING && entity.color != 0xfc00fc) {
							world.addParticle(ParticleTypes.ENCHANTED_HIT, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), 0, 0, 0);
						}
						if (entity.mode == WitchCauldronBlockEntity.Mode.BREWING) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), ((entity.color >> 16) & 0xff) / 255f, ((entity.color >> 8) & 0xff) / 255f, (entity.color & 0xff) / 255f);
						}
						world.addParticle((ParticleEffect) BWParticleTypes.CAULDRON_BUBBLE, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), ((entity.color >> 16) & 0xff) / 255f, ((entity.color >> 8) & 0xff) / 255f, (entity.color & 0xff) / 255f);
					}
				}
			}
		}
	}

	private void renderName(String name, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (name != null && pos.getSquaredDistance(MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos()) <= 256) {
			int x = -MinecraftClient.getInstance().textRenderer.getWidth(name) / 2;
			matrices.push();
			Matrix4f mat = matrices.peek().getPositionMatrix();
			matrices.translate(0.5, 1, 0.5);
			matrices.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation());
			matrices.scale(-0.025f, -0.025f, 0.025f);
			MinecraftClient.getInstance().textRenderer.draw(name, x, 0, 0x20FFFFFF, false, mat, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255f) << 24, light);
			MinecraftClient.getInstance().textRenderer.draw(name, x, 0, -1, false, mat, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
			matrices.pop();
		}
	}

	private void renderPortal(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
		matrices.push();
		Matrix4f matrix4f = matrices.peek().getPositionMatrix();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEndPortal());
		vertexConsumer.vertex(matrix4f, 0.125f, 0, 0.875f).next();
		vertexConsumer.vertex(matrix4f, 0.875f, 0, 0.875f).next();
		vertexConsumer.vertex(matrix4f, 0.875f, 0, 0.125f).next();
		vertexConsumer.vertex(matrix4f, 0.125f, 0, 0.125f).next();
		matrices.pop();
	}

	private void renderWater(int color, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Sprite sprite) {
		float sizeFactor = 0.125F;
		float maxV = (sprite.getMaxV() - sprite.getMinV()) * sizeFactor;
		float minV = (sprite.getMaxV() - sprite.getMinV()) * (1 - sizeFactor);
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		matrices.push();
		Matrix4f mat = matrices.peek().getPositionMatrix();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
		vertexConsumer.vertex(mat, sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).texture(sprite.getMinU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
		vertexConsumer.vertex(mat, 1 - sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).texture(sprite.getMaxU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
		vertexConsumer.vertex(mat, 1 - sizeFactor, 0, sizeFactor).color(red, green, blue, 255).texture(sprite.getMaxU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
		vertexConsumer.vertex(mat, sizeFactor, 0, sizeFactor).color(red, green, blue, 255).texture(sprite.getMinU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
		matrices.pop();
	}
}
