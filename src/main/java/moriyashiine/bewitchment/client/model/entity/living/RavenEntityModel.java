package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class RavenEntityModel<T extends RavenEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWingTip;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWingTip;
	private final ModelPart lLeg01;
	private final ModelPart rLeg01;
	private final ModelPart head;
	
	public RavenEntityModel() {
		textureWidth = 64;
		textureHeight = 32;
		body = new ModelPart(this);
		body.setPivot(0.0F, 18.2F, -1.6F);
		body.setTextureOffset(0, 0).addCuboid(-2.0F, -1.5F, -2.5F, 4.0F, 3.0F, 6.0F, 0.0F, false);
		
		ModelPart tail01 = new ModelPart(this);
		tail01.setPivot(0.0F, -0.9F, 3.4F);
		body.addChild(tail01);
		setRotation(tail01, 0.0524F, 0.0F, 0.0F);
		tail01.setTextureOffset(0, 10).addCuboid(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart tail02 = new ModelPart(this);
		tail02.setPivot(0.0F, 0.8F, 0.0F);
		tail01.addChild(tail02);
		tail02.setTextureOffset(0, 15).addCuboid(-1.49F, -0.5F, -0.4F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart tailPlumeM = new ModelPart(this);
		tailPlumeM.setPivot(0.0F, -0.2F, 1.7F);
		tail01.addChild(tailPlumeM);
		tailPlumeM.setTextureOffset(49, 6).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F, 0.0F, false);
		
		ModelPart tailPlumeL = new ModelPart(this);
		tailPlumeL.setPivot(-0.5F, 0.7F, 1.2F);
		tail01.addChild(tailPlumeL);
		setRotation(tailPlumeL, 0.0F, -0.5236F, 0.1745F);
		tailPlumeL.setTextureOffset(51, 13).addCuboid(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F, 0.0F, false);
		
		ModelPart tailPlumeR = new ModelPart(this);
		tailPlumeR.setPivot(0.5F, 0.7F, 1.2F);
		tail01.addChild(tailPlumeR);
		setRotation(tailPlumeR, 0.0F, 0.5236F, -0.1745F);
		tailPlumeR.setTextureOffset(51, 13).addCuboid(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F, 0.0F, true);
		
		ModelPart tail03 = new ModelPart(this);
		tail03.setPivot(0.0F, 0.4F, 3.5F);
		body.addChild(tail03);
		setRotation(tail03, 0.2094F, 0.0F, 0.0F);
		tail03.setTextureOffset(0, 20).addCuboid(-1.5F, 0.0F, -0.8F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart bodyFeathers = new ModelPart(this);
		bodyFeathers.setPivot(0.0F, 1.1F, 1.1F);
		body.addChild(bodyFeathers);
		setRotation(bodyFeathers, 0.1396F, 0.0F, 0.0F);
		bodyFeathers.setTextureOffset(26, 26).addCuboid(-1.5F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F, 0.0F, false);
		
		ModelPart neckFeathers01 = new ModelPart(this);
		neckFeathers01.setPivot(0.0F, 1.1F, -2.6F);
		body.addChild(neckFeathers01);
		setRotation(neckFeathers01, -0.8378F, 0.0F, 0.0F);
		neckFeathers01.setTextureOffset(13, 27).addCuboid(-1.49F, -0.8F, -0.2F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart neckFeathers02 = new ModelPart(this);
		neckFeathers02.setPivot(0.0F, 0.7F, -3.2F);
		body.addChild(neckFeathers02);
		setRotation(neckFeathers02, -1.2654F, 0.0F, 0.0F);
		neckFeathers02.setTextureOffset(0, 27).addCuboid(-1.5F, -0.9F, -1.2F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		lWing01 = new ModelPart(this);
		lWing01.setPivot(1.9F, 17.9F, -2.7F);
		setRotation(lWing01, -0.3491F, -1.2741F, 0.0F);
		lWing01.setTextureOffset(10, 10).addCuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F, 0.0F, false);
		
		lWing02 = new ModelPart(this);
		lWing02.setPivot(3.5F, 0.0F, 0.0F);
		lWing01.addChild(lWing02);
		lWing02.setTextureOffset(12, 18).addCuboid(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, false);
		
		lWing03 = new ModelPart(this);
		lWing03.setPivot(3.5F, 0.0F, 0.0F);
		lWing02.addChild(lWing03);
		lWing03.setTextureOffset(31, 9).addCuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, false);
		
		lWingTip = new ModelPart(this);
		lWingTip.setPivot(1.8F, 0.4F, 0.0F);
		lWing03.addChild(lWingTip);
		lWingTip.setTextureOffset(29, 18).addCuboid(0.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);
		
		rWing01 = new ModelPart(this);
		rWing01.setPivot(-1.9F, 17.9F, -2.7F);
		setRotation(rWing01, -0.3491F, 1.2741F, 0.0F);
		rWing01.setTextureOffset(10, 10).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F, 0.0F, true);
		
		rWing02 = new ModelPart(this);
		rWing02.setPivot(-3.5F, 0.0F, 0.0F);
		rWing01.addChild(rWing02);
		rWing02.setTextureOffset(12, 18).addCuboid(-4.0F, -0.52F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, true);
		
		rWing03 = new ModelPart(this);
		rWing03.setPivot(-3.5F, 0.0F, 0.0F);
		rWing02.addChild(rWing03);
		rWing03.setTextureOffset(31, 9).addCuboid(-4.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, true);
		
		rWingTip = new ModelPart(this);
		rWingTip.setPivot(-1.8F, 0.4F, 0.0F);
		rWing03.addChild(rWingTip);
		rWingTip.setTextureOffset(29, 18).addCuboid(-8.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F, 0.0F, true);
		
		lLeg01 = new ModelPart(this);
		lLeg01.setPivot(1.2F, 19.3F, 0.7F);
		setRotation(lLeg01, 0.0087F, 0.0F, 0.0F);
		lLeg01.setTextureOffset(34, 0).addCuboid(0.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.5F, 2.0F, 0.0F);
		lLeg01.addChild(lLeg02);
		setRotation(lLeg02, -0.1396F, 0.0F, 0.0F);
		lLeg02.setTextureOffset(29, 0).addCuboid(-0.51F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lFoot = new ModelPart(this);
		lFoot.setPivot(0.0F, 2.6F, 0.0F);
		lLeg02.addChild(lFoot);
		setRotation(lFoot, 0.1396F, 0.0F, 0.0F);
		lFoot.setTextureOffset(29, 5).addCuboid(-0.49F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		rLeg01 = new ModelPart(this);
		rLeg01.setPivot(-1.2F, 19.3F, 0.7F);
		setRotation(rLeg01, 0.0087F, 0.0F, 0.0F);
		rLeg01.setTextureOffset(34, 0).addCuboid(-1.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(-0.5F, 2.0F, 0.0F);
		rLeg01.addChild(rLeg02);
		setRotation(rLeg02, -0.1396F, 0.0F, 0.0F);
		rLeg02.setTextureOffset(29, 0).addCuboid(-0.49F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart rFoot = new ModelPart(this);
		rFoot.setPivot(0.0F, 2.6F, 0.1F);
		rLeg02.addChild(rFoot);
		setRotation(rFoot, 0.1396F, 0.0F, 0.0F);
		rFoot.setTextureOffset(29, 5).addCuboid(-0.51F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 15.8F, -3.0F);
		head.setTextureOffset(40, 0).addCuboid(-1.49F, -1.5F, -2.7F, 3.0F, 3.0F, 3.0F, 0.0F, false);
		
		ModelPart beakBottom = new ModelPart(this);
		beakBottom.setPivot(0.0F, 0.8F, -1.9F);
		head.addChild(beakBottom);
		setRotation(beakBottom, -0.0873F, 0.0F, 0.0F);
		beakBottom.setTextureOffset(54, 0).addCuboid(-0.49F, -0.5F, -2.8F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart beakTop = new ModelPart(this);
		beakTop.setPivot(0.0F, -0.1F, -2.3F);
		head.addChild(beakTop);
		setRotation(beakTop, 0.1396F, 0.0F, 0.0F);
		beakTop.setTextureOffset(54, 0).addCuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 0.8727f;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		boolean flying = !entity.isOnGround();
		lWingTip.visible = rWingTip.visible = flying;
		if (flying) {
			head.pitch += -2 / 3f;
			body.pitch = 0;
			lWing01.yaw = 0;
			lWing01.roll = (float) -(MathHelper.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing02.yaw = 0;
			lWing02.roll = lWing01.roll / 4;
			lWing03.yaw = 0;
			lWing03.roll = lWing02.roll / 4;
			lWingTip.roll = lWing03.roll / 4;
			rWing01.yaw = 0;
			rWing01.roll = -lWing01.roll;
			rWing02.yaw = 0;
			rWing02.roll = -lWing02.roll;
			rWing03.yaw = 0;
			rWing03.roll = -lWing03.roll;
			rWingTip.roll = -lWingTip.roll;
			lLeg01.pitch = 1;
			rLeg01.pitch = 1;
		}
		else {
			body.pitch = -0.3142f;
			lWing01.yaw = -1.2741f;
			lWing01.roll = 0;
			lWing02.yaw = -0.4538f;
			lWing02.roll = 0;
			lWing03.yaw = -0.4887f;
			lWing03.roll = 0;
			lWingTip.roll = 0;
			rWing01.yaw = 1.2741f;
			rWing01.roll = 0;
			rWing02.yaw = 0.4538f;
			rWing02.roll = 0;
			rWing03.yaw = 0.4887f;
			rWing03.roll = 0;
			rWingTip.roll = 0;
			lLeg01.pitch = (MathHelper.cos((float) (limbAngle + Math.PI)) * limbDistance) + 0.0087f;
			rLeg01.pitch = (MathHelper.cos(limbAngle) * limbDistance) + 0.0087f;
		}
		boolean sitting = entity.isInSittingPose();
		lLeg01.visible = rLeg01.visible = !sitting;
		if (sitting) {
			head.pivotY = 19.1f;
			body.pivotY = 21.5f;
			lWing01.pivotY = 21.2f;
			rWing01.pivotY = 21.2f;
		}
		else {
			head.pivotY = 15.8f;
			body.pivotY = 18.2f;
			lWing01.pivotY = 17.9f;
			rWing01.pivotY = 17.9f;
		}
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(head);
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body, lWing01, rWing01, lLeg01, rLeg01);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
