package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ZephyrHarnessModel extends EntityModel<Entity> {
	private final ModelPart armorBody;
	
	public ZephyrHarnessModel() {
		textureWidth = 32;
		textureHeight = 32;
		armorBody = new ModelPart(this);
		armorBody.setPivot(0.0F, -20, 2.5f);
		armorBody.setTextureOffset(0, 0).addCuboid(-4.5F, 23.0F, -2.5F, 9.0F, 2.0F, 5.0F, -0.2F, false);
		
		ModelPart rFeather_r1 = new ModelPart(this);
		rFeather_r1.setPivot(-4.5F, 24.0F, -0.75F);
		armorBody.addChild(rFeather_r1);
		setRotation(rFeather_r1, 0.0F, 0.3491F, 0.1745F);
		rFeather_r1.setTextureOffset(0, 2).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 9.0F, 6.0F, 0.0F, false);
		
		ModelPart lFeather_r1 = new ModelPart(this);
		lFeather_r1.setPivot(4.5F, 24.0F, -1.0F);
		armorBody.addChild(lFeather_r1);
		setRotation(lFeather_r1, 0.0F, -0.3491F, -0.1745F);
		lFeather_r1.setTextureOffset(0, 2).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 9.0F, 6.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
