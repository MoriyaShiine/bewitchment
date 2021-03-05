package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class SpecterBangleModel extends EntityModel<Entity> {
	private final ModelPart spectreBangle;
	
	public SpecterBangleModel() {
		textureWidth = 16;
		textureHeight = 16;
		spectreBangle = new ModelPart(this);
		spectreBangle.setPivot(0, -3, 0);
		spectreBangle.setTextureOffset(0, 0).addCuboid(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);
		
		ModelPart eye = new ModelPart(this);
		eye.setPivot(-0.5F, -0.5F, -1.5F);
		spectreBangle.addChild(eye);
		eye.setTextureOffset(0, 7).addCuboid(-0.5F, -0.5F, -1.0F, 2.0F, 2.0F, 1.0F, -0.1F, false);
		eye.setTextureOffset(0, 11).addCuboid(1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, -0.2F, false);
		eye.setTextureOffset(0, 11).addCuboid(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, -0.2F, false);
		eye.setTextureOffset(5, 11).addCuboid(0.0F, 0.0F, -1.1F, 1.0F, 1.0F, 1.0F, -0.1F, false);
	}
	
	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		spectreBangle.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
