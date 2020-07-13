package moriyashiine.bewitchment.client.render.entity.living;

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
		super(entityRenderDispatcher, new ToadEntityModel<>(), 0.15f);
	}
	
	@Override
	public Identifier getTexture(ToadEntity entity) {
		if (TEXTURES == null) {
			byte skinTypes = entity.getVariants();
			TEXTURES = new Identifier[skinTypes];
			for (int i = 0; i < skinTypes; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/toad/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWTameableEntity.VARIANT)];
	}
}