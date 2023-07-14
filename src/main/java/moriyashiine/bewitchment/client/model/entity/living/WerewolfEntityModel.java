/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class WerewolfEntityModel<T extends WerewolfEntity> extends BipedEntityModel<T> {
	private final ModelPart realBody;
	private final ModelPart tail01;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart neck;
	private final ModelPart lArm01;
	private final ModelPart rArm01;

	private boolean realArm = false;

	public WerewolfEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		tail01 = realBody.getChild("stomach").getChild("tail01");
		BipedLeftLeg = realBody.getChild("stomach").getChild("BipedLeftLeg");
		BipedRightLeg = realBody.getChild("stomach").getChild("BipedRightLeg");
		neck = root.getChild("neck");
		lArm01 = root.getChild("lArm01");
		rArm01 = root.getChild("rArm01");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = data.getRoot();
		ModelPartData realBody = root.addChild("realBody", ModelPartBuilder.create().uv(49, 15).cuboid(-5.5F, -3.3F, -2.0F, 11.0F, 11.0F, 8.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5672F, 0.0F, 0.0F));
		realBody.addChild("realHead", ModelPartBuilder.create().uv(1, 2).cuboid(-0.5F, 0.7F, -0.4F, 1.0F, 1.0F, 1.0F), ModelTransform.of(0.0F, -2.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData stomach = realBody.addChild("stomach", ModelPartBuilder.create().uv(16, 16).cuboid(-5.0F, 0.0F, -3.0F, 10.0F, 12.0F, 6.0F), ModelTransform.of(0.0F, 6.9F, 2.6F, -0.3927F, 0.0F, 0.0F));
		ModelPartData tail01 = stomach.addChild("tail01", ModelPartBuilder.create().uv(112, 18).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F), ModelTransform.of(0.0F, 8.6F, 2.0F, 0.1047F, 0.0F, 0.0F));
		ModelPartData tail02 = tail01.addChild("tail02", ModelPartBuilder.create().uv(111, 26).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F), ModelTransform.of(0.0F, 3.7F, 0.0F, -0.2094F, 0.0F, 0.0F));
		ModelPartData tail03 = tail02.addChild("tail03", ModelPartBuilder.create().uv(112, 40).cuboid(-1.5F, 0.2F, -1.5F, 3.0F, 4.0F, 3.0F), ModelTransform.of(0.0F, 6.5F, 0.1F, 0.1396F, 0.0F, 0.0F));
		tail03.addChild("tail04", ModelPartBuilder.create().uv(116, 7).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), ModelTransform.of(0.0F, 0.0F, 0.4F, 0.0698F, 0.0F, 0.0F));
		stomach.addChild("fur06", ModelPartBuilder.create().uv(90, 52).cuboid(-2.0F, -1.0F, 0.0F, 4.0F, 5.0F, 2.0F), ModelTransform.of(0.0F, 6.6F, 1.5F, 0.6981F, 0.0F, 0.0F));
		stomach.addChild("fur05", ModelPartBuilder.create().uv(90, 43).cuboid(-2.0F, -1.0F, 0.0F, 4.0F, 5.0F, 2.0F), ModelTransform.of(0.0F, 4.7F, 1.6F, 0.6981F, 0.0F, 0.0F));
		ModelPartData BipedLeftLeg = stomach.addChild("BipedLeftLeg", ModelPartBuilder.create().uv(11, 46).cuboid(-2.3F, -1.1F, -1.9F, 5.0F, 13.0F, 5.0F), ModelTransform.of(2.7F, 10.2F, -0.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg02 = BipedLeftLeg.addChild("lLeg02", ModelPartBuilder.create().uv(0, 35).cuboid(-2.01F, 0.4F, -2.0F, 4.0F, 7.0F, 4.0F), ModelTransform.of(0.0F, 9.8F, 0.5F, 1.309F, -0.0524F, 0.0F));
		ModelPartData lLeg03 = lLeg02.addChild("lLeg03", ModelPartBuilder.create().uv(0, 22).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), ModelTransform.of(0.0F, 5.9F, 0.8F, -0.7854F, 0.0F, 0.0873F));
		ModelPartData lFoot = lLeg03.addChild("lFoot", ModelPartBuilder.create().uv(0, 14).cuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F), ModelTransform.of(0.0F, 8.7F, -1.2F, 0.1309F, 0.0436F, -0.0087F));
		lFoot.addChild("lFootClaw01", ModelPartBuilder.create().uv(1, 48).cuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), ModelTransform.of(-1.3F, 0.5F, -2.6F, 0.2269F, 0.1047F, 0.0F));
		lFoot.addChild("lFootClaw02", ModelPartBuilder.create().uv(1, 48).cuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.5F, -2.6F, 0.2269F, 0.0F, 0.0F));
		lFoot.addChild("lFootClaw03", ModelPartBuilder.create().uv(1, 48).cuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), ModelTransform.of(1.3F, 0.5F, -2.6F, 0.2269F, -0.1047F, 0.0F));
		ModelPartData BipedRightLeg = stomach.addChild("BipedRightLeg", ModelPartBuilder.create().uv(11, 46).mirrored(true).cuboid(-2.7F, -1.1F, -1.9F, 5.0F, 13.0F, 5.0F), ModelTransform.of(-2.7F, 10.2F, -0.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg02 = BipedRightLeg.addChild("rLeg02", ModelPartBuilder.create().uv(0, 35).mirrored(true).cuboid(-1.99F, 0.4F, -2.0F, 4.0F, 7.0F, 4.0F), ModelTransform.of(0.0F, 9.8F, 0.5F, 1.309F, 0.0524F, 0.0F));
		ModelPartData rLeg03 = rLeg02.addChild("rLeg03", ModelPartBuilder.create().uv(0, 22).mirrored(true).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), ModelTransform.of(0.0F, 5.9F, 0.8F, -0.7854F, 0.0F, -0.0873F));
		ModelPartData rFoot = rLeg03.addChild("rFoot", ModelPartBuilder.create().uv(0, 14).mirrored(true).cuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F), ModelTransform.of(0.0F, 8.7F, -1.2F, 0.1309F, -0.0436F, 0.0087F));
		rFoot.addChild("rFootClaw01", ModelPartBuilder.create().uv(1, 48).mirrored(true).cuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), ModelTransform.of(1.3F, 0.5F, -2.6F, 0.2269F, -0.1047F, 0.0F));
		rFoot.addChild("rFootClaw02", ModelPartBuilder.create().uv(1, 48).mirrored(true).cuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.5F, -2.6F, 0.2269F, 0.0F, 0.0F));
		rFoot.addChild("rFootClaw03", ModelPartBuilder.create().uv(1, 48).mirrored(true).cuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), ModelTransform.of(-1.3F, 0.5F, -2.6F, 0.2269F, 0.1047F, 0.0F));
		realBody.addChild("fur04", ModelPartBuilder.create().uv(90, 35).cuboid(-2.5F, 1.0F, 0.0F, 5.0F, 4.0F, 2.0F), ModelTransform.of(0.0F, -0.3F, 4.9F, 0.4363F, 0.0F, 0.0F));
		realBody.addChild("fur03", ModelPartBuilder.create().uv(90, 24).cuboid(-4.0F, 0.0F, -1.0F, 8.0F, 7.0F, 2.0F), ModelTransform.of(0.0F, -2.6F, 4.5F, 0.6807F, 0.0F, 0.0F));
		realBody.addChild("fur02", ModelPartBuilder.create().uv(90, 11).cuboid(-5.0F, -1.0F, -1.0F, 10.0F, 8.0F, 2.0F), ModelTransform.of(0.0F, -4.7F, 3.4F, 0.8727F, 0.0F, 0.0F));
		ModelPartData lArm01 = root.addChild("lArm01", ModelPartBuilder.create().uv(32, 47).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lArm02 = lArm01.addChild("lArm02", ModelPartBuilder.create().uv(49, 46).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 13.0F, 3.0F), ModelTransform.of(0.0F, 11.5F, 0.0F, -0.5236F, 0.0F, 0.1484F));
		lArm02.addChild("lArmFur", ModelPartBuilder.create().uv(62, 50).cuboid(-0.5F, -0.5F, 1.5F, 1.0F, 8.0F, 4.0F), ModelTransform.of(0.4F, -4.0F, 0.0F, -0.4363F, 0.0873F, 0.0873F));
		ModelPartData lClawJoint = lArm02.addChild("lClawJoint", ModelPartBuilder.create().cuboid(-0.4F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F), ModelTransform.of(0.0F, 8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		lClawJoint.addChild("lClaw01", ModelPartBuilder.create().uv(27, 0).cuboid(-1.4F, 1.2F, -1.6F, 2.0F, 5.0F, 1.0F), ModelTransform.of(1.0F, 0.2F, 0.0F, -0.1047F, 0.0F, 0.2269F));
		lClawJoint.addChild("lClaw02", ModelPartBuilder.create().uv(27, 0).cuboid(-1.4F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(1.0F, 0.2F, -0.1F, 0.0F, 0.0F, 0.2269F));
		lClawJoint.addChild("lClaw03", ModelPartBuilder.create().uv(27, 0).cuboid(-1.4F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(1.0F, 0.2F, 0.8F, 0.1047F, 0.0F, 0.2269F));
		ModelPartData rArm01 = root.addChild("rArm01", ModelPartBuilder.create().uv(32, 47).mirrored(true).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rArm02 = rArm01.addChild("rArm02", ModelPartBuilder.create().uv(49, 46).mirrored(true).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 13.0F, 3.0F), ModelTransform.of(0.0F, 11.5F, 0.0F, -0.5236F, 0.0F, -0.1484F));
		rArm02.addChild("rArmFur", ModelPartBuilder.create().uv(62, 50).mirrored(true).cuboid(-0.5F, -0.5F, 1.5F, 1.0F, 8.0F, 4.0F), ModelTransform.of(-0.4F, -4.0F, 0.0F, -0.4363F, -0.0873F, -0.0873F));
		ModelPartData rClawJoint = rArm02.addChild("rClawJoint", ModelPartBuilder.create().mirrored(true).cuboid(-0.6F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F), ModelTransform.of(0.0F, 8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		rClawJoint.addChild("rClaw01", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.6F, 1.2F, -1.6F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-1.0F, 0.2F, 0.0F, -0.1047F, 0.0F, -0.2269F));
		rClawJoint.addChild("rClaw02", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.6F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-1.0F, 0.2F, -0.1F, 0.0F, 0.0F, -0.2269F));
		rClawJoint.addChild("rClaw03", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.6F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-1.0F, 0.2F, 0.8F, 0.1047F, 0.0F, -0.2269F));
		ModelPartData neck = root.addChild("neck", ModelPartBuilder.create().cuboid(-3.5F, -1.5F, -1.0F, 7.0F, 5.0F, 5.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2531F, 0.0F, 0.0F));
		ModelPartData realHead2 = neck.addChild("realHead2", ModelPartBuilder.create().uv(44, 0).cuboid(-4.0F, -3.0F, -4.4F, 8.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, -0.5F, -3.6F, 1.9199F, 0.0F, 0.0F));
		ModelPartData jawUpperLeft = realHead2.addChild("jawUpperLeft", ModelPartBuilder.create().uv(20, 36).cuboid(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 2.0F), ModelTransform.of(1.2F, -5.1F, -1.5F, 0.0F, 0.0F, -0.1396F));
		jawUpperLeft.addChild("upperTeethLeft01", ModelPartBuilder.create().uv(56, 37).cuboid(-0.5F, 1.0F, -1.8F, 1.0F, 4.0F, 2.0F), ModelTransform.of(0.4F, -2.8F, -1.0F, 0.0F, 0.0F, 0.0F));
		jawUpperLeft.addChild("upperTeethLeft02", ModelPartBuilder.create().uv(63, 38).cuboid(-2.27F, 1.4F, -1.4F, 3.0F, 0.0F, 1.0F), ModelTransform.of(0.0F, -3.2F, -1.0F, 0.0F, 0.0F, 0.1367F));
		ModelPartData jawUpperRight = realHead2.addChild("jawUpperRight", ModelPartBuilder.create().uv(20, 36).mirrored(true).cuboid(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 2.0F), ModelTransform.of(-1.2F, -5.1F, -1.5F, 0.0F, 0.0F, 0.1396F));
		jawUpperRight.addChild("upperTeethRight", ModelPartBuilder.create().uv(56, 37).mirrored(true).cuboid(-0.5F, 1.0F, -1.8F, 1.0F, 4.0F, 2.0F), ModelTransform.of(-0.4F, -2.8F, -1.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData jawLower = realHead2.addChild("jawLower", ModelPartBuilder.create().uv(39, 37).cuboid(-2.0F, -2.9F, -1.5F, 4.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, -4.1F, -3.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lowerTeeth01 = jawLower.addChild("lowerTeeth01", ModelPartBuilder.create().uv(63, 41).cuboid(-1.6F, 1.3F, -0.6F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -3.7F, 0.1F, 0.0F, 0.0F, 0.0F));
		lowerTeeth01.addChild("lowerTeeth02", ModelPartBuilder.create().uv(63, 41).cuboid(0.6F, 1.3F, -0.7F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lEar01 = realHead2.addChild("lEar01", ModelPartBuilder.create().uv(70, 0).mirrored(true).cuboid(0.0F, -1.0F, -1.6F, 1.0F, 3.0F, 2.0F), ModelTransform.of(2.8F, -0.1F, 2.8F, 0.6981F, 0.1222F, -0.6981F));
		lEar01.addChild("lEar02", ModelPartBuilder.create().uv(78, 0).mirrored(true).cuboid(-1.4F, 2.0F, -1.4F, 1.0F, 5.0F, 1.0F), ModelTransform.of(-0.3F, -3.0F, -0.5F, 0.2269F, 0.0F, -0.2618F));
		ModelPartData rEar01 = realHead2.addChild("rEar01", ModelPartBuilder.create().uv(70, 0).cuboid(-1.0F, -1.0F, -1.6F, 1.0F, 3.0F, 2.0F), ModelTransform.of(-2.8F, -0.1F, 2.8F, 0.6981F, -0.1222F, 0.6981F));
		rEar01.addChild("rEar02", ModelPartBuilder.create().uv(78, 0).cuboid(0.4F, 2.0F, -1.4F, 1.0F, 5.0F, 1.0F), ModelTransform.of(0.3F, -3.0F, -0.5F, 0.2269F, 0.0F, 0.2618F));
		realHead2.addChild("lCheekFur", ModelPartBuilder.create().uv(26, 4).mirrored(true).cuboid(0.0F, -0.8F, -3.3F, 0.0F, 6.0F, 5.0F), ModelTransform.of(3.5F, -0.5F, -0.6F, 0.1222F, -0.0873F, -0.5236F));
		realHead2.addChild("rCheekFur", ModelPartBuilder.create().uv(26, 4).cuboid(0.0F, -0.8F, -3.3F, 0.0F, 6.0F, 5.0F), ModelTransform.of(-3.5F, -0.5F, -0.6F, 0.1222F, 0.0873F, 0.5236F));
		realHead2.addChild("snout", ModelPartBuilder.create().uv(29, 35).cuboid(-1.5F, -3.2F, -2.0F, 3.0F, 5.0F, 2.0F), ModelTransform.of(0.0F, -4.2F, -0.1F, 0.182F, 0.0F, 0.0F));
		neck.addChild("fur01", ModelPartBuilder.create().uv(90, 0).cuboid(-3.5F, 2.0F, 0.0F, 7.0F, 7.0F, 2.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, 0.0F, -2.9F, 1.7453F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 128, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(neck, super.head);
		neck.setPivot(0, -18.2f, -1);
		neck.pitch -= 0.2531f;
		copyRotation(realBody, super.body);
		realBody.setPivot(0, -12.7f, 0);
		realBody.pitch += 0.5672f;
		copyRotation(lArm01, super.leftArm);
		lArm01.setPivot(5.5f, -15, 2);
		lArm01.pitch += 0.1745f;
		lArm01.yaw -= 0.0873f;
		lArm01.roll -= 0.2356f;
		copyRotation(rArm01, super.rightArm);
		rArm01.setPivot(-5.5f, -15, 2);
		rArm01.pitch += 0.1745f;
		rArm01.yaw += 0.0873f;
		rArm01.roll += 0.2356f;
		copyRotation(BipedLeftLeg, super.leftLeg);
		BipedLeftLeg.pitch /= 2;
		BipedLeftLeg.pitch -= 3 / 4f;
		BipedLeftLeg.yaw -= 0.2269f;
		BipedLeftLeg.roll -= 0.0873f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.pitch /= 2;
		BipedRightLeg.pitch -= 3 / 4f;
		BipedRightLeg.yaw -= -0.2269f;
		BipedRightLeg.roll += 0.0873f;
		tail01.roll = MathHelper.sin(animationProgress / 8) / 8;
		if (entity.isSneaking()) {
			neck.pivotY += 2;
			neck.pivotZ -= 4;
			realBody.pivotY += 2;
			realBody.pivotZ -= 4;
			realBody.pitch += 0.5f;
			lArm01.pivotY += 2;
			lArm01.pivotZ -= 4;
			rArm01.pivotY += 2;
			rArm01.pivotZ -= 4;
			BipedLeftLeg.pitch -= 0.5f;
			BipedRightLeg.pitch -= 0.5f;
		}
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lArm01.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rArm01.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? lArm01 : rArm01) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
