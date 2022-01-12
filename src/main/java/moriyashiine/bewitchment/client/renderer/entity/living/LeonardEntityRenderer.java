/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.LeonardEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class LeonardEntityRenderer extends MobEntityRenderer<LeonardEntity, LeonardEntityModel<LeonardEntity>> {
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/living/leonard.png");

	public LeonardEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new LeonardEntityModel<>(context.getPart(BewitchmentClient.LEONARD_MODEL_LAYER)), 0.5f);
		addFeature(new HeldItemFeatureRenderer<>(this));
	}

	@Override
	public Identifier getTexture(LeonardEntity entity) {
		return TEXTURE;
	}
}
