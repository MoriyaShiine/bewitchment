/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class SilverArrowEntityRenderer extends ProjectileEntityRenderer<SilverArrowEntity> {
	private static final Identifier TEXTURE = Bewitchment.id("textures/entity/projectiles/silver_arrow.png");

	public SilverArrowEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(SilverArrowEntity entity) {
		return TEXTURE;
	}
}
