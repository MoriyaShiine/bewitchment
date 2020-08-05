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
	public ModelPart body;
	public ModelPart head;
	public ModelPart lFoot;
	public ModelPart rFoot;
	public ModelPart leftWing00;
	public ModelPart rightWing00;
	public ModelPart tail01;
	public ModelPart lEar;
	public ModelPart rEar;
	public ModelPart beak;
	public ModelPart beakL;
	public ModelPart beakR;
	public ModelPart lBTalon00;
	public ModelPart lFTalon00;
	public ModelPart lFTalon01;
	public ModelPart lFTalon00_1;
	public ModelPart rBTalon00;
	public ModelPart rFTalon00;
	public ModelPart rFTalon01;
	public ModelPart rFTalon00_1;
	public ModelPart leftWing01;
	public ModelPart leftWing02;
	public ModelPart rightWing01;
	public ModelPart rightWing02;
	public ModelPart tailPlumeM;
	public ModelPart tailPlumeL;
	public ModelPart tailPlumeR;
	
	public OwlEntityModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.lFTalon00_1 = new ModelPart(this, 32, 28);
		this.lFTalon00_1.setPivot(0.6F, 1.0F, -2.5F);
		this.lFTalon00_1.addCuboid(-0.5F, -1.0F, -2.5F, 1, 2, 2, 0.0F);
		this.setRotateAngle(lFTalon00_1, 0.0F, -0.08726646259971647F, 0.0F);
		this.head = new ModelPart(this, 0, 0);
		this.head.setPivot(0.0F, -0.3F, 0.0F);
		this.head.addCuboid(-3.5F, -5.5F, -3.7F, 7, 6, 7, 0.0F);
		this.setRotateAngle(head, -0.06981317007977318F, 0.0F, 0.0F);
		this.lFTalon00 = new ModelPart(this, 39, 28);
		this.lFTalon00.setPivot(-0.5F, 1.2F, 0.2F);
		this.lFTalon00.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(lFTalon00, -0.17453292519943295F, -0.06981317007977318F, 0.0F);
		this.leftWing01 = new ModelPart(this, 47, 0);
		this.leftWing01.setPivot(4.5F, 0.0F, 0.1F);
		this.leftWing01.addCuboid(0.0F, -0.7F, -0.5F, 7, 9, 1, 0.0F);
		this.setRotateAngle(leftWing01, 0.0F, 0.0F, 0.6981317007977318F);
		this.rFTalon00 = new ModelPart(this, 39, 28);
		this.rFTalon00.mirror = true;
		this.rFTalon00.setPivot(0.5F, 1.2F, 0.2F);
		this.rFTalon00.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(rFTalon00, -0.17453292519943295F, 0.06981317007977318F, 0.0F);
		this.tailPlumeR = new ModelPart(this, 44, 23);
		this.tailPlumeR.mirror = true;
		this.tailPlumeR.setPivot(0.9F, -0.3F, 1.2F);
		this.tailPlumeR.addCuboid(-4.0F, -0.7F, -0.2F, 4, 0, 6, 0.0F);
		this.setRotateAngle(tailPlumeR, 0.0F, -0.3141592653589793F, -0.17453292519943295F);
		this.rightWing01 = new ModelPart(this, 47, 0);
		this.rightWing01.mirror = true;
		this.rightWing01.setPivot(-4.5F, 0.0F, 0.1F);
		this.rightWing01.addCuboid(-7.0F, -0.7F, -0.5F, 7, 9, 1, 0.0F);
		this.setRotateAngle(rightWing01, 0.0F, 0.0F, -0.6981317007977318F);
		this.leftWing00 = new ModelPart(this, 34, 0);
		this.leftWing00.setPivot(3.9F, 1.0F, -0.2F);
		this.leftWing00.addCuboid(0.0F, -0.7F, -0.5F, 5, 8, 1, 0.0F);
		this.setRotateAngle(leftWing00, 0.0F, -0.9599310885968813F, 0.8726646259971648F);
		this.lFTalon01 = new ModelPart(this, 32, 28);
		this.lFTalon01.setPivot(-0.6F, 1.0F, -2.5F);
		this.lFTalon01.addCuboid(-0.5F, -1.0F, -2.5F, 1, 2, 2, 0.0F);
		this.setRotateAngle(lFTalon01, 0.0F, 0.08726646259971647F, 0.0F);
		this.rightWing02 = new ModelPart(this, 32, 10);
		this.rightWing02.mirror = true;
		this.rightWing02.setPivot(-5.2F, -0.1F, 0.1F);
		this.rightWing02.addCuboid(-7.0F, -0.6F, -0.5F, 7, 10, 1, 0.0F);
		this.setRotateAngle(rightWing02, 0.0F, 0.0F, -0.6108652381980153F);
		this.leftWing02 = new ModelPart(this, 32, 10);
		this.leftWing02.setPivot(5.2F, -0.1F, 0.1F);
		this.leftWing02.addCuboid(0.0F, -0.6F, -0.5F, 7, 10, 1, 0.0F);
		this.setRotateAngle(leftWing02, 0.0F, 0.0F, 0.6108652381980153F);
		this.body = new ModelPart(this, 0, 14);
		this.body.setPivot(0.0F, 13.5F, 0.0F);
		this.body.addCuboid(-4.0F, 0.0F, -3.5F, 8, 9, 7, 0.0F);
		this.rFTalon00_1 = new ModelPart(this, 32, 28);
		this.rFTalon00_1.mirror = true;
		this.rFTalon00_1.setPivot(-0.6F, 1.0F, -2.5F);
		this.rFTalon00_1.addCuboid(-0.5F, -1.0F, -2.5F, 1, 2, 2, 0.0F);
		this.setRotateAngle(rFTalon00_1, 0.0F, 0.08726646259971647F, 0.0F);
		this.beakL = new ModelPart(this, 0, 0);
		this.beakL.setPivot(0.6F, 0.1F, 0.0F);
		this.beakL.addCuboid(-0.5F, -0.5F, -0.55F, 1, 2, 1, 0.0F);
		this.setRotateAngle(beakL, 0.0F, 0.0F, 0.3490658503988659F);
		this.rightWing00 = new ModelPart(this, 34, 0);
		this.rightWing00.mirror = true;
		this.rightWing00.setPivot(-3.9F, 1.0F, -0.2F);
		this.rightWing00.addCuboid(-5.0F, -0.7F, -0.5F, 5, 8, 1, 0.0F);
		this.setRotateAngle(rightWing00, 0.0F, 0.9599310885968813F, -0.8726646259971648F);
		this.rBTalon00 = new ModelPart(this, 39, 28);
		this.rBTalon00.mirror = true;
		this.rBTalon00.setPivot(-0.5F, 1.2F, 0.2F);
		this.rBTalon00.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(rBTalon00, -0.17453292519943295F, -0.06981317007977318F, 0.0F);
		this.lBTalon00 = new ModelPart(this, 39, 28);
		this.lBTalon00.setPivot(0.5F, 1.2F, 0.2F);
		this.lBTalon00.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(lBTalon00, -0.17453292519943295F, 0.06981317007977318F, 0.0F);
		this.tail01 = new ModelPart(this, 49, 10);
		this.tail01.setPivot(0.0F, 8.1F, 2.9F);
		this.tail01.addCuboid(-1.5F, -1.0F, 0.0F, 3, 1, 3, 0.0F);
		this.setRotateAngle(tail01, -0.5235987755982988F, 0.0F, 0.0F);
		this.rEar = new ModelPart(this, 25, 1);
		this.rEar.mirror = true;
		this.rEar.setPivot(-2.3F, -4.1F, 0.0F);
		this.rEar.addCuboid(-1.0F, -4.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(rEar, -0.2617993877991494F, 0.0F, -0.40142572795869574F);
		this.tailPlumeL = new ModelPart(this, 44, 23);
		this.tailPlumeL.setPivot(-0.9F, -0.3F, 1.2F);
		this.tailPlumeL.addCuboid(0.0F, -0.7F, -0.2F, 4, 0, 6, 0.0F);
		this.setRotateAngle(tailPlumeL, 0.0F, 0.3141592653589793F, 0.17453292519943295F);
		this.beak = new ModelPart(this, 0, 0);
		this.beak.setPivot(0.0F, -2.0F, -3.3F);
		this.beak.addCuboid(-0.5F, -0.5F, -0.7F, 1, 3, 1, 0.0F);
		this.setRotateAngle(beak, -0.08726646259971647F, 0.0F, 0.0F);
		this.tailPlumeM = new ModelPart(this, 44, 15);
		this.tailPlumeM.mirror = true;
		this.tailPlumeM.setPivot(0.0F, -0.5F, 1.9F);
		this.tailPlumeM.addCuboid(-2.5F, -0.4F, -0.9F, 5, 0, 6, 0.0F);
		this.rFoot = new ModelPart(this, 32, 22);
		this.rFoot.mirror = true;
		this.rFoot.setPivot(-1.8F, 8.9F, -0.4F);
		this.rFoot.addCuboid(-1.0F, 0.0F, -3.4F, 2, 2, 4, 0.0F);
		this.setRotateAngle(rFoot, 0.08726646259971647F, 0.08726646259971647F, 0.0F);
		this.lFoot = new ModelPart(this, 32, 22);
		this.lFoot.setPivot(1.8F, 8.9F, -0.4F);
		this.lFoot.addCuboid(-1.0F, 0.0F, -3.4F, 2, 2, 4, 0.0F);
		this.setRotateAngle(lFoot, 0.08726646259971647F, -0.08726646259971647F, 0.0F);
		this.lEar = new ModelPart(this, 25, 1);
		this.lEar.setPivot(2.3F, -4.1F, 0.0F);
		this.lEar.addCuboid(-1.0F, -4.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(lEar, -0.2617993877991494F, 0.0F, 0.40142572795869574F);
		this.rFTalon01 = new ModelPart(this, 32, 28);
		this.rFTalon01.mirror = true;
		this.rFTalon01.setPivot(0.6F, 1.0F, -2.5F);
		this.rFTalon01.addCuboid(-0.5F, -1.0F, -2.5F, 1, 2, 2, 0.0F);
		this.setRotateAngle(rFTalon01, 0.0F, -0.08726646259971647F, 0.0F);
		this.beakR = new ModelPart(this, 0, 0);
		this.beakR.mirror = true;
		this.beakR.setPivot(-0.6F, 0.1F, 0.0F);
		this.beakR.addCuboid(-0.5F, -0.5F, -0.55F, 1, 2, 1, 0.0F);
		this.setRotateAngle(beakR, 0.0F, 0.0F, -0.3490658503988659F);
		this.lFoot.addChild(this.lFTalon00_1);
		this.body.addChild(this.head);
		this.lFoot.addChild(this.lFTalon00);
		this.leftWing00.addChild(this.leftWing01);
		this.rFoot.addChild(this.rFTalon00);
		this.tail01.addChild(this.tailPlumeR);
		this.rightWing00.addChild(this.rightWing01);
		this.body.addChild(this.leftWing00);
		this.lFoot.addChild(this.lFTalon01);
		this.rightWing01.addChild(this.rightWing02);
		this.leftWing01.addChild(this.leftWing02);
		this.rFoot.addChild(this.rFTalon00_1);
		this.beak.addChild(this.beakL);
		this.body.addChild(this.rightWing00);
		this.rFoot.addChild(this.rBTalon00);
		this.lFoot.addChild(this.lBTalon00);
		this.body.addChild(this.tail01);
		this.head.addChild(this.rEar);
		this.tail01.addChild(this.tailPlumeL);
		this.head.addChild(this.beak);
		this.tail01.addChild(this.tailPlumeM);
		this.body.addChild(this.rFoot);
		this.body.addChild(this.lFoot);
		this.head.addChild(this.lEar);
		this.rFoot.addChild(this.rFTalon01);
		this.beak.addChild(this.beakR);
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
			leftWing00.yaw = (float) -(MathHelper.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			leftWing00.roll = 0;
			leftWing01.yaw = leftWing00.yaw / 4;
			leftWing01.roll = 0;
			leftWing02.yaw = leftWing00.yaw / 4;
			leftWing02.roll = 0;
			rightWing00.pitch = leftWing00.pitch;
			rightWing00.yaw = -leftWing00.yaw;
			rightWing00.roll = 0;
			rightWing01.yaw = -leftWing01.yaw;
			rightWing01.roll = 0;
			rightWing02.yaw = -leftWing02.yaw;
			rightWing02.roll = 0;
			lFoot.pitch = 1;
			rFoot.pitch = 1;
		}
		else {
			body.pivotY = 13.5f;
			body.pivotZ = 0;
			body.pitch = 0;
			tail01.pitch = -0.5235987755982988f;
			leftWing00.yaw = -1;
			leftWing00.roll = 1;
			leftWing01.yaw = 0;
			leftWing01.roll = 0.6981317007977318f;
			leftWing02.yaw = 0;
			leftWing02.roll = 0.6108652381980153f;
			rightWing00.pitch = 0;
			rightWing00.yaw = 1;
			rightWing00.roll = -1;
			rightWing01.yaw = 0;
			rightWing01.roll = -0.6981317007977318f;
			rightWing02.yaw = 0;
			rightWing02.roll = -0.6108652381980153f;
			lFoot.pitch = (MathHelper.cos((float) (limbAngle / 4f + Math.PI)) * limbDistance) + 0.08726646259971647f;
			rFoot.pitch = (MathHelper.cos(limbAngle / 4f) * limbDistance) + 0.08726646259971647f;
		}
		if (entity.isSitting()) {
			lFoot.yaw = -2 / 3f;
		}
		else {
			lFoot.yaw = -0.08726646259971647f;
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
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
