package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HellhoundEntityModel<T extends HellhoundEntity> extends EntityModel<T> {
	private final ModelPart lForeleg;
	private final ModelPart body;
	private final ModelPart tail00;
	private final ModelPart lHindleg;
	private final ModelPart rHindleg;
	private final ModelPart rForeleg;
	private final ModelPart neck;
	
	public HellhoundEntityModel() {
		textureWidth = 64;
		textureHeight = 64;
		lForeleg = new ModelPart(this);
		lForeleg.setPivot(1.5F, 16.0F, -4.0F);
		lForeleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		body = new ModelPart(this);
		body.setPivot(0.0F, 14.0F, 1.0F);
		setRotation(body, 1.5708F, 0.0F, 0.0F);
		body.setTextureOffset(18, 14).addCuboid(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);
		
		ModelPart mane00 = new ModelPart(this);
		mane00.setPivot(0.0F, -4.0F, 0.0F);
		body.addChild(mane00);
		mane00.setTextureOffset(21, 0).addCuboid(-4.0F, -3.5F, -3.01F, 8.0F, 7.0F, 7.0F, 0.0F, false);
		
		ModelPart mane02 = new ModelPart(this);
		mane02.setPivot(0.0F, -1.0F, 2.7F);
		mane00.addChild(mane02);
		setRotation(mane02, -1.2915F, 0.0F, 0.0F);
		mane02.setTextureOffset(28, 48).addCuboid(-3.5F, -1.0F, 0.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);
		
		tail00 = new ModelPart(this);
		tail00.setPivot(0.0F, 6.8F, 2.2F);
		body.addChild(tail00);
		tail00.setTextureOffset(20, 33).addCuboid(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart tail01 = new ModelPart(this);
		tail01.setPivot(0.0F, 3.3F, 0.0F);
		tail00.addChild(tail01);
		setRotation(tail01, 0.2731F, 0.0F, 0.0F);
		tail01.setTextureOffset(26, 33).addCuboid(-0.5F, -0.1F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart tail02 = new ModelPart(this);
		tail02.setPivot(0.0F, 4.4F, 0.0F);
		tail01.addChild(tail02);
		setRotation(tail02, 0.2094F, 0.0F, 0.0F);
		tail02.setTextureOffset(32, 33).addCuboid(-0.5F, -0.1F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart tail03 = new ModelPart(this);
		tail03.setPivot(0.0F, 4.3F, 0.0F);
		tail02.addChild(tail03);
		setRotation(tail03, 0.2094F, 0.0F, 0.0F);
		tail03.setTextureOffset(37, 33).addCuboid(-1.0F, -0.1F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart tail04 = new ModelPart(this);
		tail04.setPivot(0.0F, 1.3F, 0.1F);
		tail03.addChild(tail04);
		setRotation(tail04, 0.0F, 0.0F, -0.7854F);
		tail04.setTextureOffset(44, 33).addCuboid(-1.5F, -0.5F, -0.59F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart mane01 = new ModelPart(this);
		mane01.setPivot(0.0F, -8.2F, 2.5F);
		body.addChild(mane01);
		setRotation(mane01, -1.1781F, 0.0F, 0.0F);
		mane01.setTextureOffset(0, 48).addCuboid(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 7.0F, 0.0F, false);
		
		lHindleg = new ModelPart(this);
		lHindleg.setPivot(1.5F, 16.0F, 6.0F);
		lHindleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		rHindleg = new ModelPart(this);
		rHindleg.setPivot(-1.5F, 16.0F, 6.0F);
		rHindleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		rForeleg = new ModelPart(this);
		rForeleg.setPivot(-1.5F, 16.0F, -4.0F);
		rForeleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		neck = new ModelPart(this);
		neck.setPivot(0.0F, 13.6F, -4.5F);
		neck.setTextureOffset(0, 32).addCuboid(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 4.0F, 0.0F, false);
		
		ModelPart head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, -2.9F);
		neck.addChild(head);
		head.setTextureOffset(0, 0).addCuboid(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart leftEar = new ModelPart(this);
		leftEar.setPivot(-2.0F, -3.0F, -2.0F);
		head.addChild(leftEar);
		leftEar.setTextureOffset(16, 14).addCuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart rightEar = new ModelPart(this);
		rightEar.setPivot(2.0F, -3.0F, -2.0F);
		head.addChild(rightEar);
		rightEar.setTextureOffset(16, 14).addCuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart muzzle = new ModelPart(this);
		muzzle.setPivot(0.0F, 0.7F, -3.9F);
		head.addChild(muzzle);
		muzzle.setTextureOffset(0, 10).addCuboid(-1.5F, -1.0F, -3.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		
		ModelPart lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, 2.0F, -3.8F);
		head.addChild(lowerJaw);
		lowerJaw.setTextureOffset(0, 43).addCuboid(-1.5F, -0.3F, -3.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart rHorn01 = new ModelPart(this);
		rHorn01.setPivot(-1.3F, -2.1F, -1.4F);
		head.addChild(rHorn01);
		setRotation(rHorn01, -1.0016F, -0.4554F, 0.0F);
		rHorn01.setTextureOffset(46, 0).addCuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn02 = new ModelPart(this);
		rHorn02.setPivot(0.0F, -2.5F, -0.1F);
		rHorn01.addChild(rHorn02);
		setRotation(rHorn02, -0.5236F, -0.1745F, 0.0F);
		rHorn02.setTextureOffset(55, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn03 = new ModelPart(this);
		rHorn03.setPivot(0.0F, -1.5F, -0.1F);
		rHorn02.addChild(rHorn03);
		setRotation(rHorn03, -0.6981F, -0.2094F, 0.0F);
		rHorn03.setTextureOffset(52, 7).addCuboid(-0.8F, -2.0F, -0.8F, 1.0F, 2.0F, 1.0F, 0.2F, true);
		
		ModelPart rHorn04 = new ModelPart(this);
		rHorn04.setPivot(0.0F, -1.5F, -0.1F);
		rHorn03.addChild(rHorn04);
		setRotation(rHorn04, -0.6981F, 0.0F, 0.0F);
		rHorn04.setTextureOffset(57, 6).addCuboid(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.15F, true);
		
		ModelPart rHorn05 = new ModelPart(this);
		rHorn05.setPivot(0.0F, -2.5F, -0.1F);
		rHorn04.addChild(rHorn05);
		setRotation(rHorn05, -0.6981F, -0.3491F, 0.0F);
		rHorn05.setTextureOffset(52, 12).addCuboid(-0.5F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.1F, true);
		
		ModelPart rHorn06 = new ModelPart(this);
		rHorn06.setPivot(0.0F, -2.7F, 0.0F);
		rHorn05.addChild(rHorn06);
		setRotation(rHorn06, -0.6283F, 0.0F, 0.0F);
		rHorn06.setTextureOffset(58, 13).addCuboid(-0.51F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn01 = new ModelPart(this);
		lHorn01.setPivot(1.3F, -2.1F, -1.4F);
		head.addChild(lHorn01);
		setRotation(lHorn01, -1.0016F, 0.4554F, 0.0F);
		lHorn01.setTextureOffset(46, 0).addCuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart lHorn02 = new ModelPart(this);
		lHorn02.setPivot(0.0F, -2.5F, -0.1F);
		lHorn01.addChild(lHorn02);
		setRotation(lHorn02, -0.5236F, 0.1745F, 0.0F);
		lHorn02.setTextureOffset(55, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart lHorn03 = new ModelPart(this);
		lHorn03.setPivot(0.0F, -1.5F, -0.1F);
		lHorn02.addChild(lHorn03);
		setRotation(lHorn03, -0.6981F, 0.2094F, 0.0F);
		lHorn03.setTextureOffset(52, 7).addCuboid(-0.2F, -2.0F, -0.8F, 1.0F, 2.0F, 1.0F, 0.2F, false);
		
		ModelPart lHorn04 = new ModelPart(this);
		lHorn04.setPivot(0.0F, -1.5F, -0.1F);
		lHorn03.addChild(lHorn04);
		setRotation(lHorn04, -0.6981F, 0.0F, 0.0F);
		lHorn04.setTextureOffset(57, 6).addCuboid(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.15F, false);
		
		ModelPart lHorn05 = new ModelPart(this);
		lHorn05.setPivot(0.0F, -2.5F, -0.1F);
		lHorn04.addChild(lHorn05);
		setRotation(lHorn05, -0.6981F, 0.3491F, 0.0F);
		lHorn05.setTextureOffset(52, 12).addCuboid(-0.5F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.1F, false);
		
		ModelPart lHorn06 = new ModelPart(this);
		lHorn06.setPivot(0.0F, -2.7F, 0.0F);
		lHorn05.addChild(lHorn06);
		setRotation(lHorn06, -0.6283F, 0.0F, 0.0F);
		lHorn06.setTextureOffset(58, 13).addCuboid(-0.49F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		neck.pitch = (float) (headPitch * (Math.PI / 180f));
		neck.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		lForeleg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		rForeleg.pitch = -lForeleg.pitch;
		rHindleg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		lHindleg.pitch = -rHindleg.pitch;
		tail00.roll = (MathHelper.cos(limbAngle * 2 / 3f) * limbDistance) + MathHelper.sin((animationProgress + entity.getEntityId()) * 1 / 8f) * 0.25f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
