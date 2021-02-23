package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class OwlEntityModel<T extends OwlEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart rFoot;
	private final ModelPart lFoot;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWing04;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWing04;
	private final ModelPart tail01;
	private final ModelPart head;
	
	public OwlEntityModel() {
		textureWidth = 64;
		textureHeight = 32;
		body = new ModelPart(this);
		body.setPivot(0.0F, 13.5F, 0.0F);
		body.setTextureOffset(0, 14).addCuboid(-4.0F, 0.0F, -3.5F, 8.0F, 9.0F, 7.0F, 0.0F, false);
		
		lFoot = new ModelPart(this);
		lFoot.setPivot(-1.8F, 8.9F, -0.4F);
		body.addChild(lFoot);
		setRotation(lFoot, 0.0873F, 0.0873F, 0.0F);
		lFoot.setTextureOffset(32, 22).addCuboid(-1.0F, 0.0F, -3.4F, 2.0F, 2.0F, 4.0F, 0.0F, false);
		
		ModelPart lBackTalon01 = new ModelPart(this);
		lBackTalon01.setPivot(-0.5F, 1.2F, 0.2F);
		lFoot.addChild(lBackTalon01);
		setRotation(lBackTalon01, -0.1745F, -0.0698F, 0.0F);
		lBackTalon01.setTextureOffset(39, 28).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart lBackTalon02 = new ModelPart(this);
		lBackTalon02.setPivot(0.5F, 1.2F, 0.2F);
		lFoot.addChild(lBackTalon02);
		setRotation(lBackTalon02, -0.1745F, 0.0698F, 0.0F);
		lBackTalon02.setTextureOffset(39, 28).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart lFrontTalon02 = new ModelPart(this);
		lFrontTalon02.setPivot(0.6F, 1.0F, -2.5F);
		lFoot.addChild(lFrontTalon02);
		setRotation(lFrontTalon02, 0.0F, -0.0873F, 0.0F);
		lFrontTalon02.setTextureOffset(32, 28).addCuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart lFrontTalon01 = new ModelPart(this);
		lFrontTalon01.setPivot(-0.6F, 1.0F, -2.5F);
		lFoot.addChild(lFrontTalon01);
		setRotation(lFrontTalon01, 0.0F, 0.0873F, 0.0F);
		lFrontTalon01.setTextureOffset(32, 28).addCuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		rFoot = new ModelPart(this);
		rFoot.setPivot(1.8F, 8.9F, -0.4F);
		body.addChild(rFoot);
		setRotation(rFoot, 0.0873F, -0.0873F, 0.0F);
		rFoot.setTextureOffset(32, 22).addCuboid(-1.0F, 0.0F, -3.4F, 2.0F, 2.0F, 4.0F, 0.0F, false);
		
		ModelPart rBackTalon01 = new ModelPart(this);
		rBackTalon01.setPivot(0.5F, 1.2F, 0.2F);
		rFoot.addChild(rBackTalon01);
		setRotation(rBackTalon01, -0.1745F, 0.0698F, 0.0F);
		rBackTalon01.setTextureOffset(39, 28).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart rBackTalon02 = new ModelPart(this);
		rBackTalon02.setPivot(-0.5F, 1.2F, 0.2F);
		rFoot.addChild(rBackTalon02);
		setRotation(rBackTalon02, -0.1745F, -0.0698F, 0.0F);
		rBackTalon02.setTextureOffset(39, 28).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart rFrontTalon02 = new ModelPart(this);
		rFrontTalon02.setPivot(-0.6F, 1.0F, -2.5F);
		rFoot.addChild(rFrontTalon02);
		setRotation(rFrontTalon02, 0.0F, 0.0873F, 0.0F);
		rFrontTalon02.setTextureOffset(32, 28).addCuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart rFrontTalon01 = new ModelPart(this);
		rFrontTalon01.setPivot(0.6F, 1.0F, -2.5F);
		rFoot.addChild(rFrontTalon01);
		setRotation(rFrontTalon01, 0.0F, -0.0873F, 0.0F);
		rFrontTalon01.setTextureOffset(32, 28).addCuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		
		lWing01 = new ModelPart(this);
		lWing01.setPivot(-3.9F, 1.0F, -0.2F);
		body.addChild(lWing01);
		setRotation(lWing01, 0.0F, 0.9599F, -0.8727F);
		lWing01.setTextureOffset(34, 0).addCuboid(-5.0F, -0.7F, -0.5F, 5.0F, 8.0F, 1.0F, 0.0F, true);
		
		lWing02 = new ModelPart(this);
		lWing02.setPivot(-4.5F, 0.0F, 0.1F);
		lWing01.addChild(lWing02);
		setRotation(lWing02, 0.0F, 0.0F, -0.6981F);
		lWing02.setTextureOffset(47, 0).addCuboid(-6.0F, -0.7F, -0.5F, 6.0F, 9.0F, 1.0F, 0.0F, true);
		
		lWing03 = new ModelPart(this);
		lWing03.setPivot(-4.2F, -0.1F, 0.1F);
		lWing02.addChild(lWing03);
		setRotation(lWing03, 0.0F, 0.0F, -0.6109F);
		lWing03.setTextureOffset(32, 10).addCuboid(-6.0F, -0.6F, -0.5F, 6.0F, 10.0F, 1.0F, 0.0F, true);
		
		lWing04 = new ModelPart(this);
		lWing04.setPivot(-2.8F, 0.0F, 0.0F);
		lWing03.addChild(lWing04);
		lWing04.setTextureOffset(47, 26).addCuboid(-8.0F, -0.6F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, true);
		
		rWing01 = new ModelPart(this);
		rWing01.setPivot(3.9F, 1.0F, -0.2F);
		body.addChild(rWing01);
		setRotation(rWing01, 0.0F, -0.9599F, 0.8727F);
		rWing01.setTextureOffset(34, 0).addCuboid(0.0F, -0.7F, -0.5F, 5.0F, 8.0F, 1.0F, 0.0F, false);
		
		rWing02 = new ModelPart(this);
		rWing02.setPivot(4.5F, 0.0F, 0.1F);
		rWing01.addChild(rWing02);
		setRotation(rWing02, 0.0F, 0.0F, 1F);
		rWing02.setTextureOffset(47, 0).addCuboid(0.0F, -0.7F, -0.5F, 6.0F, 9.0F, 1.0F, 0.0F, false);
		
		rWing03 = new ModelPart(this);
		rWing03.setPivot(4.2F, -0.1F, 0.1F);
		rWing02.addChild(rWing03);
		setRotation(rWing03, 0.0F, 0.0F, 0.6109F);
		rWing03.setTextureOffset(32, 10).addCuboid(0.0F, -0.6F, -0.5F, 6.0F, 10.0F, 1.0F, 0.0F, false);
		
		rWing04 = new ModelPart(this);
		rWing04.setPivot(2.8F, 0.0F, 0.0F);
		rWing03.addChild(rWing04);
		rWing04.setTextureOffset(47, 26).addCuboid(1.0F, -0.6F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);
		
		tail01 = new ModelPart(this);
		tail01.setPivot(0.0F, 8.1F, 2.9F);
		body.addChild(tail01);
		setRotation(tail01, -0.5236F, 0.0F, 0.0F);
		tail01.setTextureOffset(49, 10).addCuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart mTailPlume = new ModelPart(this);
		mTailPlume.setPivot(0.0F, -0.5F, 1.9F);
		tail01.addChild(mTailPlume);
		mTailPlume.setTextureOffset(44, 14).addCuboid(-2.5F, -0.4F, -0.9F, 5.0F, 0.0F, 6.0F, 0.0F, false);
		
		ModelPart lTailPlume = new ModelPart(this);
		lTailPlume.setPivot(0.9F, -0.3F, 1.2F);
		tail01.addChild(lTailPlume);
		setRotation(lTailPlume, 0.0F, -0.3142F, -0.1745F);
		lTailPlume.setTextureOffset(44, 20).addCuboid(-4.0F, -0.7F, -0.2F, 4.0F, 0.0F, 6.0F, 0.0F, false);
		
		ModelPart rTailPlume = new ModelPart(this);
		rTailPlume.setPivot(-0.9F, -0.3F, 1.2F);
		tail01.addChild(rTailPlume);
		setRotation(rTailPlume, 0.0F, 0.3142F, 0.1745F);
		rTailPlume.setTextureOffset(44, 20).addCuboid(0.0F, -0.7F, -0.2F, 4.0F, 0.0F, 6.0F, 0.0F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, -0.3F, 0.0F);
		body.addChild(head);
		setRotation(head, -0.0698F, 0.0F, 0.0F);
		head.setTextureOffset(0, 0).addCuboid(-3.5F, -5.5F, -3.7F, 7.0F, 6.0F, 7.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(-2.3F, -4.1F, 0.0F);
		head.addChild(lEar);
		setRotation(lEar, -0.2618F, 0.0F, -0.4014F);
		lEar.setTextureOffset(25, 1).addCuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(2.3F, -4.1F, 0.0F);
		head.addChild(rEar);
		setRotation(rEar, -0.2618F, 0.0F, 0.4014F);
		rEar.setTextureOffset(25, 1).addCuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart beak = new ModelPart(this);
		beak.setPivot(0.0F, -2.0F, -3.3F);
		head.addChild(beak);
		setRotation(beak, -0.0873F, 0.0F, 0.0F);
		beak.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -0.7F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lBeak = new ModelPart(this);
		lBeak.setPivot(-0.6F, 0.1F, 0.0F);
		beak.addChild(lBeak);
		setRotation(lBeak, 0.0F, 0.0F, -0.3491F);
		lBeak.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -0.55F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart rBeak = new ModelPart(this);
		rBeak.setPivot(0.6F, 0.1F, 0.0F);
		beak.addChild(rBeak);
		setRotation(rBeak, 0.0F, 0.0F, 0.3491F);
		rBeak.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -0.55F, 1.0F, 2.0F, 1.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f));
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		if (!entity.isOnGround()) {
			head.pitch -= 1;
			body.pivotY = 19;
			body.pivotZ = -4;
			body.pitch = 1.5f;
			tail01.pitch = -1.5f;
			lWing01.yaw = (float) -(MathHelper.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing01.roll = 0;
			lWing02.yaw = lWing01.yaw / 4;
			lWing02.roll = 0;
			lWing03.yaw = lWing01.yaw / 4;
			lWing03.roll = 0;
			lWing04.visible = true;
			rWing01.yaw = -lWing01.yaw;
			rWing01.roll = 0;
			rWing02.yaw = -lWing02.yaw;
			rWing02.roll = 0;
			rWing03.yaw = -lWing03.yaw;
			rWing03.roll = 0;
			rWing04.visible = true;
			lFoot.pitch = 1;
			rFoot.pitch = 1;
		}
		else {
			body.pivotY = 13.5f;
			body.pivotZ = 0;
			body.pitch = 0;
			tail01.pitch = -0.5236f;
			lWing01.yaw = 0.9599f;
			lWing01.roll = -0.8727f;
			lWing02.yaw = 0;
			lWing02.roll = -0.6981f;
			lWing03.yaw = 0;
			lWing03.roll = -0.6109f;
			lWing04.visible = false;
			rWing01.yaw = -0.9599f;
			rWing01.roll = 0.8727f;
			rWing02.yaw = 0;
			rWing02.roll = 0.6981f;
			rWing03.yaw = 0;
			rWing03.roll = 0.6109f;
			rWing04.visible = false;
			lFoot.pitch = (MathHelper.cos((float) (limbAngle / 4f + Math.PI)) * limbDistance) + 0.0873f;
			rFoot.pitch = (MathHelper.cos(limbAngle / 4f) * limbDistance) + 0.0873f;
		}
		if (entity.isInSittingPose()) {
			lFoot.yaw = 2 / 3f;
		}
		else {
			lFoot.yaw = 0.0873f;
		}
		rFoot.yaw = -lFoot.yaw;
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
