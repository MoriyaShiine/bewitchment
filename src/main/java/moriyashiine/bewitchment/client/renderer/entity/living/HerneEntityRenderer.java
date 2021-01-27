package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.model.entity.living.HerneEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HerneEntityRenderer extends MobEntityRenderer<HerneEntity, HerneEntityModel<HerneEntity>> {
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/living/herne.png");
	
	public HerneEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new HerneEntityModel<>(), 0.5f);
		addFeature(new HeldItemFeatureRenderer<>(this));
	}
	
	@Override
	public Identifier getTexture(HerneEntity entity) {
		return TEXTURE;
	}
}
