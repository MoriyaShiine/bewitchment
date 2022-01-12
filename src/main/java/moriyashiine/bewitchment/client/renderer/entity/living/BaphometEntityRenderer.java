/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.BaphometEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BaphometEntityRenderer extends MobEntityRenderer<BaphometEntity, BaphometEntityModel<BaphometEntity>> {
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/living/baphomet/baphomet.png");
	private static Identifier[] TEXTURES;

	public BaphometEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BaphometEntityModel<>(context.getPart(BewitchmentClient.BAPHOMET_MODEL_LAYER)), 0.5f);
		addFeature(new HeldItemFeatureRenderer<>(this));
		addFeature(new FeatureRenderer<>(this) {
			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BaphometEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (TEXTURES == null) {
					TEXTURES = new Identifier[8];
					for (int i = 0; i < 8; i++) {
						TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/baphomet/flame_" + i + ".png");
					}
				}
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURES[entity.flameIndex]));
				getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
	}

	@Override
	public Identifier getTexture(BaphometEntity entity) {
		return TEXTURE;
	}
}
