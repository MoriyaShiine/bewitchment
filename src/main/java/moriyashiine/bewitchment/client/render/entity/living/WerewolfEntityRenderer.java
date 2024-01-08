/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.WerewolfEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

public class WerewolfEntityRenderer extends MobEntityRenderer<WerewolfEntity, WerewolfEntityModel<WerewolfEntity>> {
	private static Identifier[] TEXTURES;

	public WerewolfEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new WerewolfEntityModel<>(context.getPart(BewitchmentClient.WEREWOLF_MODEL_LAYER)), 0.5f);
		addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
	}

	@Override
	public Identifier getTexture(WerewolfEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/werewolf/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
