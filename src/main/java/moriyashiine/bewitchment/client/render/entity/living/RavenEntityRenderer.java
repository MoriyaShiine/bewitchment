package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.model.entity.living.RavenEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RavenEntityRenderer extends MobEntityRenderer<RavenEntity, RavenEntityModel<RavenEntity>> {
	private static Identifier[] TEXTURES;
	
	public RavenEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new RavenEntityModel<>(), 0.15f);
	}
	
	@Override
	public Identifier getTexture(RavenEntity entity) {
		if (TEXTURES == null) {
			byte skinTypes = entity.getVariants();
			TEXTURES = new Identifier[skinTypes];
			for (int i = 0; i < skinTypes; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/raven/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWTameableEntity.VARIANT)];
	}
}