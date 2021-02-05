package moriyashiine.bewitchment.client.renderer.entity;

import moriyashiine.bewitchment.api.client.renderer.BroomEntityRenderer;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;

public class JuniperBroomEntityRenderer extends BroomEntityRenderer<BroomEntity> {
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/broom/juniper.png");
	
	public JuniperBroomEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher);
	}
	
	@Override
	public Identifier getTexture(BroomEntity entity) {
		return TEXTURE;
	}
}
