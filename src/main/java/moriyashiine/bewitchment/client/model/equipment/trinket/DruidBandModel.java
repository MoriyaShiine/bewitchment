package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class DruidBandModel extends EntityModel<Entity> {
	private final ModelPart druidBand;
	
	public DruidBandModel() {
		textureWidth = 16;
		textureHeight = 16;
		druidBand = new ModelPart(this);
		druidBand.setPivot(-2.0F, -15, 0.0F);
		druidBand.setTextureOffset(0, 0).addCuboid(0.0F, 11.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);
		druidBand.setTextureOffset(0, 6).addCuboid(1.0F, 11.0F, -2.5F, 2.0F, 2.0F, 1.0F, -0.1F, false);
		
		ModelPart woodDetails = new ModelPart(this);
		woodDetails.setPivot(0.0F, 15.0F, 0.0F);
		druidBand.addChild(woodDetails);
		woodDetails.setTextureOffset(7, 6).addCuboid(3.5F, -5.5F, -2.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		woodDetails.setTextureOffset(2, 11).addCuboid(3.25F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, false);
		woodDetails.setTextureOffset(2, 11).addCuboid(-1.25F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, true);
		woodDetails.setTextureOffset(9, 12).addCuboid(-1.25F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, true);
		woodDetails.setTextureOffset(9, 12).addCuboid(3.25F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, false);
		woodDetails.setTextureOffset(8, 6).addCuboid(-0.5F, -5.5F, -2.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		woodDetails.setTextureOffset(9, 6).addCuboid(-0.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		woodDetails.setTextureOffset(8, 6).addCuboid(3.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		woodDetails.setTextureOffset(7, 6).addCuboid(1.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		woodDetails.setTextureOffset(7, 6).addCuboid(-0.5F, -5.5F, -0.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		woodDetails.setTextureOffset(7, 6).addCuboid(3.5F, -5.5F, -0.5F, 1.0F, 4.0F, 1.0F, -0.25F, false);
		
		ModelPart leaf04_r1 = new ModelPart(this);
		leaf04_r1.setPivot(3.75F, -4.0F, 1.75F);
		woodDetails.addChild(leaf04_r1);
		setRotation(leaf04_r1, 0.0F, -0.3054F, 0.0F);
		leaf04_r1.setTextureOffset(1, 12).addCuboid(-0.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, false);
		
		ModelPart leaf02_r1 = new ModelPart(this);
		leaf02_r1.setPivot(0.25F, -4.0F, -2.0F);
		woodDetails.addChild(leaf02_r1);
		setRotation(leaf02_r1, 0.0F, -0.3054F, 0.0F);
		leaf02_r1.setTextureOffset(9, 12).addCuboid(-1.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, true);
		
		ModelPart leaf03_r1 = new ModelPart(this);
		leaf03_r1.setPivot(0.25F, -4.0F, 1.75F);
		woodDetails.addChild(leaf03_r1);
		setRotation(leaf03_r1, 0.0F, 0.3054F, 0.0F);
		leaf03_r1.setTextureOffset(1, 12).addCuboid(-1.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, true);
		
		ModelPart leaf01_r1 = new ModelPart(this);
		leaf01_r1.setPivot(3.75F, -4.0F, -2.0F);
		woodDetails.addChild(leaf01_r1);
		setRotation(leaf01_r1, 0.0F, 0.3054F, 0.0F);
		leaf01_r1.setTextureOffset(9, 12).addCuboid(-0.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, -0.4F, false);
	}
	
	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		druidBand.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
