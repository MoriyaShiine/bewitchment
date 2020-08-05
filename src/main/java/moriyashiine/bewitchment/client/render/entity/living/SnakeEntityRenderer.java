package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.model.entity.living.SnakeEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SnakeEntityRenderer extends MobEntityRenderer<SnakeEntity, SnakeEntityModel<SnakeEntity>> {
	private static Identifier[] TEXTURES;
	
	public SnakeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new SnakeEntityModel<>(), 0.15f);
	}
	
	@Override
	public Identifier getTexture(SnakeEntity entity) {
		if (TEXTURES == null) {
			byte skinTypes = entity.getVariants();
			TEXTURES = new Identifier[skinTypes];
			for (int i = 0; i < skinTypes; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/snake/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWTameableEntity.VARIANT)];
	}
}
