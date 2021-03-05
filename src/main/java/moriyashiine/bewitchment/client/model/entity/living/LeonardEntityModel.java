package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
public class LeonardEntityModel<T extends LeonardEntity> extends BipedEntityModel<T> {
	private static final ItemStack SCEPTER = new ItemStack(BWObjects.SCEPTER);
	
	private final ModelPart body;
	private final ModelPart frontLoincloth00;
	private final ModelPart backLoincloth00;
	private final ModelPart head;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	
	private boolean realArm = false;
	
	public LeonardEntityModel() {
		super(1, 0, 64, 64);
		body = new ModelPart(this);
		body.setPivot(0.0F, -9.4F, 0.0F);
		body.setTextureOffset(20, 16).addCuboid(-4.5F, 0.0F, -2.5F, 9.0F, 7.0F, 5.0F, 0.0F, false);
		
		ModelPart lPec = new ModelPart(this);
		lPec.setPivot(2.2F, 3.1F, -0.1F);
		body.addChild(lPec);
		setRotation(lPec, 0.0F, -0.0873F, 0.0873F);
		lPec.setTextureOffset(14, 42).addCuboid(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 3.0F, 0.0F, false);
		
		ModelPart rPec = new ModelPart(this);
		rPec.setPivot(-2.2F, 3.1F, -0.1F);
		body.addChild(rPec);
		setRotation(rPec, 0.0F, 0.0873F, -0.0873F);
		rPec.setTextureOffset(14, 42).addCuboid(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 3.0F, 0.0F, true);
		
		ModelPart stomach = new ModelPart(this);
		stomach.setPivot(0.0F, 6.7F, 0.0F);
		body.addChild(stomach);
		stomach.setTextureOffset(17, 29).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
		
		frontLoincloth00 = new ModelPart(this);
		frontLoincloth00.setPivot(0.0F, 6.5F, -2.0F);
		stomach.addChild(frontLoincloth00);
		frontLoincloth00.setTextureOffset(16, 54).addCuboid(-4.5F, 0.0F, -0.5F, 9.0F, 8.0F, 2.0F, 0.0F, false);
		
		backLoincloth00 = new ModelPart(this);
		backLoincloth00.setPivot(0.0F, 6.5F, 1.1F);
		stomach.addChild(backLoincloth00);
		backLoincloth00.setTextureOffset(39, 51).addCuboid(-4.5F, 0.0F, -1.4F, 9.0F, 8.0F, 3.0F, 0.0F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, -9.4F, -0.2F);
		head.setTextureOffset(1, 2).addCuboid(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -4.6F, -2.5F);
		head.addChild(snout);
		setRotation(snout, 0.5236F, 0.0F, 0.0F);
		snout.setTextureOffset(29, 2).addCuboid(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F, 0.0F, false);
		
		ModelPart jawUpper00 = new ModelPart(this);
		jawUpper00.setPivot(0.0F, -2.5F, -2.2F);
		head.addChild(jawUpper00);
		setRotation(jawUpper00, 0.0698F, 0.0F, 0.0F);
		jawUpper00.setTextureOffset(43, 0).addCuboid(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart jawLower = new ModelPart(this);
		jawLower.setPivot(0.0F, -1.0F, -3.0F);
		head.addChild(jawLower);
		setRotation(jawLower, -0.0349F, 0.0F, 0.0F);
		jawLower.setTextureOffset(30, 10).addCuboid(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		
		ModelPart beard = new ModelPart(this);
		beard.setPivot(0.0F, 0.3F, -2.4F);
		jawLower.addChild(beard);
		setRotation(beard, -0.0349F, 0.0F, 0.0F);
		beard.setTextureOffset(0, 53).addCuboid(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(2.6F, -5.0F, 0.8F);
		head.addChild(lEar);
		setRotation(lEar, -0.5236F, 0.0F, -0.5236F);
		lEar.setTextureOffset(37, 29).addCuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(-2.6F, -5.0F, 0.8F);
		head.addChild(rEar);
		setRotation(rEar, -0.5236F, 0.0F, 0.5236F);
		rEar.setTextureOffset(37, 29).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart lHorn01 = new ModelPart(this);
		lHorn01.setPivot(2.0F, -6.0F, -0.2F);
		head.addChild(lHorn01);
		setRotation(lHorn01, -0.6109F, 0.0F, 0.5236F);
		lHorn01.setTextureOffset(25, 0).addCuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lHorn02 = new ModelPart(this);
		lHorn02.setPivot(0.0F, -2.7F, 0.0F);
		lHorn01.addChild(lHorn02);
		setRotation(lHorn02, -0.3142F, 0.0F, 0.2094F);
		lHorn02.setTextureOffset(25, 1).addCuboid(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lHorn03 = new ModelPart(this);
		lHorn03.setPivot(0.0F, -2.6F, 0.0F);
		lHorn02.addChild(lHorn03);
		setRotation(lHorn03, -0.3142F, 0.0F, 0.0F);
		lHorn03.setTextureOffset(0, 1).addCuboid(-0.5F, -2.8F, -0.6F, 1.0F, 3.0F, 1.0F, 0.2F, true);
		
		ModelPart lHorn04 = new ModelPart(this);
		lHorn04.setPivot(0.0F, -2.7F, 0.0F);
		lHorn03.addChild(lHorn04);
		setRotation(lHorn04, 0.4014F, 0.0F, 0.0F);
		lHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn01 = new ModelPart(this);
		rHorn01.setPivot(-2.0F, -6.0F, -0.2F);
		head.addChild(rHorn01);
		setRotation(rHorn01, -0.6109F, 0.0F, -0.5236F);
		rHorn01.setTextureOffset(25, 0).addCuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn02 = new ModelPart(this);
		rHorn02.setPivot(0.0F, -2.7F, 0.0F);
		rHorn01.addChild(rHorn02);
		setRotation(rHorn02, -0.3142F, 0.0F, -0.2094F);
		rHorn02.setTextureOffset(25, 1).addCuboid(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn03 = new ModelPart(this);
		rHorn03.setPivot(0.0F, -2.6F, 0.0F);
		rHorn02.addChild(rHorn03);
		setRotation(rHorn03, -0.3142F, 0.0F, 0.0F);
		rHorn03.setTextureOffset(0, 1).addCuboid(-0.5F, -2.8F, -0.6F, 1.0F, 3.0F, 1.0F, 0.2F, true);
		
		ModelPart rHorn04 = new ModelPart(this);
		rHorn04.setPivot(0.0F, -2.7F, 0.0F);
		rHorn03.addChild(rHorn04);
		setRotation(rHorn04, 0.4014F, 0.0F, 0.0F);
		rHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart mHorn01 = new ModelPart(this);
		mHorn01.setPivot(0.0F, -6.0F, 0.4F);
		head.addChild(mHorn01);
		setRotation(mHorn01, -0.8727F, 0.0F, 0.0F);
		mHorn01.setTextureOffset(25, 1).addCuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart mHorn02 = new ModelPart(this);
		mHorn02.setPivot(0.0F, -3.7F, 0.0F);
		mHorn01.addChild(mHorn02);
		setRotation(mHorn02, -0.4538F, 0.0F, 0.0F);
		mHorn02.setTextureOffset(25, 0).addCuboid(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart mHorn03 = new ModelPart(this);
		mHorn03.setPivot(0.0F, -3.8F, 0.0F);
		mHorn02.addChild(mHorn03);
		setRotation(mHorn03, -0.3142F, 0.0F, 0.0F);
		mHorn03.setTextureOffset(0, 1).addCuboid(-0.5F, -2.8F, -0.2F, 1.0F, 4.0F, 1.0F, 0.2F, false);
		
		ModelPart mHorn04 = new ModelPart(this);
		mHorn04.setPivot(0.0F, -2.7F, 0.0F);
		mHorn03.addChild(mHorn04);
		setRotation(mHorn04, 0.2618F, 0.0F, 0.0F);
		mHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		BipedLeftArm = new ModelPart(this);
		BipedLeftArm.setPivot(5.5F, -7.6F, 0.0F);
		BipedLeftArm.setTextureOffset(49, 19).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, false);
		
		ModelPart lClaws = new ModelPart(this);
		lClaws.setPivot(1.4F, 12.9F, -0.1F);
		BipedLeftArm.addChild(lClaws);
		setRotation(lClaws, 0.0F, 0.0F, 0.2793F);
		lClaws.setTextureOffset(0, 45).addCuboid(-1.1F, 0.0F, -2.1F, 2.0F, 3.0F, 4.0F, 0.0F, false);
		
		ModelPart lShoulderPoof = new ModelPart(this);
		lShoulderPoof.setPivot(0.4F, 0.0F, -0.1F);
		BipedLeftArm.addChild(lShoulderPoof);
		lShoulderPoof.setTextureOffset(46, 40).addCuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, 0.0F, false);
		
		ModelPart lSleeve = new ModelPart(this);
		lSleeve.setPivot(0.5F, 9.5F, 2.0F);
		BipedLeftArm.addChild(lSleeve);
		lSleeve.setTextureOffset(46, 9).addCuboid(-1.52F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F, 0.0F, false);
		
		BipedRightArm = new ModelPart(this);
		BipedRightArm.setPivot(-5.5F, -7.6F, 0.0F);
		BipedRightArm.setTextureOffset(49, 19).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, true);
		
		ModelPart rClaws = new ModelPart(this);
		rClaws.setPivot(-1.4F, 12.9F, -0.1F);
		BipedRightArm.addChild(rClaws);
		setRotation(rClaws, 0.0F, 0.0F, -0.2793F);
		rClaws.setTextureOffset(0, 45).addCuboid(-1.1F, 0.0F, -2.1F, 2.0F, 3.0F, 4.0F, 0.0F, true);
		
		ModelPart rShoulderPoof = new ModelPart(this);
		rShoulderPoof.setPivot(-0.4F, 0.0F, -0.1F);
		BipedRightArm.addChild(rShoulderPoof);
		rShoulderPoof.setTextureOffset(46, 40).addCuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, 0.0F, true);
		
		ModelPart rSleeve = new ModelPart(this);
		rSleeve.setPivot(-0.5F, 9.5F, 2.0F);
		BipedRightArm.addChild(rSleeve);
		rSleeve.setTextureOffset(46, 9).addCuboid(-1.48F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F, 0.0F, true);
		
		BipedLeftLeg = new ModelPart(this);
		BipedLeftLeg.setPivot(2.1F, 4.5F, 0.1F);
		setRotation(BipedLeftLeg, -0.0698F, 0.0F, -0.0349F);
		BipedLeftLeg.setTextureOffset(0, 16).addCuboid(-1.9F, 0.0F, -2.5F, 4.0F, 10.0F, 5.0F, 0.0F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.0F, 9.7F, 0.0F);
		BipedLeftLeg.addChild(lLeg02);
		lLeg02.setTextureOffset(0, 31).addCuboid(-2.0F, -0.1F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
		
		ModelPart lFlipper01 = new ModelPart(this);
		lFlipper01.setPivot(0.3F, 8.8F, -1.7F);
		lLeg02.addChild(lFlipper01);
		setRotation(lFlipper01, 0.0F, -0.1745F, 0.0349F);
		lFlipper01.setTextureOffset(30, 42).addCuboid(-1.0F, 0.0F, -3.0F, 3.0F, 1.0F, 5.0F, 0.0F, false);
		
		ModelPart lFlipper02 = new ModelPart(this);
		lFlipper02.setPivot(-0.9F, 8.8F, -1.7F);
		lLeg02.addChild(lFlipper02);
		setRotation(lFlipper02, 0.0F, 0.1396F, -0.0349F);
		lFlipper02.setTextureOffset(28, 48).addCuboid(-1.1F, 0.0F, -3.0F, 2.0F, 1.0F, 5.0F, 0.0F, false);
		
		BipedRightLeg = new ModelPart(this);
		BipedRightLeg.setPivot(-2.1F, 4.5F, 0.1F);
		BipedRightLeg.setTextureOffset(0, 16).addCuboid(-2.1F, 0.0F, -2.5F, 4.0F, 10.0F, 5.0F, 0.0F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(0.0F, 9.7F, 0.0F);
		BipedRightLeg.addChild(rLeg02);
		setRotation(rLeg02, 0.0698F, 0.0F, -0.0349F);
		rLeg02.setTextureOffset(0, 31).addCuboid(-2.0F, -0.1F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, true);
		
		ModelPart rFlipper01 = new ModelPart(this);
		rFlipper01.setPivot(-0.3F, 8.8F, -1.7F);
		rLeg02.addChild(rFlipper01);
		setRotation(rFlipper01, 0.0F, 0.1745F, -0.0349F);
		rFlipper01.setTextureOffset(30, 42).addCuboid(-2.0F, 0.0F, -3.0F, 3.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart rFlipper02 = new ModelPart(this);
		rFlipper02.setPivot(0.9F, 8.8F, -1.7F);
		rLeg02.addChild(rFlipper02);
		setRotation(rFlipper02, 0.0F, -0.1396F, 0.0349F);
		rFlipper02.setTextureOffset(28, 48).addCuboid(-0.9F, 0.0F, -3.0F, 2.0F, 1.0F, 5.0F, 0.0F, true);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		entity.setStackInHand(Hand.MAIN_HAND, SCEPTER);
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(head, super.head);
		copyRotation(body, super.torso);
		copyRotation(BipedLeftArm, super.leftArm);
		BipedLeftArm.roll -= 0.1f;
		copyRotation(BipedRightArm, super.rightArm);
		BipedRightArm.roll += 0.1f;
		copyRotation(BipedLeftLeg, super.leftLeg);
		BipedLeftLeg.pitch /= 2;
		BipedLeftLeg.pitch -= 0.0698f;
		BipedLeftLeg.roll -= 0.0349f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.pitch /= 2;
		BipedRightLeg.pitch -= 0.0698f;
		BipedRightLeg.roll += 0.0349f;
		frontLoincloth00.pitch = Math.min(BipedLeftLeg.pitch, BipedRightLeg.pitch) - 0.1745f;
		backLoincloth00.pitch = Math.max(BipedLeftLeg.pitch, BipedRightLeg.pitch);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? BipedLeftArm : BipedRightArm) : super.getArm(arm);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
	
	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
