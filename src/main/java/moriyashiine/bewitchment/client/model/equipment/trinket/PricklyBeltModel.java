package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class PricklyBeltModel extends EntityModel<Entity> {
	private final ModelPart armorBody;
	
	public PricklyBeltModel() {
		textureWidth = 32;
		textureHeight = 32;
		armorBody = new ModelPart(this);
		armorBody.setPivot(0.0F, -20, 2.5f);
		armorBody.setTextureOffset(0, 0).addCuboid(-4.5F, 22.0F, -2.5F, 9.0F, 3.0F, 5.0F, -0.2F, false);
		
		ModelPart buckle = new ModelPart(this);
		buckle.setPivot(0.0F, 24.5F, -2.0F);
		armorBody.addChild(buckle);
		buckle.setTextureOffset(0, 9).addCuboid(-1.5F, -2.5F, -0.5F, 3.0F, 1.0F, 1.0F, -0.1F, false);
		buckle.setTextureOffset(0, 12).addCuboid(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, -0.1F, false);
		buckle.setTextureOffset(10, 10).addCuboid(0.5F, -2.0F, -0.55F, 1.0F, 2.0F, 1.0F, -0.1F, false);
		buckle.setTextureOffset(10, 10).addCuboid(-1.5F, -2.0F, -0.55F, 1.0F, 2.0F, 1.0F, -0.1F, false);
	}
	
	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
