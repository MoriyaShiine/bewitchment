/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity;

import moriyashiine.bewitchment.api.client.renderer.BroomEntityRenderer;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class CypressBroomEntityRenderer extends BroomEntityRenderer<BroomEntity> {
	private static final Identifier TEXTURE = Bewitchment.id("textures/entity/broom/cypress.png");

	public CypressBroomEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(BroomEntity entity) {
		return TEXTURE;
	}
}
