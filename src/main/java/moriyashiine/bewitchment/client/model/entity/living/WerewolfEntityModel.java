package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class WerewolfEntityModel<T extends WerewolfEntity> extends BipedEntityModel<T> {
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart tail01;
	
	public WerewolfEntityModel() {
		super(1);
		textureWidth = 128;
		textureHeight = 64;
		body = new ModelPart(this);
		body.setPivot(0.0F, -12.7F, 0.0F);
		setRotationAngle(body, 0.5672F, 0.0F, 0.0F);
		body.setTextureOffset(49, 15).addCuboid(-5.5F, -3.3F, -2.0F, 11.0F, 11.0F, 8.0F, 0.0F, false);
		
		ModelPart head = new ModelPart(this);
		head.setPivot(0.0F, -2.0F, 2.0F);
		body.addChild(head);
		head.setTextureOffset(1, 2).addCuboid(-0.5F, 0.7F, -0.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		
		neck = new ModelPart(this);
		neck.setPivot(0.0F, -2.5F, 0.0F);
		head.addChild(neck);
		setRotationAngle(neck, -0.9076F, 0.0F, 0.0F);
		neck.setTextureOffset(0, 0).addCuboid(-3.5F, -1.5F, -1.0F, 7.0F, 5.0F, 5.0F, 0.0F, false);
		
		ModelPart head2 = new ModelPart(this);
		head2.setPivot(0.0F, -0.5F, -3.6F);
		neck.addChild(head2);
		setRotationAngle(head2, 1.9199F, 0.0F, 0.0F);
		head2.setTextureOffset(44, 0).addCuboid(-4.0F, -3.0F, -4.4F, 8.0F, 6.0F, 7.0F, 0.0F, false);
		
		ModelPart jawUpperLeft = new ModelPart(this);
		jawUpperLeft.setPivot(1.2F, -5.1F, -1.5F);
		head2.addChild(jawUpperLeft);
		setRotationAngle(jawUpperLeft, 0.0F, 0.0F, -0.1396F);
		jawUpperLeft.setTextureOffset(20, 36).addCuboid(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);
		
		ModelPart upperTeethLeft01 = new ModelPart(this);
		upperTeethLeft01.setPivot(0.4F, -2.8F, -1.0F);
		jawUpperLeft.addChild(upperTeethLeft01);
		upperTeethLeft01.setTextureOffset(56, 37).addCuboid(-0.5F, 1.0F, -1.8F, 1.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart upperTeethLeft02 = new ModelPart(this);
		upperTeethLeft02.setPivot(0.0F, -3.2F, -1.0F);
		jawUpperLeft.addChild(upperTeethLeft02);
		setRotationAngle(upperTeethLeft02, 0.0F, 0.0F, 0.1367F);
		upperTeethLeft02.setTextureOffset(63, 38).addCuboid(-2.27F, 1.4F, -1.4F, 3.0F, 0.0F, 1.0F, 0.0F, false);
		
		ModelPart jawUpperRight = new ModelPart(this);
		jawUpperRight.setPivot(-1.2F, -5.1F, -1.5F);
		head2.addChild(jawUpperRight);
		setRotationAngle(jawUpperRight, 0.0F, 0.0F, 0.1396F);
		jawUpperRight.setTextureOffset(20, 36).addCuboid(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 2.0F, 0.0F, true);
		
		ModelPart upperTeethRight = new ModelPart(this);
		upperTeethRight.setPivot(-0.4F, -2.8F, -1.0F);
		jawUpperRight.addChild(upperTeethRight);
		upperTeethRight.setTextureOffset(56, 37).addCuboid(-0.5F, 1.0F, -1.8F, 1.0F, 4.0F, 2.0F, 0.0F, true);
		
		ModelPart jawLower = new ModelPart(this);
		jawLower.setPivot(0.0F, -4.1F, -3.0F);
		head2.addChild(jawLower);
		jawLower.setTextureOffset(39, 37).addCuboid(-2.0F, -2.9F, -1.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lowerTeeth01 = new ModelPart(this);
		lowerTeeth01.setPivot(0.0F, -3.7F, 0.1F);
		jawLower.addChild(lowerTeeth01);
		lowerTeeth01.setTextureOffset(63, 41).addCuboid(-1.6F, 1.3F, -0.6F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lowerTeeth02 = new ModelPart(this);
		lowerTeeth02.setPivot(0.0F, 0.0F, 0.0F);
		lowerTeeth01.addChild(lowerTeeth02);
		lowerTeeth02.setTextureOffset(63, 41).addCuboid(0.6F, 1.3F, -0.7F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lEar01 = new ModelPart(this);
		lEar01.setPivot(2.8F, -0.1F, 2.8F);
		head2.addChild(lEar01);
		setRotationAngle(lEar01, 0.6981F, 0.1222F, -0.6981F);
		lEar01.setTextureOffset(70, 0).addCuboid(0.0F, -1.0F, -1.6F, 1.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lEar02 = new ModelPart(this);
		lEar02.setPivot(-0.3F, -3.0F, -0.5F);
		lEar01.addChild(lEar02);
		setRotationAngle(lEar02, 0.2269F, 0.0F, -0.2618F);
		lEar02.setTextureOffset(78, 0).addCuboid(-1.4F, 2.0F, -1.4F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rEar01 = new ModelPart(this);
		rEar01.setPivot(-2.8F, -0.1F, 2.8F);
		head2.addChild(rEar01);
		setRotationAngle(rEar01, 0.6981F, -0.1222F, 0.6981F);
		rEar01.setTextureOffset(70, 0).addCuboid(-1.0F, -1.0F, -1.6F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart rEar02 = new ModelPart(this);
		rEar02.setPivot(0.3F, -3.0F, -0.5F);
		rEar01.addChild(rEar02);
		setRotationAngle(rEar02, 0.2269F, 0.0F, 0.2618F);
		rEar02.setTextureOffset(78, 0).addCuboid(0.4F, 2.0F, -1.4F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lCheekFur = new ModelPart(this);
		lCheekFur.setPivot(3.5F, -0.5F, -0.6F);
		head2.addChild(lCheekFur);
		setRotationAngle(lCheekFur, 0.1222F, -0.0873F, -0.5236F);
		lCheekFur.setTextureOffset(26, 4).addCuboid(0.0F, -0.8F, -3.3F, 0.0F, 6.0F, 5.0F, 0.0F, true);
		
		ModelPart rCheekFur = new ModelPart(this);
		rCheekFur.setPivot(-3.5F, -0.5F, -0.6F);
		head2.addChild(rCheekFur);
		setRotationAngle(rCheekFur, 0.1222F, 0.0873F, 0.5236F);
		rCheekFur.setTextureOffset(26, 4).addCuboid(0.0F, -0.8F, -3.3F, 0.0F, 6.0F, 5.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -4.2F, -0.1F);
		head2.addChild(snout);
		setRotationAngle(snout, 0.182F, 0.0F, 0.0F);
		snout.setTextureOffset(29, 35).addCuboid(-1.5F, -3.2F, -2.0F, 3.0F, 5.0F, 2.0F, 0.0F, false);
		
		ModelPart fur01 = new ModelPart(this);
		fur01.setPivot(0.0F, 0.0F, -2.9F);
		neck.addChild(fur01);
		setRotationAngle(fur01, 1.9635F, 0.0F, 0.0F);
		fur01.setTextureOffset(90, 0).addCuboid(-3.5F, 2.0F, 0.0F, 7.0F, 7.0F, 2.0F, 0.1F, false);
		
		BipedLeftArm = new ModelPart(this);
		BipedLeftArm.setPivot(5.5F, -2.3F, 2.0F);
		body.addChild(BipedLeftArm);
		setRotationAngle(BipedLeftArm, -0.2618F, -0.0873F, -0.2356F);
		BipedLeftArm.setTextureOffset(32, 47).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		ModelPart lArm02 = new ModelPart(this);
		lArm02.setPivot(0.0F, 11.5F, 0.0F);
		BipedLeftArm.addChild(lArm02);
		setRotationAngle(lArm02, -0.5236F, 0.0F, 0.1484F);
		lArm02.setTextureOffset(49, 46).addCuboid(-1.5F, -1.0F, -1.5F, 3.0F, 13.0F, 3.0F, 0.0F, false);
		
		ModelPart lArmFur = new ModelPart(this);
		lArmFur.setPivot(0.4F, -4.0F, 0.0F);
		lArm02.addChild(lArmFur);
		setRotationAngle(lArmFur, -0.4363F, 0.0873F, 0.0873F);
		lArmFur.setTextureOffset(62, 50).addCuboid(-0.5F, -0.5F, 1.5F, 1.0F, 8.0F, 4.0F, 0.0F, false);
		
		ModelPart lClawJoint = new ModelPart(this);
		lClawJoint.setPivot(0.0F, 8.6F, 0.0F);
		lArm02.addChild(lClawJoint);
		lClawJoint.setTextureOffset(0, 0).addCuboid(-0.4F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw01 = new ModelPart(this);
		lClaw01.setPivot(1.0F, 0.2F, 0.0F);
		lClawJoint.addChild(lClaw01);
		setRotationAngle(lClaw01, -0.1047F, 0.0F, 0.2269F);
		lClaw01.setTextureOffset(27, 0).addCuboid(-1.4F, 1.2F, -1.6F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw02 = new ModelPart(this);
		lClaw02.setPivot(1.0F, 0.2F, -0.1F);
		lClawJoint.addChild(lClaw02);
		setRotationAngle(lClaw02, 0.0F, 0.0F, 0.2269F);
		lClaw02.setTextureOffset(27, 0).addCuboid(-1.4F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw03 = new ModelPart(this);
		lClaw03.setPivot(1.0F, 0.2F, 0.8F);
		lClawJoint.addChild(lClaw03);
		setRotationAngle(lClaw03, 0.1047F, 0.0F, 0.2269F);
		lClaw03.setTextureOffset(27, 0).addCuboid(-1.4F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart leftHandItem = new ModelPart(this);
		leftHandItem.setPivot(-1.0F, 8.5F, -0.5F);
		lArm02.addChild(leftHandItem);
		
		BipedRightArm = new ModelPart(this);
		BipedRightArm.setPivot(-5.5F, -2.3F, 2.0F);
		body.addChild(BipedRightArm);
		setRotationAngle(BipedRightArm, -0.2618F, 0.0873F, 0.2356F);
		BipedRightArm.setTextureOffset(32, 47).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		
		ModelPart rArm02 = new ModelPart(this);
		rArm02.setPivot(0.0F, 11.5F, 0.0F);
		BipedRightArm.addChild(rArm02);
		setRotationAngle(rArm02, -0.5236F, 0.0F, -0.1484F);
		rArm02.setTextureOffset(49, 46).addCuboid(-1.5F, -1.0F, -1.5F, 3.0F, 13.0F, 3.0F, 0.0F, true);
		
		ModelPart rArmFur = new ModelPart(this);
		rArmFur.setPivot(-0.4F, -4.0F, 0.0F);
		rArm02.addChild(rArmFur);
		setRotationAngle(rArmFur, -0.4363F, -0.0873F, -0.0873F);
		rArmFur.setTextureOffset(62, 50).addCuboid(-0.5F, -0.5F, 1.5F, 1.0F, 8.0F, 4.0F, 0.0F, true);
		
		ModelPart rClawJoint = new ModelPart(this);
		rClawJoint.setPivot(0.0F, 8.6F, 0.0F);
		rArm02.addChild(rClawJoint);
		rClawJoint.setTextureOffset(0, 0).addCuboid(-0.6F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw01 = new ModelPart(this);
		rClaw01.setPivot(-1.0F, 0.2F, 0.0F);
		rClawJoint.addChild(rClaw01);
		setRotationAngle(rClaw01, -0.1047F, 0.0F, -0.2269F);
		rClaw01.setTextureOffset(27, 0).addCuboid(-0.6F, 1.2F, -1.6F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw02 = new ModelPart(this);
		rClaw02.setPivot(-1.0F, 0.2F, -0.1F);
		rClawJoint.addChild(rClaw02);
		setRotationAngle(rClaw02, 0.0F, 0.0F, -0.2269F);
		rClaw02.setTextureOffset(27, 0).addCuboid(-0.6F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw03 = new ModelPart(this);
		rClaw03.setPivot(-1.0F, 0.2F, 0.8F);
		rClawJoint.addChild(rClaw03);
		setRotationAngle(rClaw03, 0.1047F, 0.0F, -0.2269F);
		rClaw03.setTextureOffset(27, 0).addCuboid(-0.6F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rightHandItem = new ModelPart(this);
		rightHandItem.setPivot(1.0F, 8.5F, -0.5F);
		rArm02.addChild(rightHandItem);
		
		ModelPart stomach = new ModelPart(this);
		stomach.setPivot(0.0F, 6.9F, 2.6F);
		body.addChild(stomach);
		setRotationAngle(stomach, -0.3927F, 0.0F, 0.0F);
		stomach.setTextureOffset(16, 16).addCuboid(-5.0F, 0.0F, -3.0F, 10.0F, 12.0F, 6.0F, 0.0F, false);
		
		BipedLeftLeg = new ModelPart(this);
		BipedLeftLeg.setPivot(2.7F, 10.2F, -0.4F);
		stomach.addChild(BipedLeftLeg);
		setRotationAngle(BipedLeftLeg, -0.829F, -0.2269F, -0.0873F);
		BipedLeftLeg.setTextureOffset(11, 46).addCuboid(-2.3F, -1.1F, -1.9F, 5.0F, 13.0F, 5.0F, 0.0F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.0F, 9.8F, 0.5F);
		BipedLeftLeg.addChild(lLeg02);
		setRotationAngle(lLeg02, 1.309F, -0.0524F, 0.0F);
		lLeg02.setTextureOffset(0, 35).addCuboid(-2.01F, 0.4F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
		
		ModelPart lLeg03 = new ModelPart(this);
		lLeg03.setPivot(0.0F, 5.9F, 0.8F);
		lLeg02.addChild(lLeg03);
		setRotationAngle(lLeg03, -0.7854F, 0.0F, 0.0873F);
		lLeg03.setTextureOffset(0, 22).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, false);
		
		ModelPart lFoot = new ModelPart(this);
		lFoot.setPivot(0.0F, 8.7F, -1.2F);
		lLeg03.addChild(lFoot);
		setRotationAngle(lFoot, 0.1309F, 0.0436F, -0.0087F);
		lFoot.setTextureOffset(0, 14).addCuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart lFootClaw01 = new ModelPart(this);
		lFootClaw01.setPivot(-1.3F, 0.5F, -2.6F);
		lFoot.addChild(lFootClaw01);
		setRotationAngle(lFootClaw01, 0.2269F, 0.1047F, 0.0F);
		lFootClaw01.setTextureOffset(1, 48).addCuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		
		ModelPart lFootClaw02 = new ModelPart(this);
		lFootClaw02.setPivot(0.0F, 0.5F, -2.6F);
		lFoot.addChild(lFootClaw02);
		setRotationAngle(lFootClaw02, 0.2269F, 0.0F, 0.0F);
		lFootClaw02.setTextureOffset(1, 48).addCuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		
		ModelPart lFootClaw03 = new ModelPart(this);
		lFootClaw03.setPivot(1.3F, 0.5F, -2.6F);
		lFoot.addChild(lFootClaw03);
		setRotationAngle(lFootClaw03, 0.2269F, -0.1047F, 0.0F);
		lFootClaw03.setTextureOffset(1, 48).addCuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		
		BipedRightLeg = new ModelPart(this);
		BipedRightLeg.setPivot(-2.7F, 10.2F, -0.4F);
		stomach.addChild(BipedRightLeg);
		setRotationAngle(BipedRightLeg, -0.829F, 0.2269F, 0.0873F);
		BipedRightLeg.setTextureOffset(11, 46).addCuboid(-2.7F, -1.1F, -1.9F, 5.0F, 13.0F, 5.0F, 0.0F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(0.0F, 9.8F, 0.5F);
		BipedRightLeg.addChild(rLeg02);
		setRotationAngle(rLeg02, 1.309F, 0.0524F, 0.0F);
		rLeg02.setTextureOffset(0, 35).addCuboid(-1.99F, 0.4F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, true);
		
		ModelPart rLeg03 = new ModelPart(this);
		rLeg03.setPivot(0.0F, 5.9F, 0.8F);
		rLeg02.addChild(rLeg03);
		setRotationAngle(rLeg03, -0.7854F, 0.0F, -0.0873F);
		rLeg03.setTextureOffset(0, 22).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, true);
		
		ModelPart rFoot = new ModelPart(this);
		rFoot.setPivot(0.0F, 8.7F, -1.2F);
		rLeg03.addChild(rFoot);
		setRotationAngle(rFoot, 0.1309F, -0.0436F, 0.0087F);
		rFoot.setTextureOffset(0, 14).addCuboid(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F, 0.0F, true);
		
		ModelPart rFootClaw01 = new ModelPart(this);
		rFootClaw01.setPivot(1.3F, 0.5F, -2.6F);
		rFoot.addChild(rFootClaw01);
		setRotationAngle(rFootClaw01, 0.2269F, -0.1047F, 0.0F);
		rFootClaw01.setTextureOffset(1, 48).addCuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F, 0.0F, true);
		
		ModelPart rFootClaw02 = new ModelPart(this);
		rFootClaw02.setPivot(0.0F, 0.5F, -2.6F);
		rFoot.addChild(rFootClaw02);
		setRotationAngle(rFootClaw02, 0.2269F, 0.0F, 0.0F);
		rFootClaw02.setTextureOffset(1, 48).addCuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F, 0.0F, true);
		
		ModelPart rFootClaw03 = new ModelPart(this);
		rFootClaw03.setPivot(-1.3F, 0.5F, -2.6F);
		rFoot.addChild(rFootClaw03);
		setRotationAngle(rFootClaw03, 0.2269F, 0.1047F, 0.0F);
		rFootClaw03.setTextureOffset(1, 48).addCuboid(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F, 0.0F, true);
		
		tail01 = new ModelPart(this);
		tail01.setPivot(0.0F, 8.6F, 2.0F);
		stomach.addChild(tail01);
		setRotationAngle(tail01, 1 / 3f, 0.0F, 0.0F);
		tail01.setTextureOffset(112, 18).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		
		ModelPart tail02 = new ModelPart(this);
		tail02.setPivot(0.0F, 3.7F, 0.0F);
		tail01.addChild(tail02);
		setRotationAngle(tail02, -0.2094F, 0.0F, 0.0F);
		tail02.setTextureOffset(111, 26).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
		
		ModelPart tail03 = new ModelPart(this);
		tail03.setPivot(0.0F, 6.5F, 0.1F);
		tail02.addChild(tail03);
		setRotationAngle(tail03, 0.1396F, 0.0F, 0.0F);
		tail03.setTextureOffset(112, 40).addCuboid(-1.5F, 0.2F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		
		ModelPart tail04 = new ModelPart(this);
		tail04.setPivot(0.0F, 0.0F, 0.4F);
		tail03.addChild(tail04);
		setRotationAngle(tail04, 0.0698F, 0.0F, 0.0F);
		tail04.setTextureOffset(116, 7).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
		
		ModelPart fur06 = new ModelPart(this);
		fur06.setPivot(0.0F, 6.6F, 1.5F);
		stomach.addChild(fur06);
		setRotationAngle(fur06, 0.6981F, 0.0F, 0.0F);
		fur06.setTextureOffset(90, 52).addCuboid(-2.0F, -1.0F, 0.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);
		
		ModelPart fur05 = new ModelPart(this);
		fur05.setPivot(0.0F, 4.7F, 1.6F);
		stomach.addChild(fur05);
		setRotationAngle(fur05, 0.6981F, 0.0F, 0.0F);
		fur05.setTextureOffset(90, 43).addCuboid(-2.0F, -1.0F, 0.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);
		
		ModelPart fur04 = new ModelPart(this);
		fur04.setPivot(0.0F, -0.3F, 4.9F);
		body.addChild(fur04);
		setRotationAngle(fur04, 0.4363F, 0.0F, 0.0F);
		fur04.setTextureOffset(90, 35).addCuboid(-2.5F, -1.0F, 0.0F, 5.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart fur03 = new ModelPart(this);
		fur03.setPivot(0.0F, -3.6F, 4.5F);
		body.addChild(fur03);
		setRotationAngle(fur03, 0.6807F, 0.0F, 0.0F);
		fur03.setTextureOffset(90, 24).addCuboid(-4.0F, -1.0F, 0.0F, 8.0F, 7.0F, 2.0F, 0.0F, false);
		
		ModelPart fur02 = new ModelPart(this);
		fur02.setPivot(0.0F, -4.7F, 3.4F);
		body.addChild(fur02);
		setRotationAngle(fur02, 1.0908F, 0.0F, 0.0F);
		fur02.setTextureOffset(90, 11).addCuboid(-5.0F, -1.0F, 0.0F, 10.0F, 8.0F, 2.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		neck.copyPositionAndRotation(super.head);
		neck.pivotY -= 4;
		neck.pitch -= 3 / 4f;
		BipedLeftArm.copyPositionAndRotation(super.leftArm);
		BipedLeftArm.pitch -= 1 / 8f;
		BipedLeftArm.pitch /= 2;
		BipedLeftArm.roll -= 1 / 3f;
		BipedLeftArm.pivotY -= 5;
		BipedLeftArm.pivotZ += 2;
		BipedRightArm.copyPositionAndRotation(super.rightArm);
		BipedRightArm.pitch -= 1 / 8f;
		BipedRightArm.pitch /= 2;
		BipedRightArm.roll += 1 / 3f;
		BipedRightArm.pivotY -= 5;
		BipedRightArm.pivotZ += 2;
		BipedRightLeg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * limbDistance * 0.5f - 7 / 8f;
		BipedLeftLeg.pitch = -BipedRightLeg.pitch - 7 / 4f;
		tail01.roll = MathHelper.sin(animationProgress / 8) / 8;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}