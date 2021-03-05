package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.model.entity.living.DemonEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DemonEntityRenderer extends MobEntityRenderer<DemonEntity, DemonEntityModel<DemonEntity>> {
	private static Identifier[] MALE_TEXTURES, FEMALE_TEXTURES;
	
	public DemonEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new DemonEntityModel<>(), 0.5f);
		addFeature(new HeldItemFeatureRenderer<>(this));
	}
	
	@Override
	public Identifier getTexture(DemonEntity entity) {
		if (MALE_TEXTURES == null) {
			int variants = entity.getVariants();
			MALE_TEXTURES = new Identifier[variants];
			FEMALE_TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				MALE_TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/demon/male_" + i + ".png");
				FEMALE_TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/demon/female_" + i + ".png");
			}
		}
		int variant = entity.getDataTracker().get(BWHostileEntity.VARIANT);
		return entity.getDataTracker().get(DemonEntity.MALE) ? MALE_TEXTURES[variant] : FEMALE_TEXTURES[variant];
	}
}
