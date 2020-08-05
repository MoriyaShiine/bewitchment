package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.model.entity.living.OwlEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class OwlEntityRenderer extends MobEntityRenderer<OwlEntity, OwlEntityModel<OwlEntity>> {
	private static Identifier[] TEXTURES;
	
	public OwlEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new OwlEntityModel<>(), 0.3f);
	}
	
	@Override
	public Identifier getTexture(OwlEntity entity) {
		if (TEXTURES == null) {
			byte skinTypes = entity.getVariants();
			TEXTURES = new Identifier[skinTypes];
			for (int i = 0; i < skinTypes; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/owl/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWTameableEntity.VARIANT)];
	}
}
