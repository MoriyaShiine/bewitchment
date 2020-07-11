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
	public ModelPart body;
	public ModelPart neck;
	public ModelPart tail01;
	public ModelPart lLeg01;
	public ModelPart rLeg01;
	public ModelPart tail03;
	public ModelPart bodyFeathers;
	public ModelPart lWing01;
	public ModelPart rWing01;
	public ModelPart neckFeathers01;
	public ModelPart neckFeathers02;
	public ModelPart head;
	public ModelPart beakBottom;
	public ModelPart beakTop;
	public ModelPart tail02;
	public ModelPart tailPlumeM;
	public ModelPart tailPlumeL;
	public ModelPart tailPlumeR;
	public ModelPart lLeg02;
	public ModelPart lFoot;
	public ModelPart rLeg02;
	public ModelPart rFoot;
	public ModelPart lWing02;
	public ModelPart lWing03;
	public ModelPart lWingTip;
	public ModelPart rWing02;
	public ModelPart rWing03;
	public ModelPart rWingTip;
	
	public RavenEntityModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.tail01 = new ModelPart(this, 0, 10);
		this.tail01.setPivot(0.0F, -0.9F, 3.4F);
		this.tail01.addCuboid(-1.5F, -0.5F, 0.0F, 3, 1, 3, 0.0F);
		this.setRotateAngle(tail01, 0.05235987755982988F, 0.0F, 0.0F);
		this.rLeg01 = new ModelPart(this, 34, 0);
		this.rLeg01.mirror = true;
		this.rLeg01.setPivot(-1.2F, 0.1F, 2.3F);
		this.rLeg01.addCuboid(-1.0F, 0.2F, -1.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(rLeg01, 0.3141592653589793F, 0.0F, 0.0F);
		this.rWing03 = new ModelPart(this, 31, 9);
		this.rWing03.mirror = true;
		this.rWing03.setPivot(-3.5F, 0.0F, 0.0F);
		this.rWing03.addCuboid(-4.0F, -0.51F, -1.0F, 4, 1, 7, 0.0F);
		this.setRotateAngle(rWing03, 0.0F, 0.4886921905584123F, 0.0F);
		this.tail03 = new ModelPart(this, 0, 20);
		this.tail03.setPivot(0.0F, 0.4F, 3.5F);
		this.tail03.addCuboid(-1.5F, 0.0F, -0.8F, 3, 1, 3, 0.0F);
		this.setRotateAngle(tail03, 0.20943951023931953F, 0.0F, 0.0F);
		this.rWing02 = new ModelPart(this, 12, 18);
		this.rWing02.mirror = true;
		this.rWing02.setPivot(-3.5F, 0.0F, 0.0F);
		this.rWing02.addCuboid(-4.0F, -0.51F, -1.0F, 4, 1, 7, 0.0F);
		this.setRotateAngle(rWing02, 0.0F, 0.45378560551852565F, 0.0F);
		this.tailPlumeM = new ModelPart(this, 49, 6);
		this.tailPlumeM.setPivot(0.0F, -0.2F, 1.7F);
		this.tailPlumeM.addCuboid(-1.5F, 0.0F, 0.0F, 3, 0, 6, 0.0F);
		this.tail02 = new ModelPart(this, 0, 15);
		this.tail02.setPivot(0.0F, 0.8F, -0.0F);
		this.tail02.addCuboid(-1.51F, -0.5F, -0.4F, 3, 1, 3, 0.0F);
		this.rWing01 = new ModelPart(this, 10, 10);
		this.rWing01.mirror = true;
		this.rWing01.setPivot(-1.9F, -0.3F, -1.1F);
		this.rWing01.addCuboid(-4.0F, -0.5F, -1.0F, 4, 1, 6, 0.0F);
		this.setRotateAngle(rWing01, -0.08726646259971647F, 1.2740903539558606F, 0.0F);
		this.bodyFeathers = new ModelPart(this, 26, 26);
		this.bodyFeathers.setPivot(0.0F, 1.1F, 1.1F);
		this.bodyFeathers.addCuboid(-1.5F, 0.0F, -2.5F, 3, 1, 5, 0.0F);
		this.setRotateAngle(bodyFeathers, 0.13962634015954636F, 0.0F, 0.0F);
		this.neckFeathers02 = new ModelPart(this, 0, 27);
		this.neckFeathers02.setPivot(0.0F, 0.7F, -2.3F);
		this.neckFeathers02.addCuboid(-1.5F, -0.5F, -0.2F, 3, 1, 3, 0.0F);
		this.setRotateAngle(neckFeathers02, -0.8726646259971648F, 0.0F, 0.0F);
		this.neckFeathers01 = new ModelPart(this, 13, 27);
		this.neckFeathers01.setPivot(0.0F, 1.1F, -1.7F);
		this.neckFeathers01.addCuboid(-1.51F, -0.5F, -0.2F, 3, 1, 3, 0.0F);
		this.setRotateAngle(neckFeathers01, -0.40142572795869574F, 0.0F, 0.0F);
		this.body = new ModelPart(this, 0, 0);
		this.body.setPivot(0.0F, 18.2F, -1.6F);
		this.body.addCuboid(-2.0F, -1.5F, -2.5F, 4, 3, 6, 0.0F);
		this.setRotateAngle(body, -0.3141592653589793F, 0.0F, 0.0F);
		this.rFoot = new ModelPart(this, 29, 5);
		this.rFoot.mirror = true;
		this.rFoot.setPivot(0.0F, 2.6F, 0.1F);
		this.rFoot.addCuboid(-0.51F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(rFoot, 0.13962634015954636F, 0.0F, 0.0F);
		this.lWing03 = new ModelPart(this, 31, 9);
		this.lWing03.setPivot(3.5F, 0.0F, 0.0F);
		this.lWing03.addCuboid(0.0F, -0.5F, -1.0F, 4, 1, 7, 0.0F);
		this.setRotateAngle(lWing03, 0.0F, -0.4886921905584123F, 0.0F);
		this.lWing01 = new ModelPart(this, 10, 10);
		this.lWing01.setPivot(1.9F, -0.3F, -1.1F);
		this.lWing01.addCuboid(0.0F, -0.5F, -1.0F, 4, 1, 6, 0.0F);
		this.setRotateAngle(lWing01, -0.08726646259971647F, -1.2740903539558606F, 0.0F);
		this.tailPlumeR = new ModelPart(this, 51, 13);
		this.tailPlumeR.mirror = true;
		this.tailPlumeR.setPivot(-0.5F, 0.7F, 1.2F);
		this.tailPlumeR.addCuboid(-1.5F, -0.7F, -0.2F, 3, 0, 4, 0.0F);
		this.setRotateAngle(tailPlumeR, 0.0F, -0.5235987755982988F, -0.17453292519943295F);
		this.lWing02 = new ModelPart(this, 12, 18);
		this.lWing02.setPivot(3.5F, 0.0F, 0.0F);
		this.lWing02.addCuboid(0.0F, -0.51F, -1.0F, 4, 1, 7, 0.0F);
		this.setRotateAngle(lWing02, 0.0F, -0.45378560551852565F, 0.0F);
		this.rLeg02 = new ModelPart(this, 29, 0);
		this.rLeg02.mirror = true;
		this.rLeg02.setPivot(-0.5F, 2.0F, 0.0F);
		this.rLeg02.addCuboid(-0.49F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		this.setRotateAngle(rLeg02, -0.13962634015954636F, 0.0F, 0.0F);
		this.rWingTip = new ModelPart(this, 29, 18);
		this.rWingTip.mirror = true;
		this.rWingTip.setPivot(-1.8F, 0.4F, 0.0F);
		this.rWingTip.addCuboid(-8.0F, -0.5F, -1.0F, 8, 0, 6, 0.0F);
		this.neck = new ModelPart(this, 16, 0);
		this.neck.setPivot(0.0F, 0.0F, -0.9F);
		this.neck.addCuboid(-1.5F, -1.3F, -3.0F, 3, 2, 3, 0.0F);
		this.setRotateAngle(neck, -0.5235987755982988F, 0.0F, 0.0F);
		this.lWingTip = new ModelPart(this, 29, 18);
		this.lWingTip.setPivot(1.8F, 0.4F, 0.0F);
		this.lWingTip.addCuboid(0.0F, -0.5F, -1.0F, 8, 0, 6, 0.0F);
		this.beakTop = new ModelPart(this, 54, 0);
		this.beakTop.setPivot(0.0F, -0.1F, -2.3F);
		this.beakTop.addCuboid(-0.5F, -0.5F, -2.5F, 1, 1, 3, 0.0F);
		this.setRotateAngle(beakTop, 0.13962634015954636F, 0.0F, 0.0F);
		this.tailPlumeL = new ModelPart(this, 51, 13);
		this.tailPlumeL.setPivot(0.5F, 0.7F, 1.2F);
		this.tailPlumeL.addCuboid(-1.5F, -0.7F, -0.2F, 3, 0, 4, 0.0F);
		this.setRotateAngle(tailPlumeL, 0.0F, 0.5235987755982988F, 0.17453292519943295F);
		this.lLeg02 = new ModelPart(this, 29, 0);
		this.lLeg02.setPivot(0.5F, 2.0F, 0.0F);
		this.lLeg02.addCuboid(-0.51F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
		this.setRotateAngle(lLeg02, -0.13962634015954636F, 0.0F, 0.0F);
		this.head = new ModelPart(this, 40, 0);
		this.head.setPivot(0.0F, -0.3F, -2.4F);
		this.head.addCuboid(-1.51F, -1.5F, -2.7F, 3, 3, 3, 0.0F);
		this.setRotateAngle(head, 0.8726646259971648F, 0.0F, 0.0F);
		this.lFoot = new ModelPart(this, 29, 5);
		this.lFoot.setPivot(0.0F, 2.6F, 0.0F);
		this.lFoot.addCuboid(-0.49F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
		this.setRotateAngle(lFoot, 0.13962634015954636F, 0.0F, 0.0F);
		this.lLeg01 = new ModelPart(this, 34, 0);
		this.lLeg01.setPivot(1.2F, 0.1F, 2.3F);
		this.lLeg01.addCuboid(0.0F, 0.2F, -1.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(lLeg01, 0.3141592653589793F, 0.0F, 0.0F);
		this.beakBottom = new ModelPart(this, 54, 0);
		this.beakBottom.setPivot(0.0F, 0.8F, -1.9F);
		this.beakBottom.addCuboid(-0.51F, -0.5F, -2.8F, 1, 1, 3, 0.0F);
		this.setRotateAngle(beakBottom, -0.08726646259971647F, 0.0F, 0.0F);
		this.body.addChild(this.tail01);
		this.body.addChild(this.rLeg01);
		this.rWing02.addChild(this.rWing03);
		this.body.addChild(this.tail03);
		this.rWing01.addChild(this.rWing02);
		this.tail01.addChild(this.tailPlumeM);
		this.tail01.addChild(this.tail02);
		this.body.addChild(this.rWing01);
		this.body.addChild(this.bodyFeathers);
		this.neck.addChild(this.neckFeathers02);
		this.neck.addChild(this.neckFeathers01);
		this.rLeg02.addChild(this.rFoot);
		this.lWing02.addChild(this.lWing03);
		this.body.addChild(this.lWing01);
		this.tail01.addChild(this.tailPlumeR);
		this.lWing01.addChild(this.lWing02);
		this.rLeg01.addChild(this.rLeg02);
		this.rWing03.addChild(this.rWingTip);
		this.body.addChild(this.neck);
		this.lWing03.addChild(this.lWingTip);
		this.head.addChild(this.beakTop);
		this.tail01.addChild(this.tailPlumeL);
		this.lLeg01.addChild(this.lLeg02);
		this.neck.addChild(this.head);
		this.lLeg02.addChild(this.lFoot);
		this.body.addChild(this.lLeg01);
		this.head.addChild(this.beakBottom);
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
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 1;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		boolean flying = !entity.isOnGround();
		lWing03.visible = lWingTip.visible = rWing03.visible = rWingTip.visible = flying;
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
			body.pitch = -0.3141592653589793f;
			lWing01.yaw = -1.2740903539558606f;
			lWing01.roll = 0;
			lWing02.yaw = -0.45378560551852565f;
			lWing02.roll = 0;
			lWing03.yaw = -0.4886921905584123f;
			lWing03.roll = 0;
			lWingTip.roll = 0;
			rWing01.yaw = 1.2740903539558606f;
			rWing01.roll = 0;
			rWing02.yaw = 0.45378560551852565f;
			rWing02.roll = 0;
			rWing03.yaw = 0.4886921905584123f;
			rWing03.roll = 0;
			rWingTip.roll = 0;
			lLeg01.pitch = (MathHelper.cos((float) (limbAngle + Math.PI)) * limbDistance) + 0.3141592653589793f;
			rLeg01.pitch = (MathHelper.cos(limbAngle) * limbDistance) + 0.3141592653589793f;
		}
		boolean sitting = entity.isSitting();
		lLeg01.visible = rLeg01.visible = !sitting;
		if (sitting) {
			body.pivotY = 21.5f;
		}
		else {
			body.pivotY = 18.2f;
		}
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