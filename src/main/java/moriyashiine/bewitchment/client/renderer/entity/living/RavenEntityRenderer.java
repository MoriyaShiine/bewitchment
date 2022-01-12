package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.RavenEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RavenEntityRenderer extends MobEntityRenderer<RavenEntity, RavenEntityModel<RavenEntity>> {
	private static Identifier[] TEXTURES;

	public RavenEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RavenEntityModel<>(context.getPart(BewitchmentClient.RAVEN_MODEL_LAYER)), 0.3f);
	}

	@Override
	public Identifier getTexture(RavenEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/raven/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWTameableEntity.VARIANT)];
	}
}
