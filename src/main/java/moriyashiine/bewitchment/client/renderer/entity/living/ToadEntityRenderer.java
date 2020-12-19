package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.model.entity.living.ToadEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ToadEntityRenderer extends MobEntityRenderer<ToadEntity, ToadEntityModel<ToadEntity>> {
	private static Identifier[] TEXTURES;
	
	public ToadEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new ToadEntityModel<>(), 0.3f);
	}
	
	@Override
	public Identifier getTexture(ToadEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/toad/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWTameableEntity.VARIANT)];
	}
}
