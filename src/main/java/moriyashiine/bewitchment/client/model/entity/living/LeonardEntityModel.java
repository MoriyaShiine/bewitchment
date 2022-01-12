package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
public class LeonardEntityModel<T extends LeonardEntity> extends BipedEntityModel<T> {
	private static final ItemStack SCEPTER = new ItemStack(BWObjects.SCEPTER);

	private final ModelPart realBody;
	private final ModelPart frontLoincloth00;
	private final ModelPart backLoincloth00;
	private final ModelPart realHead;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;

	private boolean realArm = false;

	public LeonardEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		frontLoincloth00 = realBody.getChild("stomach").getChild("frontLoincloth00");
		backLoincloth00 = realBody.getChild("stomach").getChild("backLoincloth00");
		realHead = root.getChild("realHead");
		BipedLeftArm = root.getChild("BipedLeftArm");
		BipedRightArm = root.getChild("BipedRightArm");
		BipedLeftLeg = root.getChild("BipedLeftLeg");
		BipedRightLeg = root.getChild("BipedRightLeg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = data.getRoot();
		ModelPartData realBody = root.addChild("realBody", ModelPartBuilder.create().uv(20, 16).cuboid(-4.5F, 0.0F, -2.5F, 9.0F, 7.0F, 5.0F), ModelTransform.of(0.0F, -9.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		realBody.addChild("lPec", ModelPartBuilder.create().uv(14, 42).cuboid(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 3.0F), ModelTransform.of(2.2F, 3.1F, -0.1F, 0.0F, -0.0873F, 0.0873F));
		realBody.addChild("rPec", ModelPartBuilder.create().uv(14, 42).mirrored(true).cuboid(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 3.0F), ModelTransform.of(-2.2F, 3.1F, -0.1F, 0.0F, 0.0873F, -0.0873F));
		ModelPartData stomach = realBody.addChild("stomach", ModelPartBuilder.create().uv(17, 29).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F), ModelTransform.of(0.0F, 6.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		stomach.addChild("frontLoincloth00", ModelPartBuilder.create().uv(16, 54).cuboid(-4.5F, 0.0F, -0.5F, 9.0F, 8.0F, 2.0F), ModelTransform.of(0.0F, 6.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		stomach.addChild("backLoincloth00", ModelPartBuilder.create().uv(39, 51).cuboid(-4.5F, 0.0F, -1.4F, 9.0F, 8.0F, 3.0F), ModelTransform.of(0.0F, 6.5F, 1.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData realHead = root.addChild("realHead", ModelPartBuilder.create().uv(1, 2).cuboid(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F), ModelTransform.of(0.0F, -9.4F, -0.2F, 0.0F, 0.0F, 0.0F));
		realHead.addChild("snout", ModelPartBuilder.create().uv(29, 2).cuboid(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F), ModelTransform.of(0.0F, -4.6F, -2.5F, 0.5236F, 0.0F, 0.0F));
		realHead.addChild("jawUpper00", ModelPartBuilder.create().uv(43, 0).cuboid(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F), ModelTransform.of(0.0F, -2.5F, -2.2F, 0.0698F, 0.0F, 0.0F));
		ModelPartData jawLower = realHead.addChild("jawLower", ModelPartBuilder.create().uv(30, 10).cuboid(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F), ModelTransform.of(0.0F, -1.0F, -3.0F, -0.0349F, 0.0F, 0.0F));
		jawLower.addChild("beard", ModelPartBuilder.create().uv(0, 53).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F), ModelTransform.of(0.0F, 0.3F, -2.4F, -0.0349F, 0.0F, 0.0F));
		realHead.addChild("lEar", ModelPartBuilder.create().uv(37, 29).mirrored(true).cuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), ModelTransform.of(2.6F, -5.0F, 0.8F, -0.5236F, 0.0F, -0.5236F));
		realHead.addChild("rEar", ModelPartBuilder.create().uv(37, 29).mirrored(true).cuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), ModelTransform.of(-2.6F, -5.0F, 0.8F, -0.5236F, 0.0F, 0.5236F));
		ModelPartData lHorn01 = realHead.addChild("lHorn01", ModelPartBuilder.create().uv(25, 0).mirrored(true).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(2.0F, -6.0F, -0.2F, -0.6109F, 0.0F, 0.5236F));
		ModelPartData lHorn02 = lHorn01.addChild("lHorn02", ModelPartBuilder.create().uv(25, 1).mirrored(true).cuboid(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.3142F, 0.0F, 0.2094F));
		ModelPartData lHorn03 = lHorn02.addChild("lHorn03", ModelPartBuilder.create().uv(0, 1).mirrored(true).cuboid(-0.5F, -2.8F, -0.6F, 1.0F, 3.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, -2.6F, 0.0F, -0.3142F, 0.0F, 0.0F));
		lHorn03.addChild("lHorn04", ModelPartBuilder.create().mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.4014F, 0.0F, 0.0F));
		ModelPartData rHorn01 = realHead.addChild("rHorn01", ModelPartBuilder.create().uv(25, 0).mirrored(true).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(-2.0F, -6.0F, -0.2F, -0.6109F, 0.0F, -0.5236F));
		ModelPartData rHorn02 = rHorn01.addChild("rHorn02", ModelPartBuilder.create().uv(25, 1).mirrored(true).cuboid(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.3142F, 0.0F, -0.2094F));
		ModelPartData rHorn03 = rHorn02.addChild("rHorn03", ModelPartBuilder.create().uv(0, 1).mirrored(true).cuboid(-0.5F, -2.8F, -0.6F, 1.0F, 3.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, -2.6F, 0.0F, -0.3142F, 0.0F, 0.0F));
		rHorn03.addChild("rHorn04", ModelPartBuilder.create().mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.4014F, 0.0F, 0.0F));
		ModelPartData mHorn01 = realHead.addChild("mHorn01", ModelPartBuilder.create().uv(25, 1).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F), ModelTransform.of(0.0F, -6.0F, 0.4F, -0.8727F, 0.0F, 0.0F));
		ModelPartData mHorn02 = mHorn01.addChild("mHorn02", ModelPartBuilder.create().uv(25, 0).cuboid(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -3.7F, 0.0F, -0.4538F, 0.0F, 0.0F));
		ModelPartData mHorn03 = mHorn02.addChild("mHorn03", ModelPartBuilder.create().uv(0, 1).cuboid(-0.5F, -2.8F, -0.2F, 1.0F, 4.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, -3.8F, 0.0F, -0.3142F, 0.0F, 0.0F));
		mHorn03.addChild("mHorn04", ModelPartBuilder.create().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData BipedRightLeg = root.addChild("BipedRightLeg", ModelPartBuilder.create().uv(0, 16).mirrored(true).cuboid(-2.1F, 0.0F, -2.5F, 4.0F, 10.0F, 5.0F), ModelTransform.of(-2.1F, 4.5F, 0.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg02 = BipedRightLeg.addChild("rLeg02", ModelPartBuilder.create().uv(0, 31).mirrored(true).cuboid(-2.0F, -0.1F, -2.0F, 4.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 9.7F, 0.0F, 0.0698F, 0.0F, -0.0349F));
		rLeg02.addChild("rFlipper01", ModelPartBuilder.create().uv(30, 42).mirrored(true).cuboid(-2.0F, 0.0F, -3.0F, 3.0F, 1.0F, 5.0F), ModelTransform.of(-0.3F, 8.8F, -1.7F, 0.0F, 0.1745F, -0.0349F));
		rLeg02.addChild("rFlipper02", ModelPartBuilder.create().uv(28, 48).mirrored(true).cuboid(-0.9F, 0.0F, -3.0F, 2.0F, 1.0F, 5.0F), ModelTransform.of(0.9F, 8.8F, -1.7F, 0.0F, -0.1396F, 0.0349F));
		ModelPartData BipedLeftArm = root.addChild("BipedLeftArm", ModelPartBuilder.create().uv(49, 19).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), ModelTransform.of(5.5F, -7.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		BipedLeftArm.addChild("lClaws", ModelPartBuilder.create().uv(0, 45).cuboid(-1.1F, 0.0F, -2.1F, 2.0F, 3.0F, 4.0F), ModelTransform.of(1.4F, 12.9F, -0.1F, 0.0F, 0.0F, 0.2793F));
		BipedLeftArm.addChild("lShoulderPoof", ModelPartBuilder.create().uv(46, 40).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F), ModelTransform.of(0.4F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0F));
		BipedLeftArm.addChild("lSleeve", ModelPartBuilder.create().uv(46, 9).cuboid(-1.52F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F), ModelTransform.of(0.5F, 9.5F, 2.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData BipedRightArm = root.addChild("BipedRightArm", ModelPartBuilder.create().uv(49, 19).mirrored(true).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), ModelTransform.of(-5.5F, -7.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		BipedRightArm.addChild("rClaws", ModelPartBuilder.create().uv(0, 45).mirrored(true).cuboid(-1.1F, 0.0F, -2.1F, 2.0F, 3.0F, 4.0F), ModelTransform.of(-1.4F, 12.9F, -0.1F, 0.0F, 0.0F, -0.2793F));
		BipedRightArm.addChild("rShoulderPoof", ModelPartBuilder.create().uv(46, 40).mirrored(true).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F), ModelTransform.of(-0.4F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0F));
		BipedRightArm.addChild("rSleeve", ModelPartBuilder.create().uv(46, 9).mirrored(true).cuboid(-1.48F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F), ModelTransform.of(-0.5F, 9.5F, 2.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData BipedLeftLeg = root.addChild("BipedLeftLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.9F, 0.0F, -2.5F, 4.0F, 10.0F, 5.0F), ModelTransform.of(2.1F, 4.5F, 0.1F, -0.0698F, 0.0F, -0.0349F));
		ModelPartData lLeg02 = BipedLeftLeg.addChild("lLeg02", ModelPartBuilder.create().uv(0, 31).cuboid(-2.0F, -0.1F, -2.0F, 4.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 9.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		lLeg02.addChild("lFlipper01", ModelPartBuilder.create().uv(30, 42).cuboid(-1.0F, 0.0F, -3.0F, 3.0F, 1.0F, 5.0F), ModelTransform.of(0.3F, 8.8F, -1.7F, 0.0F, -0.1745F, 0.0349F));
		lLeg02.addChild("lFlipper02", ModelPartBuilder.create().uv(28, 48).cuboid(-1.1F, 0.0F, -3.0F, 2.0F, 1.0F, 5.0F), ModelTransform.of(-0.9F, 8.8F, -1.7F, 0.0F, 0.1396F, -0.0349F));
		return TexturedModelData.of(data, 64, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		entity.setStackInHand(Hand.MAIN_HAND, SCEPTER);
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		copyRotation(realBody, super.body);
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
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? BipedLeftArm : BipedRightArm) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
