package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;

@Environment(EnvType.CLIENT)
public class ToadEntityModel<T extends ToadEntity> extends AnimalModel<T> {
	public ModelPart body;
	public ModelPart lLeg00;
	public ModelPart rLeg00;
	public ModelPart lArm00;
	public ModelPart rArm00;
	public ModelPart head;
	public ModelPart lLeg01;
	public ModelPart lFoot;
	public ModelPart rLeg01;
	public ModelPart rFoot;
	public ModelPart lArm01;
	public ModelPart lHand;
	public ModelPart rArm01;
	public ModelPart rHand;
	public ModelPart throat;
	public ModelPart lowerJaw;
	
	public ToadEntityModel() {
		this.textureWidth = 32;
		this.textureHeight = 32;
		this.body = new ModelPart(this, 0, 0);
		this.body.setPivot(0.0F, 19.8F, 0.7F);
		this.body.addCuboid(-3.5F, -2.0F, -3.5F, 7, 4, 8, 0.0F);
		this.setRotateAngle(body, -0.40142572795869574F, 0.0F, 0.0F);
		this.rLeg01 = new ModelPart(this, 0, 21);
		this.rLeg01.mirror = true;
		this.rLeg01.setPivot(-1.1F, 3.2F, 0.6F);
		this.rLeg01.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 4, 0.0F);
		this.setRotateAngle(rLeg01, 0.8726646259971648F, 0.0F, 0.0F);
		this.lLeg00 = new ModelPart(this, 0, 13);
		this.lLeg00.setPivot(2.3F, -1.2F, 3.3F);
		this.lLeg00.addCuboid(0.0F, -1.0F, -1.6F, 2, 5, 3, 0.0F);
		this.setRotateAngle(lLeg00, -0.7853981633974483F, -0.3490658503988659F, -0.13962634015954636F);
		this.lArm01 = new ModelPart(this, 11, 18);
		this.lArm01.setPivot(1.3F, 1.4F, 0.3F);
		this.lArm01.addCuboid(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
		this.setRotateAngle(lArm01, -0.3490658503988659F, 0.0F, 0.2617993877991494F);
		this.lowerJaw = new ModelPart(this, 13, 28);
		this.lowerJaw.setPivot(0.0F, 0.0F, -2.5F);
		this.lowerJaw.addCuboid(-1.9F, -0.5F, -2.0F, 4, 1, 3, 0.0F);
		this.lHand = new ModelPart(this, -2, 0);
		this.lHand.setPivot(0.0F, 3.8F, -0.3F);
		this.lHand.addCuboid(-1.5F, 0.0F, -2.7F, 3, 0, 4, 0.0F);
		this.setRotateAngle(lHand, 0.45378560551852565F, -0.17453292519943295F, 0.0F);
		this.throat = new ModelPart(this, 18, 14);
		this.throat.setPivot(0.0F, 0.9F, -0.4F);
		this.throat.addCuboid(-2.51F, -0.5F, -1.5F, 5, 1, 2, 0.0F);
		this.rArm00 = new ModelPart(this, 10, 13);
		this.rArm00.mirror = true;
		this.rArm00.setPivot(-1.9F, 0.5F, -2.0F);
		this.rArm00.addCuboid(-2.0F, -1.0F, -1.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(rArm00, 0.3490658503988659F, 0.0F, 0.3490658503988659F);
		this.head = new ModelPart(this, 12, 20);
		this.head.setPivot(0.0F, -0.9F, -2.9F);
		this.head.addCuboid(-2.5F, -1.5F, -5.0F, 5, 2, 5, 0.0F);
		this.setRotateAngle(head, 0.40142572795869574F, 0.0F, 0.0F);
		this.lLeg01 = new ModelPart(this, 0, 21);
		this.lLeg01.setPivot(1.1F, 3.2F, 0.6F);
		this.lLeg01.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 4, 0.0F);
		this.setRotateAngle(lLeg01, 0.8726646259971648F, 0.0F, 0.0F);
		this.lFoot = new ModelPart(this, 0, 27);
		this.lFoot.setPivot(0.0F, 1.3F, 3.4F);
		this.lFoot.addCuboid(-1.0F, -0.5F, -3.3F, 2, 1, 4, 0.0F);
		this.setRotateAngle(lFoot, 0.3141592653589793F, 0.0F, 0.0F);
		this.rFoot = new ModelPart(this, 0, 27);
		this.rFoot.mirror = true;
		this.rFoot.setPivot(0.0F, 1.3F, 3.4F);
		this.rFoot.addCuboid(-1.0F, -0.5F, -3.3F, 2, 1, 4, 0.0F);
		this.setRotateAngle(rFoot, 0.3141592653589793F, 0.0F, 0.0F);
		this.lArm00 = new ModelPart(this, 10, 13);
		this.lArm00.setPivot(1.9F, 0.5F, -2.0F);
		this.lArm00.addCuboid(0.0F, -1.0F, -1.0F, 2, 3, 2, 0.0F);
		this.setRotateAngle(lArm00, 0.3490658503988659F, 0.0F, -0.3490658503988659F);
		this.rHand = new ModelPart(this, -2, 0);
		this.rHand.mirror = true;
		this.rHand.setPivot(0.0F, 3.8F, -0.3F);
		this.rHand.addCuboid(-1.5F, 0.0F, -2.7F, 3, 0, 4, 0.0F);
		this.setRotateAngle(rHand, 0.45378560551852565F, 0.17453292519943295F, 0.0F);
		this.rArm01 = new ModelPart(this, 11, 18);
		this.rArm01.mirror = true;
		this.rArm01.setPivot(-1.3F, 1.4F, 0.3F);
		this.rArm01.addCuboid(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
		this.setRotateAngle(rArm01, -0.3490658503988659F, 0.0F, -0.2617993877991494F);
		this.rLeg00 = new ModelPart(this, 0, 13);
		this.rLeg00.mirror = true;
		this.rLeg00.setPivot(-2.3F, -1.2F, 3.3F);
		this.rLeg00.addCuboid(-2.0F, -1.0F, -1.6F, 2, 5, 3, 0.0F);
		this.setRotateAngle(rLeg00, -0.7853981633974483F, 0.3490658503988659F, 0.13962634015954636F);
		this.rLeg00.addChild(this.rLeg01);
		this.body.addChild(this.lLeg00);
		this.lArm00.addChild(this.lArm01);
		this.throat.addChild(this.lowerJaw);
		this.lArm01.addChild(this.lHand);
		this.head.addChild(this.throat);
		this.body.addChild(this.rArm00);
		this.body.addChild(this.head);
		this.lLeg00.addChild(this.lLeg01);
		this.lLeg01.addChild(this.lFoot);
		this.rLeg01.addChild(this.rFoot);
		this.body.addChild(this.lArm00);
		this.rArm01.addChild(this.rHand);
		this.rArm00.addChild(this.rArm01);
		this.body.addChild(this.rLeg00);
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 0.5f;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 1 / 3f;
		if (!entity.isOnGround() || entity.isSitting()) {
			head.pitch -= 1 / 3f;
			body.pitch = 0;
			lArm00.pitch = -2 / 3f;
			lHand.pitch = 1.5f;
			lLeg00.pitch = -1 / 3f;
			lLeg00.yaw = 0;
			if (!entity.isOnGround()) {
				lLeg00.pitch = 1;
				lLeg01.pitch = -0.5f;
				lFoot.pitch = 1.5f;
			}
		}
		else {
			body.pitch = -0.40142572795869574f;
			lArm00.pitch = 0.3490658503988659f;
			lHand.pitch = 0.45378560551852565f;
			lLeg00.pitch = -0.7853981633974483f;
			lLeg00.yaw = -0.3490658503988659f;
			lLeg01.pitch = 0.8726646259971648f;
			lFoot.pitch = 0.3141592653589793f;
		}
		rArm00.pitch = lArm00.pitch;
		rHand.pitch = lHand.pitch;
		rLeg00.pitch = lLeg00.pitch;
		rLeg00.yaw = -lLeg00.yaw;
		rLeg01.pitch = lLeg01.pitch;
		rFoot.pitch = lFoot.pitch;
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
