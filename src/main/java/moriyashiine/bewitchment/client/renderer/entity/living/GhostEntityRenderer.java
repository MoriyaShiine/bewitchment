/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.GhostEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
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
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class GhostEntityRenderer extends MobEntityRenderer<GhostEntity, GhostEntityModel<GhostEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");
	private static Identifier[] TEXTURES;

	public GhostEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new GhostEntityModel<>(context.getPart(BewitchmentClient.GHOST_MODEL_LAYER)), 0);
		addFeature(new HeldItemFeatureRenderer<>(this));
		addFeature(new FeatureRenderer<>(this) {
			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, GhostEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (TEXTURES == null) {
					int variants = entity.getVariants();
					TEXTURES = new Identifier[variants];
					for (int i = 0; i < variants; i++) {
						TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/ghost/" + i + ".png");
					}
				}
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURES[entity.getDataTracker().get(BWHostileEntity.VARIANT)]));
				getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
	}

	@Override
	public Identifier getTexture(GhostEntity entity) {
		return EMPTY;
	}

	@Override
	protected int getBlockLight(GhostEntity entity, BlockPos blockPos) {
		return 15;
	}
}
