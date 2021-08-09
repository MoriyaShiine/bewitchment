package moriyashiine.bewitchment.client.model.equipment.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class WitchArmorModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public final ModelPart hat1;
	public final ModelPart hood01;
	public final ModelPart lowerLeftSkirt;
	public final ModelPart armorLeftBoot;
	public final ModelPart lowerRightSkirt;
	public final ModelPart armorRightBoot;
	
	public WitchArmorModel(ModelPart root) {
		super(root, RenderLayer::getArmorCutoutNoCull);
		child = false;
		hat1 = head.getChild("armorHead").getChild("hat1");
		hood01 = head.getChild("armorHead").getChild("hood01");
		lowerLeftSkirt = leftLeg.getChild("armorLeftLeg").getChild("lowerLeftSkirt");
		armorLeftBoot = leftLeg.getChild("armorLeftBoot");
		lowerRightSkirt = rightLeg.getChild("armorRightLeg").getChild("lowerRightSkirt");
		armorRightBoot = rightLeg.getChild("armorRightBoot");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData head = data.getRoot().getChild(EntityModelPartNames.HEAD);
		ModelPartData body = data.getRoot().getChild(EntityModelPartNames.BODY);
		ModelPartData rightLeg = data.getRoot().getChild(EntityModelPartNames.RIGHT_LEG);
		ModelPartData leftLeg = data.getRoot().getChild(EntityModelPartNames.LEFT_LEG);
		ModelPartData rightArm = data.getRoot().getChild(EntityModelPartNames.RIGHT_ARM);
		ModelPartData leftArm = data.getRoot().getChild(EntityModelPartNames.LEFT_ARM);

		ModelPartData armorRightLeg = rightLeg.addChild("armorRightLeg", ModelPartBuilder.create(), ModelTransform.of(3.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightLeg.addChild("tunicRightFront", ModelPartBuilder.create().uv(53, 65).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F), ModelTransform.of(-2.01F, 0.0F, -2.1F, -0.1222F, 0.0F, 0.0F));
		armorRightLeg.addChild("tunicRight", ModelPartBuilder.create().uv(24, 65).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F), ModelTransform.of(-6.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		ModelPartData tunicRightBack = armorRightLeg.addChild("tunicRightBack", ModelPartBuilder.create().uv(53, 65).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F), ModelTransform.of(-2.01F, 0.0F, 2.1F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack.addChild("tunicRightBack2", ModelPartBuilder.create(), ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData lowerRightSkirt = armorRightLeg.addChild("lowerRightSkirt", ModelPartBuilder.create(), ModelTransform.of(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLowerFront2 = lowerRightSkirt.addChild("tunicLowerFront2", ModelPartBuilder.create().uv(53, 71).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F), ModelTransform.of(1.99F, 0.0F, -2.1F, -0.1222F, 0.0F, 0.0F));
		tunicLowerFront2.addChild("tunicRightFront4", ModelPartBuilder.create().uv(53, 77).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F), ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRight3 = lowerRightSkirt.addChild("tunicRight3", ModelPartBuilder.create().uv(24, 72).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F), ModelTransform.of(-2.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		tunicRight3.addChild("tunicRight4", ModelPartBuilder.create().uv(23, 78).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F), ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData tunicRightBack3 = lowerRightSkirt.addChild("tunicRightBack3", ModelPartBuilder.create().uv(53, 72).cuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F), ModelTransform.of(1.99F, 0.0F, 2.1F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack3.addChild("tunicRightBack4", ModelPartBuilder.create().uv(53, 77).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F), ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		body.addChild("armorBody", ModelPartBuilder.create().uv(0, 81).cuboid(-4.5F, -0.01F, -2.5F, 9.0F, 12.0F, 5.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData armorLeftLeg = leftLeg.addChild("armorLeftLeg", ModelPartBuilder.create(), ModelTransform.of(-3.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLeftFront = armorLeftLeg.addChild("tunicLeftFront", ModelPartBuilder.create().uv(53, 65).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F), ModelTransform.of(1.99F, 0.0F, -2.1F, -3.0194F, 0.0F, 3.1416F));
		tunicLeftFront.addChild("tunicFront", ModelPartBuilder.create(), ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeft = armorLeftLeg.addChild("tunicLeft", ModelPartBuilder.create().uv(23, 65).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F), ModelTransform.of(6.01F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLeft.addChild("tunicLeft2", ModelPartBuilder.create(), ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeftBack = armorLeftLeg.addChild("tunicLeftBack", ModelPartBuilder.create().uv(53, 65).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F), ModelTransform.of(1.99F, 0.0F, 2.1F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack.addChild("tunicLeftBack2", ModelPartBuilder.create(), ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData lowerLeftSkirt = armorLeftLeg.addChild("lowerLeftSkirt", ModelPartBuilder.create(), ModelTransform.of(11.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tunicLowerFront = lowerLeftSkirt.addChild("tunicLowerFront", ModelPartBuilder.create().uv(53, 72).cuboid(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F), ModelTransform.of(-6.0F, 10.0F, -3.1F, -3.0194F, 0.0F, -3.1416F));
		tunicLowerFront.addChild("tunicFrontFringe_r1", ModelPartBuilder.create().uv(53, 77).cuboid(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F), ModelTransform.of(1.0F, -1.0F, -0.4F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLowerLeft = lowerLeftSkirt.addChild("tunicLowerLeft", ModelPartBuilder.create().uv(23, 72).cuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F), ModelTransform.of(-4.99F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLowerLeft.addChild("tunicLeft4", ModelPartBuilder.create().uv(23, 78).cuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F), ModelTransform.of(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData tunicLeftBack3 = lowerLeftSkirt.addChild("tunicLeftBack3", ModelPartBuilder.create().uv(53, 72).cuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F), ModelTransform.of(-9.01F, 0.0F, 2.1F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack3.addChild("tunicLeftBack4", ModelPartBuilder.create().uv(53, 77).cuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F), ModelTransform.of(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		ModelPartData armorHead = head.addChild("armorHead", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData hat1 = armorHead.addChild("hat1", ModelPartBuilder.create().uv(49, 111).cuboid(-4.5F, -9.5F, -4.5F, 9.0F, 4.0F, 9.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData hat2 = hat1.addChild("hat2", ModelPartBuilder.create().uv(89, 114).cuboid(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 6.0F), ModelTransform.of(0.0F, -9.5F, -3.0F, -0.2618F, 0.0F, 0.0F));
		hat2.addChild("hat3", ModelPartBuilder.create().uv(110, 112).cuboid(0.0F, -4.0F, 0.0F, 3.0F, 4.0F, 3.0F), ModelTransform.of(-1.4F, -4.0F, 1.5F, -0.3491F, 0.0F, 0.0F));
		hat1.addChild("hatWing", ModelPartBuilder.create().uv(0, 112).cuboid(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 12.0F), ModelTransform.of(0.0F, -6.0F, 0.0F, 0.0349F, 0.0F, 0.0698F));
		ModelPartData hood01 = armorHead.addChild("hood01", ModelPartBuilder.create().uv(70, 0).cuboid(-4.5F, -8.6F, -4.7F, 9.0F, 9.0F, 10.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		hood01.addChild("hoodFringe01", ModelPartBuilder.create().uv(66, 0).cuboid(-3.0F, -8.9F, -5.4F, 6.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F));
		hood01.addChild("hoodFringeL01", ModelPartBuilder.create().uv(77, 24).mirrored(true).cuboid(0.0F, -0.3F, -5.39F, 2.0F, 8.0F, 10.0F), ModelTransform.of(-4.1F, -8.2F, 0.1F, 0.0F, 0.0F, 0.2793F));
		hood01.addChild("hoodFringeR01", ModelPartBuilder.create().uv(77, 24).cuboid(-2.0F, -0.3F, -5.39F, 2.0F, 8.0F, 10.0F), ModelTransform.of(4.1F, -8.2F, 0.1F, 0.0F, 0.0F, -0.2793F));
		hood01.addChild("hoodFringeL02", ModelPartBuilder.create().uv(97, 33).cuboid(0.0F, -0.9F, -5.37F, 5.0F, 2.0F, 10.0F), ModelTransform.of(0.85F, 0.55F, 0.1F, 0.0F, 0.0F, -0.3142F));
		hood01.addChild("hoodFringeR02", ModelPartBuilder.create().uv(97, 33).mirrored(true).cuboid(-5.0F, -0.9F, -5.37F, 5.0F, 2.0F, 10.0F), ModelTransform.of(-0.85F, 0.55F, 0.1F, 0.0F, 0.0F, 0.3142F));
		hood01.addChild("hoodShade", ModelPartBuilder.create().uv(105, 0).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F), ModelTransform.of(0.0F, -7.3F, -5.2F, 0.0524F, 0.0F, 0.0F));
		hood01.addChild("hoodFringeL04", ModelPartBuilder.create().uv(57, 20).cuboid(-0.81F, -1.0F, -5.37F, 4.0F, 2.0F, 11.0F), ModelTransform.of(0.3F, -8.9F, 0.1F, 0.0F, 0.0F, 0.3142F));
		hood01.addChild("hoodFringeR04", ModelPartBuilder.create().uv(57, 20).mirrored(true).cuboid(-3.0F, -1.0F, -5.38F, 4.0F, 2.0F, 11.0F), ModelTransform.of(-0.8F, -8.9F, 0.1F, 0.0F, 0.0F, -0.3142F));
		ModelPartData hoodPipe01 = hood01.addChild("hoodPipe01", ModelPartBuilder.create().uv(65, 43).cuboid(-3.0F, -2.5F, 0.0F, 6.0F, 6.0F, 4.0F), ModelTransform.of(0.0F, -7.0F, 4.2F, -0.4014F, 0.0F, 0.0F));
		hoodPipe01.addChild("hoodPipe02", ModelPartBuilder.create().uv(65, 54).cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F), ModelTransform.of(0.0F, -0.3F, 3.1F, -0.4538F, 0.0F, 0.0F));
		ModelPartData armorLeftBoot = leftLeg.addChild("armorLeftBoot", ModelPartBuilder.create().uv(86, 81).cuboid(-2.5F, 6.0F, -2.5F, 5.0F, 7.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)).uv(110, 89).cuboid(-2.5F, 11.0F, -4.25F, 5.0F, 2.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorLeftBoot.addChild("lBootClaw03_r1", ModelPartBuilder.create().uv(87, 94).cuboid(-1.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(-0.5F, 8.75F, -2.25F, 0.6545F, 0.1745F, 0.0F));
		armorLeftBoot.addChild("lBootClaw02_r1", ModelPartBuilder.create().uv(87, 94).cuboid(-1.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(1.5F, 8.75F, -2.25F, 0.6545F, -0.1745F, 0.0F));
		armorLeftBoot.addChild("lBootClaw01_r1", ModelPartBuilder.create().uv(87, 94).cuboid(-1.0F, 0.0F, -4.25F, 1.0F, 2.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(0.5F, 8.75F, -2.25F, 0.6545F, 0.0F, 0.0F));
		ModelPartData armorRightArm = rightArm.addChild("armorRightArm", ModelPartBuilder.create().uv(47, 82).cuboid(-4.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightArm.addChild("rShoulder_r1", ModelPartBuilder.create().uv(0, 68).cuboid(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 3.1416F, 0.0873F));
		armorRightArm.addChild("lArmSleeve02_r1", ModelPartBuilder.create().uv(50, 100).mirrored(true).cuboid(-2.3F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F), ModelTransform.of(-2.0F, 7.0F, 2.0F, 0.5236F, 0.0F, 0.0F));
		ModelPartData armorRightBoot = rightLeg.addChild("armorRightBoot", ModelPartBuilder.create().uv(86, 81).mirrored(true).cuboid(-2.5F, 6.0F, -2.5F, 5.0F, 7.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)).uv(110, 89).cuboid(-2.5F, 11.0F, -4.25F, 5.0F, 2.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightBoot.addChild("rBootClaw03_r1", ModelPartBuilder.create().uv(87, 94).mirrored(true).cuboid(0.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(0.5F, 8.75F, -2.25F, 0.6545F, -0.1745F, 0.0F));
		armorRightBoot.addChild("rBootClaw02_r1", ModelPartBuilder.create().uv(87, 94).mirrored(true).cuboid(0.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(-1.5F, 8.75F, -2.25F, 0.6545F, 0.1745F, 0.0F));
		armorRightBoot.addChild("rBootClaw01_r1", ModelPartBuilder.create().uv(87, 94).mirrored(true).cuboid(0.0F, 0.0F, -4.25F, 1.0F, 2.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(-0.5F, 8.75F, -2.25F, 0.6545F, 0.0F, 0.0F));
		ModelPartData armorLeftArm = leftArm.addChild("armorLeftArm", ModelPartBuilder.create().uv(47, 82).mirrored(true).cuboid(-0.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorLeftArm.addChild("lShoulder_r1", ModelPartBuilder.create().uv(0, 68).cuboid(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
		armorLeftArm.addChild("lArmSleeve02_r2", ModelPartBuilder.create().uv(50, 100).cuboid(-1.7F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F), ModelTransform.of(2.0F, 7.0F, 2.0F, 0.5236F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 128, 128);
	}
}
