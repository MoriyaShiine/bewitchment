package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.BlackDogEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BlackDogEntityModel<T extends BlackDogEntity> extends EntityModel<T> {
	private final ModelPart lForeleg;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart lHindleg;
	private final ModelPart rHindleg;
	private final ModelPart rForeleg;
	
	public BlackDogEntityModel() {
		textureWidth = 64;
		textureHeight = 64;
		lForeleg = new ModelPart(this);
		lForeleg.setPivot(1.5F, 16.0F, -4.0F);
		lForeleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		body = new ModelPart(this);
		body.setPivot(0.0F, 14.0F, 1.0F);
		setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
		body.setTextureOffset(18, 14).addCuboid(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);
		
		ModelPart mane00 = new ModelPart(this);
		mane00.setPivot(0.0F, -4.0F, 0.0F);
		body.addChild(mane00);
		mane00.setTextureOffset(21, 0).addCuboid(-4.0F, -3.5F, -3.01F, 8.0F, 6.0F, 7.0F, 0.0F, false);
		
		ModelPart mane02 = new ModelPart(this);
		mane02.setPivot(0.0F, -1.0F, 2.7F);
		mane00.addChild(mane02);
		setRotationAngle(mane02, -1.2915F, 0.0F, 0.0F);
		mane02.setTextureOffset(28, 48).addCuboid(-3.5F, -1.0F, 0.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);
		
		tail = new ModelPart(this);
		tail.setPivot(0.0F, 5.7F, 2.0F);
		body.addChild(tail);
		setRotationAngle(tail, -0.8727F, 0.0F, 0.0F);
		tail.setTextureOffset(9, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
		
		ModelPart neck = new ModelPart(this);
		neck.setPivot(0.0F, -5.4F, 0.5F);
		body.addChild(neck);
		setRotationAngle(neck, -1.5708F, 0.0F, 0.0F);
		neck.setTextureOffset(0, 32).addCuboid(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 4.0F, 0.0F, false);
		
		ModelPart mane01 = new ModelPart(this);
		mane01.setPivot(0.0F, -1.8F, -3.0F);
		neck.addChild(mane01);
		setRotationAngle(mane01, 0.4363F, 0.0F, 0.0F);
		mane01.setTextureOffset(0, 48).addCuboid(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 7.0F, -0.01F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, -2.9F);
		neck.addChild(head);
		head.setTextureOffset(0, 0).addCuboid(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart muzzle = new ModelPart(this);
		muzzle.setPivot(0.0F, 0.7F, -3.9F);
		head.addChild(muzzle);
		muzzle.setTextureOffset(0, 10).addCuboid(-1.5F, -1.0F, -3.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		
		ModelPart lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, 2.0F, -3.8F);
		head.addChild(lowerJaw);
		lowerJaw.setTextureOffset(0, 43).addCuboid(-1.5F, -0.4F, -3.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(-2.9F, -2.9F, -2.0F);
		head.addChild(lEar);
		setRotationAngle(lEar, 0.0F, 0.0F, 0.3054F);
		lEar.setTextureOffset(39, 14).addCuboid(-0.5F, -0.1F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(2.9F, -2.9F, -2.0F);
		head.addChild(rEar);
		setRotationAngle(rEar, 0.0F, 0.0F, -0.3054F);
		rEar.setTextureOffset(39, 14).addCuboid(-0.5F, -0.1F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		lHindleg = new ModelPart(this);
		lHindleg.setPivot(1.5F, 16.0F, 6.0F);
		lHindleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		rHindleg = new ModelPart(this);
		rHindleg.setPivot(-1.5F, 16.0F, 6.0F);
		rHindleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		
		rForeleg = new ModelPart(this);
		rForeleg.setPivot(-1.5F, 16.0F, -4.0F);
		rForeleg.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f));
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		lForeleg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		rForeleg.pitch = MathHelper.cos((float) (limbAngle * 2 / 3f + Math.PI)) * 1.4f * limbDistance;
		lHindleg.pitch = MathHelper.cos((float) (limbAngle * 2 / 3f + Math.PI)) * 1.4f * limbDistance;
		rHindleg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		tail.roll = (MathHelper.cos(limbAngle * 2 / 3f) * limbDistance) + MathHelper.sin((animationProgress + entity.getEntityId()) * 1 / 8F) * 0.25f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}