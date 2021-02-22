package moriyashiine.bewitchment.client.model.equipment.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class WitchArmorModel<T extends LivingEntity> extends BipedEntityModel<T> {
	private final EquipmentSlot slot;
	private final boolean hood;
	private final boolean wearingBoots;
	
	private final ModelPart hat1;
	private final ModelPart hood01;
	private final ModelPart lowerLeftSkirt;
	private final ModelPart armorLeftBoot;
	private final ModelPart lowerRightSkirt;
	private final ModelPart armorRightBoot;
	
	public WitchArmorModel(EquipmentSlot slot, boolean hood, boolean wearingBoots) {
		super(RenderLayer::getArmorCutoutNoCull, 1, 0, 128, 128);
		this.slot = slot;
		this.hood = hood;
		this.wearingBoots = wearingBoots;
		
		ModelPart armorHead = new ModelPart(this);
		armorHead.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(armorHead);
		
		hat1 = new ModelPart(this);
		hat1.setPivot(0.0F, 0.0F, 0.0F);
		armorHead.addChild(hat1);
		hat1.setTextureOffset(49, 111).addCuboid(-4.5F, -9.5F, -4.5F, 9.0F, 4.0F, 9.0F, 0.1F, false);
		
		ModelPart hat2 = new ModelPart(this);
		hat2.setPivot(0.0F, -9.5F, -3.0F);
		hat1.addChild(hat2);
		setRotation(hat2, -0.2618F, 0.0F, 0.0F);
		hat2.setTextureOffset(89, 114).addCuboid(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
		
		ModelPart hat3 = new ModelPart(this);
		hat3.setPivot(-1.4F, -4.0F, 1.5F);
		hat2.addChild(hat3);
		setRotation(hat3, -0.3491F, 0.0F, 0.0F);
		hat3.setTextureOffset(110, 112).addCuboid(0.0F, -4.0F, 0.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		
		ModelPart hatWing = new ModelPart(this);
		hatWing.setPivot(0.0F, -6.0F, 0.0F);
		hat1.addChild(hatWing);
		setRotation(hatWing, 0.0349F, 0.0F, 0.0698F);
		hatWing.setTextureOffset(0, 112).addCuboid(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
		
		hood01 = new ModelPart(this);
		hood01.setPivot(0.0F, 0.0F, 0.0F);
		armorHead.addChild(hood01);
		hood01.setTextureOffset(70, 0).addCuboid(-4.5F, -8.6F, -4.7F, 9.0F, 9.0F, 10.0F, 0.1F, false);
		
		ModelPart hoodFringe01 = new ModelPart(this);
		hoodFringe01.setPivot(0.0F, 0.0F, 0.1F);
		hood01.addChild(hoodFringe01);
		hoodFringe01.setTextureOffset(66, 0).addCuboid(-3.0F, -8.9F, -5.4F, 6.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart hoodFringeL01 = new ModelPart(this);
		hoodFringeL01.setPivot(-4.1F, -8.2F, 0.1F);
		hood01.addChild(hoodFringeL01);
		setRotation(hoodFringeL01, 0.0F, 0.0F, 0.2793F);
		hoodFringeL01.setTextureOffset(77, 24).addCuboid(0.0F, -0.3F, -5.39F, 2.0F, 8.0F, 10.0F, 0.0F, true);
		
		ModelPart hoodFringeR01 = new ModelPart(this);
		hoodFringeR01.setPivot(4.1F, -8.2F, 0.1F);
		hood01.addChild(hoodFringeR01);
		setRotation(hoodFringeR01, 0.0F, 0.0F, -0.2793F);
		hoodFringeR01.setTextureOffset(77, 24).addCuboid(-2.0F, -0.3F, -5.39F, 2.0F, 8.0F, 10.0F, 0.0F, false);
		
		ModelPart hoodFringeL02 = new ModelPart(this);
		hoodFringeL02.setPivot(0.85F, 0.55F, 0.1F);
		hood01.addChild(hoodFringeL02);
		setRotation(hoodFringeL02, 0.0F, 0.0F, -0.3142F);
		hoodFringeL02.setTextureOffset(97, 33).addCuboid(0.0F, -0.9F, -5.37F, 5.0F, 2.0F, 10.0F, 0.0F, false);
		
		ModelPart hoodFringeR02 = new ModelPart(this);
		hoodFringeR02.setPivot(-0.85F, 0.55F, 0.1F);
		hood01.addChild(hoodFringeR02);
		setRotation(hoodFringeR02, 0.0F, 0.0F, 0.3142F);
		hoodFringeR02.setTextureOffset(97, 33).addCuboid(-5.0F, -0.9F, -5.37F, 5.0F, 2.0F, 10.0F, 0.0F, true);
		
		ModelPart hoodShade = new ModelPart(this);
		hoodShade.setPivot(0.0F, -7.3F, -5.2F);
		hood01.addChild(hoodShade);
		setRotation(hoodShade, 0.0524F, 0.0F, 0.0F);
		hoodShade.setTextureOffset(105, 0).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		
		ModelPart hoodFringeL04 = new ModelPart(this);
		hoodFringeL04.setPivot(0.3F, -8.9F, 0.1F);
		hood01.addChild(hoodFringeL04);
		setRotation(hoodFringeL04, 0.0F, 0.0F, 0.3142F);
		hoodFringeL04.setTextureOffset(57, 20).addCuboid(-0.81F, -1.0F, -5.37F, 4.0F, 2.0F, 11.0F, 0.0F, false);
		
		ModelPart hoodFringeR04 = new ModelPart(this);
		hoodFringeR04.setPivot(-0.8F, -8.9F, 0.1F);
		hood01.addChild(hoodFringeR04);
		setRotation(hoodFringeR04, 0.0F, 0.0F, -0.3142F);
		hoodFringeR04.setTextureOffset(57, 20).addCuboid(-3.0F, -1.0F, -5.38F, 4.0F, 2.0F, 11.0F, 0.0F, true);
		
		ModelPart hoodPipe01 = new ModelPart(this);
		hoodPipe01.setPivot(0.0F, -7.0F, 4.2F);
		hood01.addChild(hoodPipe01);
		setRotation(hoodPipe01, -0.4014F, 0.0F, 0.0F);
		hoodPipe01.setTextureOffset(65, 43).addCuboid(-3.0F, -2.5F, 0.0F, 6.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart hoodPipe02 = new ModelPart(this);
		hoodPipe02.setPivot(0.0F, -0.3F, 3.1F);
		hoodPipe01.addChild(hoodPipe02);
		setRotation(hoodPipe02, -0.4538F, 0.0F, 0.0F);
		hoodPipe02.setTextureOffset(65, 54).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		
		ModelPart armorBody = new ModelPart(this);
		armorBody.setPivot(0.0F, 0.0F, 0.0F);
		torso.addChild(armorBody);
		armorBody.setTextureOffset(0, 81).addCuboid(-4.5F, -0.01F, -2.5F, 9.0F, 12.0F, 5.0F, 0.0F, false);
		
		ModelPart armorRightArm = new ModelPart(this);
		armorRightArm.setPivot(1, 0.0F, 0.0F);
		rightArm.addChild(armorRightArm);
		armorRightArm.setTextureOffset(47, 82).addCuboid(-4.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, -0.1F, false);
		
		ModelPart rShoulder_r1 = new ModelPart(this);
		rShoulder_r1.setPivot(0.0F, -1.0F, 0.0F);
		armorRightArm.addChild(rShoulder_r1);
		setRotation(rShoulder_r1, 0.0F, 3.1416F, 0.0873F);
		rShoulder_r1.setTextureOffset(0, 68).addCuboid(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
		
		ModelPart lArmSleeve02_r1 = new ModelPart(this);
		lArmSleeve02_r1.setPivot(-2.0F, 7.0F, 2.0F);
		armorRightArm.addChild(lArmSleeve02_r1);
		setRotation(lArmSleeve02_r1, 0.5236F, 0.0F, 0.0F);
		lArmSleeve02_r1.setTextureOffset(50, 100).addCuboid(-2.3F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F, 0.0F, true);
		
		ModelPart armorLeftArm = new ModelPart(this);
		armorLeftArm.setPivot(-1, 0.0F, 0.0F);
		leftArm.addChild(armorLeftArm);
		armorLeftArm.setTextureOffset(47, 82).addCuboid(-0.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, -0.1F, true);
		
		ModelPart lShoulder_r1 = new ModelPart(this);
		lShoulder_r1.setPivot(0.0F, -1.0F, 0.0F);
		armorLeftArm.addChild(lShoulder_r1);
		setRotation(lShoulder_r1, 0.0F, 0.0F, -0.0873F);
		lShoulder_r1.setTextureOffset(0, 68).addCuboid(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
		
		ModelPart lArmSleeve02_r2 = new ModelPart(this);
		lArmSleeve02_r2.setPivot(2.0F, 7.0F, 2.0F);
		armorLeftArm.addChild(lArmSleeve02_r2);
		setRotation(lArmSleeve02_r2, 0.5236F, 0.0F, 0.0F);
		lArmSleeve02_r2.setTextureOffset(50, 100).addCuboid(-1.7F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart armorLeftLeg = new ModelPart(this);
		armorLeftLeg.setPivot(-3.9f, 0.0F, 0.0F);
		leftLeg.addChild(armorLeftLeg);
		
		ModelPart tunicLeftFront = new ModelPart(this);
		tunicLeftFront.setPivot(1.99F, 0.0F, -2.1F);
		armorLeftLeg.addChild(tunicLeftFront);
		setRotation(tunicLeftFront, -3.0194F, 0.0F, 3.1416F);
		tunicLeftFront.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicFront = new ModelPart(this);
		tunicFront.setPivot(0.01F, 10.0F, -1.0F);
		tunicLeftFront.addChild(tunicFront);
		setRotation(tunicFront, 0.2618F, 0.0F, 0.0F);
		
		ModelPart tunicLeft = new ModelPart(this);
		tunicLeft.setPivot(6.01F, 0.0F, -2.1F);
		armorLeftLeg.addChild(tunicLeft);
		setRotation(tunicLeft, 0.1222F, 1.5708F, 0.0F);
		tunicLeft.setTextureOffset(23, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicLeft2 = new ModelPart(this);
		tunicLeft2.setPivot(0.01F, 10.0F, -1.0F);
		tunicLeft.addChild(tunicLeft2);
		setRotation(tunicLeft2, 0.2618F, 0.0F, 0.0F);
		
		ModelPart tunicLeftBack = new ModelPart(this);
		tunicLeftBack.setPivot(1.99F, 0.0F, 2.1F);
		armorLeftLeg.addChild(tunicLeftBack);
		setRotation(tunicLeftBack, 3.0194F, 0.0F, 3.1416F);
		tunicLeftBack.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicLeftBack2 = new ModelPart(this);
		tunicLeftBack2.setPivot(0.01F, 10.0F, 1.0F);
		tunicLeftBack.addChild(tunicLeftBack2);
		setRotation(tunicLeftBack2, -0.2618F, 0.0F, 0.0F);
		
		lowerLeftSkirt = new ModelPart(this);
		lowerLeftSkirt.setPivot(11.0F, 0.0F, 0.0F);
		armorLeftLeg.addChild(lowerLeftSkirt);
		
		ModelPart tunicLowerFront = new ModelPart(this);
		tunicLowerFront.setPivot(-6.0F, 10.0F, -3.1F);
		lowerLeftSkirt.addChild(tunicLowerFront);
		setRotation(tunicLowerFront, -3.0194F, 0.0F, -3.1416F);
		tunicLowerFront.setTextureOffset(53, 72).addCuboid(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicFrontFringe_r1 = new ModelPart(this);
		tunicFrontFringe_r1.setPivot(1.0F, -1.0F, -0.4F);
		tunicLowerFront.addChild(tunicFrontFringe_r1);
		setRotation(tunicFrontFringe_r1, 0.2618F, 0.0F, 0.0F);
		tunicFrontFringe_r1.setTextureOffset(53, 77).addCuboid(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicLowerLeft = new ModelPart(this);
		tunicLowerLeft.setPivot(-4.99F, 0.0F, -2.1F);
		lowerLeftSkirt.addChild(tunicLowerLeft);
		setRotation(tunicLowerLeft, 0.1222F, 1.5708F, 0.0F);
		tunicLowerLeft.setTextureOffset(23, 72).addCuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicLeft4 = new ModelPart(this);
		tunicLeft4.setPivot(0.01F, 10.0F, -1.0F);
		tunicLowerLeft.addChild(tunicLeft4);
		setRotation(tunicLeft4, 0.2618F, 0.0F, 0.0F);
		tunicLeft4.setTextureOffset(23, 78).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicLeftBack3 = new ModelPart(this);
		tunicLeftBack3.setPivot(-9.01F, 0.0F, 2.1F);
		lowerLeftSkirt.addChild(tunicLeftBack3);
		setRotation(tunicLeftBack3, 3.0194F, 0.0F, 3.1416F);
		tunicLeftBack3.setTextureOffset(53, 72).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicLeftBack4 = new ModelPart(this);
		tunicLeftBack4.setPivot(0.01F, 10.0F, 1.0F);
		tunicLeftBack3.addChild(tunicLeftBack4);
		setRotation(tunicLeftBack4, -0.2618F, 0.0F, 0.0F);
		tunicLeftBack4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		
		armorLeftBoot = new ModelPart(this);
		armorLeftBoot.setPivot(0, 0.0F, 0.0F);
		armorLeftBoot.setTextureOffset(86, 81).addCuboid(-2.5F, 6.0F, -2.5F, 5.0F, 7.0F, 5.0F, -0.1F, false);
		armorLeftBoot.setTextureOffset(110, 89).addCuboid(-2.5F, 11.0F, -4.25F, 5.0F, 2.0F, 2.0F, -0.1F, false);
		leftLeg.addChild(armorLeftBoot);
		
		ModelPart lBootClaw03_r1 = new ModelPart(this);
		lBootClaw03_r1.setPivot(-0.5F, 8.75F, -2.25F);
		armorLeftBoot.addChild(lBootClaw03_r1);
		setRotation(lBootClaw03_r1, 0.6545F, 0.1745F, 0.0F);
		lBootClaw03_r1.setTextureOffset(87, 94).addCuboid(-1.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, -0.1F, false);
		
		ModelPart lBootClaw02_r1 = new ModelPart(this);
		lBootClaw02_r1.setPivot(1.5F, 8.75F, -2.25F);
		armorLeftBoot.addChild(lBootClaw02_r1);
		setRotation(lBootClaw02_r1, 0.6545F, -0.1745F, 0.0F);
		lBootClaw02_r1.setTextureOffset(87, 94).addCuboid(-1.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, -0.1F, false);
		
		ModelPart lBootClaw01_r1 = new ModelPart(this);
		lBootClaw01_r1.setPivot(0.5F, 8.75F, -2.25F);
		armorLeftBoot.addChild(lBootClaw01_r1);
		setRotation(lBootClaw01_r1, 0.6545F, 0.0F, 0.0F);
		lBootClaw01_r1.setTextureOffset(87, 94).addCuboid(-1.0F, 0.0F, -4.25F, 1.0F, 2.0F, 5.0F, -0.1F, false);
		
		ModelPart armorRightLeg = new ModelPart(this);
		armorRightLeg.setPivot(3.9f, 0.0F, 0.0F);
		rightLeg.addChild(armorRightLeg);
		
		ModelPart tunicRightFront = new ModelPart(this);
		tunicRightFront.setPivot(-2.01F, 0.0F, -2.1F);
		armorRightLeg.addChild(tunicRightFront);
		setRotation(tunicRightFront, -0.1222F, 0.0F, 0.0F);
		tunicRightFront.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRight = new ModelPart(this);
		tunicRight.setPivot(-6.01F, 0.0F, -2.1F);
		armorRightLeg.addChild(tunicRight);
		setRotation(tunicRight, -0.1222F, 1.5708F, 0.0F);
		tunicRight.setTextureOffset(24, 65).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRightBack = new ModelPart(this);
		tunicRightBack.setPivot(-2.01F, 0.0F, 2.1F);
		armorRightLeg.addChild(tunicRightBack);
		setRotation(tunicRightBack, 0.1222F, 0.0F, 0.0F);
		tunicRightBack.setTextureOffset(53, 65).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRightBack2 = new ModelPart(this);
		tunicRightBack2.setPivot(0.01F, 10.0F, -1.0F);
		tunicRightBack.addChild(tunicRightBack2);
		setRotation(tunicRightBack2, 0.2618F, 0.0F, 0.0F);
		
		lowerRightSkirt = new ModelPart(this);
		lowerRightSkirt.setPivot(-4.0F, 0.0F, 0.0F);
		armorRightLeg.addChild(lowerRightSkirt);
		
		ModelPart tunicLowerFront2 = new ModelPart(this);
		tunicLowerFront2.setPivot(1.99F, 0.0F, -2.1F);
		lowerRightSkirt.addChild(tunicLowerFront2);
		setRotation(tunicLowerFront2, -0.1222F, 0.0F, 0.0F);
		tunicLowerFront2.setTextureOffset(53, 71).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRightFront4 = new ModelPart(this);
		tunicRightFront4.setPivot(0.01F, 10.0F, 1.0F);
		tunicLowerFront2.addChild(tunicRightFront4);
		setRotation(tunicRightFront4, -0.2618F, 0.0F, 0.0F);
		tunicRightFront4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRight3 = new ModelPart(this);
		tunicRight3.setPivot(-2.01F, 0.0F, -2.1F);
		lowerRightSkirt.addChild(tunicRight3);
		setRotation(tunicRight3, -0.1222F, 1.5708F, 0.0F);
		tunicRight3.setTextureOffset(24, 72).addCuboid(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRight4 = new ModelPart(this);
		tunicRight4.setPivot(0.01F, 10.0F, 1.0F);
		tunicRight3.addChild(tunicRight4);
		setRotation(tunicRight4, -0.2618F, 0.0F, 0.0F);
		tunicRight4.setTextureOffset(23, 78).addCuboid(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRightBack3 = new ModelPart(this);
		tunicRightBack3.setPivot(1.99F, 0.0F, 2.1F);
		lowerRightSkirt.addChild(tunicRightBack3);
		setRotation(tunicRightBack3, 0.1222F, 0.0F, 0.0F);
		tunicRightBack3.setTextureOffset(53, 72).addCuboid(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart tunicRightBack4 = new ModelPart(this);
		tunicRightBack4.setPivot(0.01F, 10.0F, -1.0F);
		tunicRightBack3.addChild(tunicRightBack4);
		setRotation(tunicRightBack4, 0.2618F, 0.0F, 0.0F);
		tunicRightBack4.setTextureOffset(53, 77).addCuboid(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		
		armorRightBoot = new ModelPart(this);
		armorRightBoot.setPivot(0, 0.0F, 0.0F);
		armorRightBoot.setTextureOffset(86, 81).addCuboid(-2.5F, 6.0F, -2.5F, 5.0F, 7.0F, 5.0F, -0.1F, true);
		armorRightBoot.setTextureOffset(110, 89).addCuboid(-2.5F, 11.0F, -4.25F, 5.0F, 2.0F, 2.0F, -0.1F, true);
		rightLeg.addChild(armorRightBoot);
		
		ModelPart rBootClaw03_r1 = new ModelPart(this);
		rBootClaw03_r1.setPivot(0.5F, 8.75F, -2.25F);
		armorRightBoot.addChild(rBootClaw03_r1);
		setRotation(rBootClaw03_r1, 0.6545F, -0.1745F, 0.0F);
		rBootClaw03_r1.setTextureOffset(87, 94).addCuboid(0.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, -0.1F, true);
		
		ModelPart rBootClaw02_r1 = new ModelPart(this);
		rBootClaw02_r1.setPivot(-1.5F, 8.75F, -2.25F);
		armorRightBoot.addChild(rBootClaw02_r1);
		setRotation(rBootClaw02_r1, 0.6545F, 0.1745F, 0.0F);
		rBootClaw02_r1.setTextureOffset(87, 94).addCuboid(0.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, -0.1F, true);
		
		ModelPart rBootClaw01_r1 = new ModelPart(this);
		rBootClaw01_r1.setPivot(-0.5F, 8.75F, -2.25F);
		armorRightBoot.addChild(rBootClaw01_r1);
		setRotation(rBootClaw01_r1, 0.6545F, 0.0F, 0.0F);
		rBootClaw01_r1.setTextureOffset(87, 94).addCuboid(0.0F, 0.0F, -4.25F, 1.0F, 2.0F, 5.0F, -0.1F, true);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		hood01.visible = hood;
		hat1.visible = !hood;
		head.visible = slot == EquipmentSlot.HEAD;
		helmet.visible = slot == EquipmentSlot.HEAD;
		torso.visible = slot == EquipmentSlot.CHEST;
		leftArm.visible = slot == EquipmentSlot.CHEST;
		rightArm.visible = slot == EquipmentSlot.CHEST;
		armorLeftBoot.visible = slot == EquipmentSlot.FEET;
		armorRightBoot.visible = slot == EquipmentSlot.FEET;
		lowerLeftSkirt.visible = !wearingBoots;
		lowerRightSkirt.visible = !wearingBoots;
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
