/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.OwlEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class OwlEntityRenderer extends MobEntityRenderer<OwlEntity, OwlEntityModel<OwlEntity>> {
	private static Identifier[] TEXTURES;

	public OwlEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new OwlEntityModel<>(context.getPart(BewitchmentClient.OWL_MODEL_LAYER)), 0.3f);
	}

	@Override
	public Identifier getTexture(OwlEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/owl/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
