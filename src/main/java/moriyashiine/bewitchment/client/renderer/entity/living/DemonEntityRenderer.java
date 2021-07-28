package moriyashiine.bewitchment.client.renderer.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.DemonEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DemonEntityRenderer extends MobEntityRenderer<DemonEntity, DemonEntityModel<DemonEntity>> {
	private static Identifier[] TEXTURES;
	
	public DemonEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new DemonEntityModel<>(context.getPart(BewitchmentClient.DEMON_MODEL_LAYER)), 0.5f);
		addFeature(new HeldItemFeatureRenderer<>(this));
	}
	
	@Override
	public Identifier getTexture(DemonEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new Identifier[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = new Identifier(Bewitchment.MODID, "textures/entity/living/demon/" + i + ".png");
			}
		}
		return TEXTURES[entity.getDataTracker().get(BWHostileEntity.VARIANT)];
	}
}
