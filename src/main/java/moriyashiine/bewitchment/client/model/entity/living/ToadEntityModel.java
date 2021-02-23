package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;

@Environment(EnvType.CLIENT)
public class ToadEntityModel<T extends ToadEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart lArm01;
	private final ModelPart lHand;
	private final ModelPart rArm01;
	private final ModelPart rHand;
	private final ModelPart head;
	private final ModelPart lLeg01;
	private final ModelPart lLeg02;
	private final ModelPart lFoot;
	private final ModelPart rLeg01;
	private final ModelPart rLeg02;
	private final ModelPart rFoot;
	
	public ToadEntityModel() {
		textureWidth = 32;
		textureHeight = 32;
		body = new ModelPart(this);
		body.setPivot(0.0F, 19.05F, 0.7F);
		setRotationAngle(body, -0.4014F, 0.0F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 8.0F, 0.0F, false);
		
		lArm01 = new ModelPart(this);
		lArm01.setPivot(4.1F, 19.05F, -1.3F);
		setRotationAngle(lArm01, -0.1745F, 0.0F, -0.3491F);
		lArm01.setTextureOffset(10, 13).addCuboid(-2.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart lArm02 = new ModelPart(this);
		lArm02.setPivot(-1.3F, 1.4F, 0.3F);
		lArm01.addChild(lArm02);
		setRotationAngle(lArm02, -0.3491F, 0.0F, 0.2618F);
		lArm02.setTextureOffset(11, 18).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
		
		lHand = new ModelPart(this);
		lHand.setPivot(0.0F, 3.8F, -0.3F);
		lArm02.addChild(lHand);
		setRotationAngle(lHand, 0.4538F, 0.0873F, 0.0873F);
		lHand.setTextureOffset(-2, 0).addCuboid(-1.5F, 0.0F, -2.7F, 3.0F, 0.0F, 4.0F, 0.0F, false);
		
		rArm01 = new ModelPart(this);
		rArm01.setPivot(-4.1F, 19.05F, -1.3F);
		setRotationAngle(rArm01, -0.1745F, 0.0F, 0.3491F);
		rArm01.setTextureOffset(10, 13).addCuboid(0.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rArm02 = new ModelPart(this);
		rArm02.setPivot(1.3F, 1.4F, 0.3F);
		rArm01.addChild(rArm02);
		setRotationAngle(rArm02, -0.3491F, 0.0F, -0.2618F);
		rArm02.setTextureOffset(11, 18).addCuboid(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);
		
		rHand = new ModelPart(this);
		rHand.setPivot(0.0F, 3.8F, -0.3F);
		rArm02.addChild(rHand);
		setRotationAngle(rHand, 0.4538F, -0.0873F, -0.0873F);
		rHand.setTextureOffset(-2, 0).addCuboid(-1.5F, 0.0F, -2.7F, 3.0F, 0.0F, 4.0F, 0.0F, true);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 17.15F, -1.7F);
		head.setTextureOffset(12, 20).addCuboid(-2.5F, -1.5F, -5.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart throat = new ModelPart(this);
		throat.setPivot(0.0F, 0.9F, -0.4F);
		head.addChild(throat);
		throat.setTextureOffset(18, 14).addCuboid(-2.49F, -0.5F, -1.5F, 5.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, 0.0F, -2.5F);
		throat.addChild(lowerJaw);
		lowerJaw.setTextureOffset(13, 28).addCuboid(-2.1F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
		
		lLeg01 = new ModelPart(this);
		lLeg01.setPivot(4.7F, 18.85F, 4.0F);
		setRotationAngle(lLeg01, -1.0036F, -0.3491F, -0.1396F);
		lLeg01.setTextureOffset(0, 13).addCuboid(-2.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F, 0.0F, false);
		
		lLeg02 = new ModelPart(this);
		lLeg02.setPivot(-1.1F, 3.2F, 0.6F);
		lLeg01.addChild(lLeg02);
		setRotationAngle(lLeg02, 0.6981F, 0.0F, 0.0F);
		lLeg02.setTextureOffset(0, 21).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		lFoot = new ModelPart(this);
		lFoot.setPivot(0.0F, 1.3F, 3.4F);
		lLeg02.addChild(lFoot);
		setRotationAngle(lFoot, 0.3578F, 0.0F, 0.1309F);
		lFoot.setTextureOffset(0, 27).addCuboid(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F, 0.0F, false);
		
		rLeg01 = new ModelPart(this);
		rLeg01.setPivot(-4.7F, 18.85F, 4.0F);
		setRotationAngle(rLeg01, -1.0036F, 0.3491F, 0.1396F);
		rLeg01.setTextureOffset(0, 13).addCuboid(0.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F, 0.0F, true);
		
		rLeg02 = new ModelPart(this);
		rLeg02.setPivot(1.1F, 3.2F, 0.6F);
		rLeg01.addChild(rLeg02);
		setRotationAngle(rLeg02, 0.6981F, 0.0F, 0.0F);
		rLeg02.setTextureOffset(0, 21).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);
		
		rFoot = new ModelPart(this);
		rFoot.setPivot(0.0F, 1.3F, 3.4F);
		rLeg02.addChild(rFoot);
		setRotationAngle(rFoot, 0.3578F, 0.0F, -0.1309F);
		rFoot.setTextureOffset(0, 27).addCuboid(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F, 0.0F, true);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		boolean altLegs = false;
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 0.0087f;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 1 / 3f;
		if (entity.isInSittingPose() || !entity.isOnGround()) {
			head.pitch -= 1 / 3f;
			body.pitch = 0;
			lArm01.pitch = -2 / 3f;
			lHand.pitch = 1.5f;
			lLeg01.pitch = -1 / 3f;
			lLeg01.yaw = 0;
			if (!entity.isOnGround()) {
				lLeg01.pitch = 1;
				altLegs = true;
			}
		}
		else {
			body.pitch = -0.4014f;
			lArm01.pitch = -0.1745f;
			lHand.pitch = 0.4538f;
			lLeg01.pitch = -1.0036f;
			lLeg01.yaw = -0.3491f;
		}
		if (altLegs) {
			lLeg02.pitch = -0.5f;
			lFoot.pitch = 1.5f;
		}
		else {
			lLeg02.pitch = 0.8727f;
			lFoot.pitch = 0.3142f;
		}
		rArm01.pitch = lArm01.pitch;
		rHand.pitch = lHand.pitch;
		rLeg01.pitch = lLeg01.pitch;
		rLeg01.yaw = -lLeg01.yaw;
		rLeg02.pitch = lLeg02.pitch;
		rFoot.pitch = lFoot.pitch;
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(head);
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body, lArm01, rArm01, lLeg01, rLeg01);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}