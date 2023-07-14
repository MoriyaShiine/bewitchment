/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.HellhoundEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HellhoundEntityRenderer extends MobEntityRenderer<HellhoundEntity, HellhoundEntityModel<HellhoundEntity>> {
	private static Identifier[] TEXTURES;

	public HellhoundEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new HellhoundEntityModel<>(context.getPart(BewitchmentClient.HELLHOUND_MODEL_LAYER)), 0.3f);
	}

	@Override
	public Identifier getTexture(HellhoundEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/hellhound/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
