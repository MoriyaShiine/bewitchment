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
	private final ModelPart head;
	private final ModelPart lLeg01;
	private final ModelPart rLeg01;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWing04;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWing04;
	
	public RavenEntityModel() {
		textureWidth = 64;
		textureHeight = 32;
		
		body = new ModelPart(this);
		body.setPivot(0.0F, 18.2F, -1.6F);
		setRotation(body, -0.3142F, 0.0F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-2.0F, -1.5F, -2.5F, 4.0F, 3.0F, 6.0F, 0.0F, false);
		
		ModelPart neck = new ModelPart(this);
		neck.setPivot(0.0F, 0.0F, -0.9F);
		body.addChild(neck);
		setRotation(neck, -0.5236F, 0.0F, 0.0F);
		neck.setTextureOffset(16, 0).addCuboid(-1.5F, -1.3F, -3.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		
		ModelPart neckFeathers01 = new ModelPart(this);
		neckFeathers01.setPivot(0.0F, 1.1F, -1.7F);
		neck.addChild(neckFeathers01);
		setRotation(neckFeathers01, -0.4014F, 0.0F, 0.0F);
		neckFeathers01.setTextureOffset(13, 27).addCuboid(-1.49F, -0.5F, -0.2F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart neckFeathers02 = new ModelPart(this);
		neckFeathers02.setPivot(0.0F, 0.7F, -2.3F);
		neck.addChild(neckFeathers02);
		setRotation(neckFeathers02, -0.8727F, 0.0F, 0.0F);
		neckFeathers02.setTextureOffset(0, 27).addCuboid(-1.5F, -0.5F, -0.2F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, -0.4F, -2.5F);
		neck.addChild(head);
		setRotation(head, 0.8727F, 0.0F, 0.0F);
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
		
		ModelPart tail01 = new ModelPart(this);
		tail01.setPivot(0.0F, -0.9F, 3.4F);
		body.addChild(tail01);
		setRotation(tail01, 0.0524F, 0.0F, 0.0F);
		tail01.setTextureOffset(0, 10).addCuboid(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart tail02 = new ModelPart(this);
		tail02.setPivot(0.0F, 0.8F, 0.0F);
		tail01.addChild(tail02);
		tail02.setTextureOffset(0, 15).addCuboid(-1.49F, -0.5F, -0.4F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart middleTalePlume = new ModelPart(this);
		middleTalePlume.setPivot(0.0F, -0.2F, 1.7F);
		tail01.addChild(middleTalePlume);
		middleTalePlume.setTextureOffset(49, 6).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F, 0.0F, false);
		
		ModelPart lTailPlume = new ModelPart(this);
		lTailPlume.setPivot(-0.5F, 0.7F, 1.2F);
		tail01.addChild(lTailPlume);
		setRotation(lTailPlume, 0.0F, -0.5236F, 0.1745F);
		lTailPlume.setTextureOffset(51, 13).addCuboid(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F, 0.0F, false);
		
		ModelPart rTailPlume = new ModelPart(this);
		rTailPlume.setPivot(0.5F, 0.7F, 1.2F);
		tail01.addChild(rTailPlume);
		setRotation(rTailPlume, 0.0F, 0.5236F, -0.1745F);
		rTailPlume.setTextureOffset(51, 13).addCuboid(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F, 0.0F, true);
		
		lLeg01 = new ModelPart(this);
		lLeg01.setPivot(-1.2F, 0.1F, 2.3F);
		body.addChild(lLeg01);
		setRotation(lLeg01, 0.3142F, 0.0F, 0.0F);
		lLeg01.setTextureOffset(34, 0).addCuboid(-1.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(-0.5F, 2.0F, 0.0F);
		lLeg01.addChild(lLeg02);
		setRotation(lLeg02, -0.1396F, 0.0F, 0.0F);
		lLeg02.setTextureOffset(29, 0).addCuboid(-0.49F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lFoot = new ModelPart(this);
		lFoot.setPivot(0.0F, 2.6F, 0.0F);
		lLeg02.addChild(lFoot);
		setRotation(lFoot, 0.1396F, 0.0F, 0.0F);
		lFoot.setTextureOffset(29, 5).addCuboid(-0.51F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		rLeg01 = new ModelPart(this);
		rLeg01.setPivot(1.2F, 0.1F, 2.3F);
		body.addChild(rLeg01);
		setRotation(rLeg01, 0.3142F, 0.0F, 0.0F);
		rLeg01.setTextureOffset(34, 0).addCuboid(0.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(0.5F, 2.0F, 0.0F);
		rLeg01.addChild(rLeg02);
		setRotation(rLeg02, -0.1396F, 0.0F, 0.0F);
		rLeg02.setTextureOffset(29, 0).addCuboid(-0.51F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rFoot = new ModelPart(this);
		rFoot.setPivot(0.0F, 2.6F, 0.1F);
		rLeg02.addChild(rFoot);
		setRotation(rFoot, 0.1396F, 0.0F, 0.0F);
		rFoot.setTextureOffset(29, 5).addCuboid(-0.49F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
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
		
		lWing01 = new ModelPart(this);
		lWing01.setPivot(-1.9F, -0.3F, -1.1F);
		body.addChild(lWing01);
		setRotation(lWing01, -0.0873F, 1.2741F, 0.0F);
		lWing01.setTextureOffset(10, 10).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F, 0.0F, true);
		
		lWing02 = new ModelPart(this);
		lWing02.setPivot(-3.5F, 0.0F, 0.0F);
		lWing01.addChild(lWing02);
		setRotation(lWing02, 0.0F, 0.4538F, 0.0F);
		lWing02.setTextureOffset(12, 18).addCuboid(-4.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, true);
		
		lWing03 = new ModelPart(this);
		lWing03.setPivot(-3.5F, 0.0F, 0.0F);
		lWing02.addChild(lWing03);
		setRotation(lWing03, 0.0F, 0.4887F, 0.0F);
		lWing03.setTextureOffset(31, 9).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, true);
		
		lWing04 = new ModelPart(this);
		lWing04.setPivot(-1.8F, 0.4F, 0.0F);
		lWing03.addChild(lWing04);
		lWing04.setTextureOffset(29, 18).addCuboid(-8.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F, 0.0F, true);
		
		rWing01 = new ModelPart(this);
		rWing01.setPivot(1.9F, -0.3F, -1.1F);
		body.addChild(rWing01);
		setRotation(rWing01, -0.0873F, -1.2741F, 0.0F);
		rWing01.setTextureOffset(10, 10).addCuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F, 0.0F, false);
		
		rWing02 = new ModelPart(this);
		rWing02.setPivot(3.5F, 0.0F, 0.0F);
		rWing01.addChild(rWing02);
		setRotation(rWing02, 0.0F, -0.4538F, 0.0F);
		rWing02.setTextureOffset(12, 18).addCuboid(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, false);
		
		rWing03 = new ModelPart(this);
		rWing03.setPivot(3.5F, 0.0F, 0.0F);
		rWing02.addChild(rWing03);
		setRotation(rWing03, 0.0F, -0.4887F, 0.0F);
		rWing03.setTextureOffset(31, 9).addCuboid(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F, 0.0F, false);
		
		rWing04 = new ModelPart(this);
		rWing04.setPivot(1.8F, 0.4F, 0.0F);
		rWing03.addChild(rWing04);
		rWing04.setTextureOffset(29, 18).addCuboid(0.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 1;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		boolean flying = !entity.isOnGround();
		lWing03.visible = lWing04.visible = rWing03.visible = rWing04.visible = flying;
		if (flying) {
			head.pitch += -2 / 3f;
			body.pitch = 0;
			lWing01.yaw = 0;
			lWing01.roll = (float) -(MathHelper.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing02.yaw = 0;
			lWing02.roll = lWing01.roll / 4;
			lWing03.yaw = 0;
			lWing03.roll = lWing02.roll / 4;
			lWing04.roll = lWing03.roll / 4;
			rWing01.yaw = 0;
			rWing01.roll = -lWing01.roll;
			rWing02.yaw = 0;
			rWing02.roll = -lWing02.roll;
			rWing03.yaw = 0;
			rWing03.roll = -lWing03.roll;
			rWing04.roll = -lWing04.roll;
			lLeg01.pitch = 1;
			rLeg01.pitch = 1;
		}
		else {
			body.pitch = -0.3142f;
			lWing01.yaw = 1.2741f;
			lWing01.roll = 0;
			lWing02.yaw = 0.4538f;
			lWing02.roll = 0;
			lWing03.yaw = 0.4887f;
			lWing03.roll = 0;
			lWing04.roll = 0;
			rWing01.yaw = -1.2741f;
			rWing01.roll = 0;
			rWing02.yaw = -0.4538f;
			rWing02.roll = 0;
			rWing03.yaw = -0.4887f;
			rWing03.roll = 0;
			rWing04.roll = 0;
			lLeg01.pitch = (MathHelper.cos((float) (limbAngle + Math.PI)) * limbDistance) + 0.3142f;
			rLeg01.pitch = (MathHelper.cos(limbAngle) * limbDistance) + 0.3142f;
		}
		boolean sitting = entity.isInSittingPose();
		lLeg01.visible = rLeg01.visible = !sitting;
		if (sitting) {
			body.pivotY = 21.5f;
		}
		else {
			body.pivotY = 18.2f;
		}
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}