// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package moriyashiine.bewitchment.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ContributorHornsModel extends Model {
	private final ModelPart armorHead;
	
	public ContributorHornsModel() {
		super(RenderLayer::getEntityTranslucent);
		textureWidth = 16;
		textureHeight = 16;
		armorHead = new ModelPart(this);
		armorHead.setPivot(0.0F, 0.0F, 0.0F);
		
		ModelPart lUpperHorn01 = new ModelPart(this);
		lUpperHorn01.setPivot(2.0F, -8.7F, -0.5F);
		armorHead.addChild(lUpperHorn01);
		setRotation(lUpperHorn01, 0.8727F, 0.1745F, 0.0F);
		lUpperHorn01.setTextureOffset(6, 5).addCuboid(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F, 0.0F, true);
		
		ModelPart lUpperHorn02 = new ModelPart(this);
		lUpperHorn02.setPivot(0.0F, 0.0F, 0.9F);
		lUpperHorn01.addChild(lUpperHorn02);
		setRotation(lUpperHorn02, -0.3142F, 0.2618F, 0.0F);
		lUpperHorn02.setTextureOffset(0, 12).addCuboid(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, 0.2F, true);
		
		ModelPart lUpperHorn03 = new ModelPart(this);
		lUpperHorn03.setPivot(0.0F, 0.0F, 2.4F);
		lUpperHorn02.addChild(lUpperHorn03);
		setRotation(lUpperHorn03, -0.1745F, 0.1745F, 0.0F);
		lUpperHorn03.setTextureOffset(8, 12).addCuboid(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rUpperHorn01 = new ModelPart(this);
		rUpperHorn01.setPivot(-2.0F, -8.7F, -0.5F);
		armorHead.addChild(rUpperHorn01);
		setRotation(rUpperHorn01, 0.8727F, -0.1745F, 0.0F);
		rUpperHorn01.setTextureOffset(6, 5).addCuboid(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F, 0.0F, true);
		
		ModelPart rUpperHorn02 = new ModelPart(this);
		rUpperHorn02.setPivot(0.0F, 0.0F, 0.9F);
		rUpperHorn01.addChild(rUpperHorn02);
		setRotation(rUpperHorn02, -0.3142F, -0.2618F, 0.0F);
		rUpperHorn02.setTextureOffset(0, 12).addCuboid(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, 0.2F, true);
		
		ModelPart rUpperHorn03 = new ModelPart(this);
		rUpperHorn03.setPivot(0.0F, 0.0F, 2.4F);
		rUpperHorn02.addChild(rUpperHorn03);
		setRotation(rUpperHorn03, -0.1745F, -0.1745F, 0.0F);
		rUpperHorn03.setTextureOffset(8, 12).addCuboid(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart lHorn01 = new ModelPart(this);
		lHorn01.setPivot(2.9F, -7.3F, 1.6F);
		armorHead.addChild(lHorn01);
		setRotation(lHorn01, -0.6109F, 0.0F, 1.309F);
		lHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart lHorn02 = new ModelPart(this);
		lHorn02.setPivot(-0.1F, -1.5F, -0.1F);
		lHorn01.addChild(lHorn02);
		setRotation(lHorn02, -0.2618F, 0.0F, 0.4014F);
		lHorn02.setTextureOffset(0, 4).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.2F, false);
		
		ModelPart lHorn03 = new ModelPart(this);
		lHorn03.setPivot(0.0F, -1.6F, 0.0F);
		lHorn02.addChild(lHorn03);
		setRotation(lHorn03, -0.1745F, 0.0F, 0.4363F);
		lHorn03.setTextureOffset(0, 4).addCuboid(-0.8F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.1F, false);
		
		ModelPart lHorn04 = new ModelPart(this);
		lHorn04.setPivot(0.0F, -1.7F, 0.0F);
		lHorn03.addChild(lHorn04);
		setRotation(lHorn04, 0.0524F, 0.0F, 0.1396F);
		lHorn04.setTextureOffset(12, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lHorn05 = new ModelPart(this);
		lHorn05.setPivot(0.0F, -2.7F, 0.0F);
		lHorn04.addChild(lHorn05);
		setRotation(lHorn05, 0.0524F, 0.0F, -0.3142F);
		lHorn05.setTextureOffset(12, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart rHorn01 = new ModelPart(this);
		rHorn01.setPivot(-2.9F, -7.3F, 1.6F);
		armorHead.addChild(rHorn01);
		setRotation(rHorn01, -0.6109F, 0.0F, -1.309F);
		rHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn02 = new ModelPart(this);
		rHorn02.setPivot(0.1F, -1.5F, -0.1F);
		rHorn01.addChild(rHorn02);
		setRotation(rHorn02, -0.2618F, 0.0F, -0.4014F);
		rHorn02.setTextureOffset(0, 4).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.2F, true);
		
		ModelPart rHorn03 = new ModelPart(this);
		rHorn03.setPivot(0.0F, -1.6F, 0.0F);
		rHorn02.addChild(rHorn03);
		setRotation(rHorn03, -0.1745F, 0.0F, -0.4363F);
		rHorn03.setTextureOffset(0, 4).addCuboid(-0.2F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.1F, true);
		
		ModelPart rHorn04 = new ModelPart(this);
		rHorn04.setPivot(0.0F, -1.7F, 0.0F);
		rHorn03.addChild(rHorn04);
		setRotation(rHorn04, 0.0524F, 0.0F, -0.1396F);
		rHorn04.setTextureOffset(12, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn05 = new ModelPart(this);
		rHorn05.setPivot(0.0F, -2.7F, 0.0F);
		rHorn04.addChild(rHorn05);
		setRotation(rHorn05, 0.0524F, 0.0F, 0.3142F);
		rHorn05.setTextureOffset(12, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
