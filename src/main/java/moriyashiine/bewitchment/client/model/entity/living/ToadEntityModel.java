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
	private final ModelPart lLeg00;
	private final ModelPart lLeg01;
	private final ModelPart lFoot;
	private final ModelPart rLeg00;
	private final ModelPart rLeg01;
	private final ModelPart rFoot;
	private final ModelPart lArm00;
	private final ModelPart lHand;
	private final ModelPart rArm00;
	private final ModelPart rHand;
	private final ModelPart head;
	
	public ToadEntityModel() {
		textureWidth = 32;
		textureHeight = 32;
		
		body = new ModelPart(this);
		body.setPivot(0.0F, 18.8F, 0.7F);
		setRotation(body, -0.4014F, 0.0F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 8.0F, 0.0F, false);
		
		lLeg00 = new ModelPart(this);
		lLeg00.setPivot(3.7F, -0.2795F, 3.6907F);
		body.addChild(lLeg00);
		setRotation(lLeg00, -0.7854F, -0.3491F, -0.1396F);
		lLeg00.setTextureOffset(0, 13).addCuboid(-2.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F, 0.0F, false);
		
		lLeg01 = new ModelPart(this);
		lLeg01.setPivot(-1.1F, 3.2F, 0.6F);
		lLeg00.addChild(lLeg01);
		setRotation(lLeg01, 0.8727F, 0.0F, 0.0F);
		lLeg01.setTextureOffset(0, 21).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		lFoot = new ModelPart(this);
		lFoot.setPivot(0.0F, 1.3F, 3.4F);
		lLeg01.addChild(lFoot);
		setRotation(lFoot, 0.3142F, 0.0F, 0.0F);
		lFoot.setTextureOffset(0, 27).addCuboid(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F, 0.0F, false);
		
		rLeg00 = new ModelPart(this);
		rLeg00.setPivot(-3.7F, -0.2795F, 3.6907F);
		body.addChild(rLeg00);
		setRotation(rLeg00, -0.7854F, 0.3491F, 0.1396F);
		rLeg00.setTextureOffset(0, 13).addCuboid(0.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F, 0.0F, true);
		
		rLeg01 = new ModelPart(this);
		rLeg01.setPivot(1.1F, 3.2F, 0.6F);
		rLeg00.addChild(rLeg01);
		setRotation(rLeg01, 0.8727F, 0.0F, 0.0F);
		rLeg01.setTextureOffset(0, 21).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);
		
		rFoot = new ModelPart(this);
		rFoot.setPivot(0.0F, 1.3F, 3.4F);
		rLeg01.addChild(rFoot);
		setRotation(rFoot, 0.3142F, 0.0F, 0.0F);
		rFoot.setTextureOffset(0, 27).addCuboid(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F, 0.0F, true);
		
		lArm00 = new ModelPart(this);
		lArm00.setPivot(4.1F, 0.5F, -2.0F);
		body.addChild(lArm00);
		setRotation(lArm00, 0.3491F, 0.0F, -0.3491F);
		lArm00.setTextureOffset(10, 13).addCuboid(-2.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart lArm01 = new ModelPart(this);
		lArm01.setPivot(-1.3F, 1.4F, 0.3F);
		lArm00.addChild(lArm01);
		setRotation(lArm01, -0.3491F, 0.0F, 0.2618F);
		lArm01.setTextureOffset(11, 18).addCuboid(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
		
		lHand = new ModelPart(this);
		lHand.setPivot(0.0F, 3.8F, -0.3F);
		lArm01.addChild(lHand);
		setRotation(lHand, 0.4538F, -0.1745F, 0.0F);
		lHand.setTextureOffset(-2, 0).addCuboid(-1.0F, 0.2F, -2.7F, 3.0F, 0.0F, 4.0F, 0.0F, false);
		
		rArm00 = new ModelPart(this);
		rArm00.setPivot(-4.1F, 0.5F, -2.0F);
		body.addChild(rArm00);
		setRotation(rArm00, 0.3491F, 0.0F, 0.3491F);
		rArm00.setTextureOffset(10, 13).addCuboid(0.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rArm01 = new ModelPart(this);
		rArm01.setPivot(1.3F, 1.4F, 0.3F);
		rArm00.addChild(rArm01);
		setRotation(rArm01, -0.3491F, 0.0F, -0.2618F);
		rArm01.setTextureOffset(11, 18).addCuboid(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);
		
		rHand = new ModelPart(this);
		rHand.setPivot(0.0F, 3.8F, -0.3F);
		rArm01.addChild(rHand);
		setRotation(rHand, 0.4538F, 0.1745F, 0.0F);
		rHand.setTextureOffset(-2, 0).addCuboid(-2.0F, 0.2F, -2.7F, 3.0F, 0.0F, 4.0F, 0.0F, true);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, -0.9F, -2.9F);
		body.addChild(head);
		setRotation(head, 0.4014F, 0.0F, 0.0F);
		head.setTextureOffset(12, 20).addCuboid(-2.5F, -1.5F, -5.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart throat = new ModelPart(this);
		throat.setPivot(0.0F, 0.9F, -0.4F);
		head.addChild(throat);
		throat.setTextureOffset(18, 14).addCuboid(-2.49F, -0.5F, -1.5F, 5.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, 0.0F, -2.5F);
		throat.addChild(lowerJaw);
		lowerJaw.setTextureOffset(13, 28).addCuboid(-2.1F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		boolean altLegs = false;
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 0.5f;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 1 / 3f;
		if (entity.isInSittingPose() || !entity.isOnGround()) {
			head.pitch -= 1 / 3f;
			body.pitch = 0;
			lArm00.pitch = -2 / 3f;
			lHand.pitch = 1.5f;
			lLeg00.pitch = -1 / 3f;
			lLeg00.yaw = 0;
			if (!entity.isOnGround()) {
				lLeg00.pitch = 1;
				altLegs = true;
			}
		}
		else {
			body.pitch = -0.4014f;
			lArm00.pitch = 0.3491f;
			lHand.pitch = 0.4538f;
			lLeg00.pitch = -0.7854f;
			lLeg00.yaw = -0.3491f;
		}
		if (altLegs) {
			lLeg01.pitch = -0.5f;
			lFoot.pitch = 1.5f;
		}
		else {
			lLeg01.pitch = 0.8727f;
			lFoot.pitch = 0.3142f;
		}
		rArm00.pitch = lArm00.pitch;
		rHand.pitch = lHand.pitch;
		rLeg00.pitch = lLeg00.pitch;
		rLeg00.yaw = -lLeg00.yaw;
		rLeg01.pitch = lLeg01.pitch;
		rFoot.pitch = lFoot.pitch;
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