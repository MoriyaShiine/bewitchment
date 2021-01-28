package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HerneEntityModel<T extends HerneEntity> extends BipedEntityModel<T> {
	public static final ItemStack HORNED_SPEAR = new ItemStack(BWObjects.HORNED_SPEAR);
	
	private final ModelPart chest;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart moss01;
	private final ModelPart moss02;
	private final ModelPart mossL;
	private final ModelPart mossR;
	private final ModelPart neck00;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	
	public HerneEntityModel() {
		super(1);
		textureWidth = 128;
		textureHeight = 64;
		chest = new ModelPart(this);
		chest.setPivot(0.0F, -9.4F, 0.0F);
		chest.setTextureOffset(23, 15).addCuboid(-4.9F, -8.0F, -3.5F, 10.0F, 8.0F, 7.0F, 0.0F, false);
		
		ModelPart lPec = new ModelPart(this);
		lPec.setPivot(2.6F, -4.8F, -1.2F);
		chest.addChild(lPec);
		setRotationAngle(lPec, 0.0F, -0.1047F, 0.0873F);
		lPec.setTextureOffset(19, 50).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F, 0.0F, false);
		
		ModelPart rPec = new ModelPart(this);
		rPec.setPivot(-2.6F, -4.8F, -1.2F);
		chest.addChild(rPec);
		setRotationAngle(rPec, 0.0F, 0.1047F, -0.0873F);
		rPec.setTextureOffset(19, 50).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F, 0.0F, true);
		
		ModelPart stomach = new ModelPart(this);
		stomach.setPivot(0.0F, 6.2F, 0.5F);
		chest.addChild(stomach);
		stomach.setTextureOffset(19, 31).addCuboid(-4.5F, -6.5F, -3.0F, 9.0F, 10.0F, 6.0F, 0.0F, false);
		
		bipedLeftLeg = new ModelPart(this);
		bipedLeftLeg.setPivot(2.5F, 3.0F, 0.1F);
		stomach.addChild(bipedLeftLeg);
		setRotationAngle(bipedLeftLeg, -0.2793F, 0.0F, -0.1047F);
		bipedLeftLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, -0.2F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.0F, 9.5F, -0.7F);
		bipedLeftLeg.addChild(lLeg02);
		setRotationAngle(lLeg02, 0.733F, 0.0F, 0.0873F);
		lLeg02.setTextureOffset(0, 34).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, -0.3F, false);
		
		ModelPart lLeg03 = new ModelPart(this);
		lLeg03.setPivot(0.0F, 7.1F, 0.1F);
		lLeg02.addChild(lLeg03);
		setRotationAngle(lLeg03, -0.4538F, 0.0F, 0.0F);
		lLeg03.setTextureOffset(0, 47).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, -0.15F, false);
		
		ModelPart lHoofClaw01a = new ModelPart(this);
		lHoofClaw01a.setPivot(0.7F, 8.4F, -1.4F);
		lLeg03.addChild(lHoofClaw01a);
		setRotationAngle(lHoofClaw01a, 0.0524F, -0.1047F, 0.0F);
		lHoofClaw01a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw01b = new ModelPart(this);
		lHoofClaw01b.setPivot(0.0F, 0.0F, -0.8F);
		lHoofClaw01a.addChild(lHoofClaw01b);
		setRotationAngle(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw01b.setTextureOffset(0, 59).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart lHoofClaw02a = new ModelPart(this);
		lHoofClaw02a.setPivot(-0.6F, 8.4F, -1.4F);
		lLeg03.addChild(lHoofClaw02a);
		setRotationAngle(lHoofClaw02a, 0.0524F, 0.0524F, 0.0F);
		lHoofClaw02a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw02b = new ModelPart(this);
		lHoofClaw02b.setPivot(0.0F, 0.0F, -0.8F);
		lHoofClaw02a.addChild(lHoofClaw02b);
		setRotationAngle(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw02b.setTextureOffset(0, 59).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		bipedRightLeg = new ModelPart(this);
		bipedRightLeg.setPivot(-2.5F, 3.0F, 0.1F);
		stomach.addChild(bipedRightLeg);
		setRotationAngle(bipedRightLeg, -0.2793F, 0.0F, 0.1047F);
		bipedRightLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, -0.2F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(0.0F, 9.5F, -0.7F);
		bipedRightLeg.addChild(rLeg02);
		setRotationAngle(rLeg02, 0.733F, 0.0F, -0.0873F);
		rLeg02.setTextureOffset(0, 34).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, -0.3F, true);
		
		ModelPart rLeg03 = new ModelPart(this);
		rLeg03.setPivot(0.0F, 7.1F, 0.1F);
		rLeg02.addChild(rLeg03);
		setRotationAngle(rLeg03, -0.4538F, 0.0F, 0.0F);
		rLeg03.setTextureOffset(0, 47).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, -0.15F, true);
		
		ModelPart rHoofClaw01a = new ModelPart(this);
		rHoofClaw01a.setPivot(-0.7F, 8.4F, -1.4F);
		rLeg03.addChild(rHoofClaw01a);
		setRotationAngle(rHoofClaw01a, 0.0524F, 0.1047F, 0.0F);
		rHoofClaw01a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw01b = new ModelPart(this);
		rHoofClaw01b.setPivot(0.0F, 0.0F, -0.8F);
		rHoofClaw01a.addChild(rHoofClaw01b);
		setRotationAngle(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw01b.setTextureOffset(0, 59).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rHoofClaw02a = new ModelPart(this);
		rHoofClaw02a.setPivot(0.6F, 8.4F, -1.4F);
		rLeg03.addChild(rHoofClaw02a);
		setRotationAngle(rHoofClaw02a, 0.0524F, -0.0524F, 0.0F);
		rHoofClaw02a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw02b = new ModelPart(this);
		rHoofClaw02b.setPivot(0.0F, 0.0F, -0.8F);
		rHoofClaw02a.addChild(rHoofClaw02b);
		setRotationAngle(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw02b.setTextureOffset(0, 59).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		moss01 = new ModelPart(this);
		moss01.setPivot(0.0F, 1.4F, -2.5F);
		stomach.addChild(moss01);
		setRotationAngle(moss01, -0.3665F, 0.0F, 0.0F);
		moss01.setTextureOffset(92, 45).addCuboid(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F, 0.0F, false);
		
		moss02 = new ModelPart(this);
		moss02.setPivot(0.0F, 1.4F, 2.8F);
		stomach.addChild(moss02);
		moss02.setTextureOffset(39, 53).addCuboid(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F, 0.0F, false);
		
		mossL = new ModelPart(this);
		mossL.setPivot(-4.7F, 2.1F, -0.1F);
		stomach.addChild(mossL);
		setRotationAngle(mossL, -0.1745F, 0.0F, 0.1745F);
		mossL.setTextureOffset(111, 48).addCuboid(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F, 0.0F, true);
		
		mossR = new ModelPart(this);
		mossR.setPivot(4.7F, 2.1F, -0.1F);
		stomach.addChild(mossR);
		setRotationAngle(mossR, -0.1745F, 0.0F, -0.1745F);
		mossR.setTextureOffset(111, 48).addCuboid(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F, 0.0F, false);
		
		neck00 = new ModelPart(this);
		neck00.setPivot(0.0F, -8.1F, -0.1F);
		chest.addChild(neck00);
		neck00.setTextureOffset(51, 0).addCuboid(-3.5F, -1.7F, -3.0F, 7.0F, 3.0F, 6.0F, 0.0F, false);
		
		ModelPart neck01 = new ModelPart(this);
		neck01.setPivot(0.0F, -1.2206F, -0.584F);
		neck00.addChild(neck01);
		setRotationAngle(neck01, 0.2182F, 0.0F, 0.0F);
		neck01.setTextureOffset(28, 0).addCuboid(-3.11F, -3.0F, -2.5F, 6.0F, 3.0F, 5.0F, 0.0F, false);
		
		ModelPart head = new ModelPart(this);
		head.setPivot(0.0F, -2.8F, 0.2F);
		neck01.addChild(head);
		setRotationAngle(head, -0.1396F, 0.0F, 0.0F);
		head.setTextureOffset(0, 0).addCuboid(-3.0F, -4.8F, -3.6F, 6.0F, 5.0F, 6.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -3.2F, -4.1F);
		head.addChild(snout);
		setRotationAngle(snout, 0.2793F, 0.0F, 0.0F);
		snout.setTextureOffset(78, 0).addCuboid(-2.0F, -1.35F, -3.9F, 4.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, -0.5F, -3.9F);
		head.addChild(lowerJaw);
		lowerJaw.setTextureOffset(97, 10).addCuboid(-2.0F, -0.4F, -3.6F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(2.3F, -3.4F, 0.9F);
		head.addChild(lEar);
		setRotationAngle(lEar, 0.1745F, -0.3491F, -0.4538F);
		lEar.setTextureOffset(20, 0).addCuboid(0.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(-2.3F, -3.4F, 0.9F);
		head.addChild(rEar);
		setRotationAngle(rEar, 0.1745F, 0.3491F, 0.4538F);
		rEar.setTextureOffset(20, 0).addCuboid(-3.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler01 = new ModelPart(this);
		lAntler01.setPivot(1.5F, -4.6F, -0.6F);
		head.addChild(lAntler01);
		setRotationAngle(lAntler01, -0.3491F, 0.0F, 0.2793F);
		lAntler01.setTextureOffset(107, 0).addCuboid(-1.0F, -2.3F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lAntler02a = new ModelPart(this);
		lAntler02a.setPivot(-0.2F, -1.7F, -0.3F);
		lAntler01.addChild(lAntler02a);
		setRotationAngle(lAntler02a, 0.7854F, 0.5236F, 0.1396F);
		lAntler02a.setTextureOffset(107, 0).addCuboid(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart lAntler02b = new ModelPart(this);
		lAntler02b.setPivot(0.0F, 0.0F, 0.0F);
		lAntler02a.addChild(lAntler02b);
		lAntler02b.setTextureOffset(107, 0).addCuboid(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart lAntler03 = new ModelPart(this);
		lAntler03.setPivot(0.2F, 0.0F, 5.4F);
		lAntler02b.addChild(lAntler03);
		setRotationAngle(lAntler03, 0.6109F, -0.2618F, 0.0F);
		lAntler03.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart lAntler04 = new ModelPart(this);
		lAntler04.setPivot(0.0F, 0.0F, 4.8F);
		lAntler03.addChild(lAntler04);
		setRotationAngle(lAntler04, -0.5236F, 0.0F, 0.0F);
		lAntler04.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart lAntler04b = new ModelPart(this);
		lAntler04b.setPivot(0.0F, 0.0F, 4.8F);
		lAntler04.addChild(lAntler04b);
		setRotationAngle(lAntler04b, 0.3491F, -0.2618F, 0.0F);
		lAntler04b.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart lAntler07a = new ModelPart(this);
		lAntler07a.setPivot(0.0F, 0.1F, 1.0F);
		lAntler03.addChild(lAntler07a);
		setRotationAngle(lAntler07a, 0.8727F, 0.0F, -0.5934F);
		lAntler07a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler07b = new ModelPart(this);
		lAntler07b.setPivot(0.0F, 2.9F, 0.0F);
		lAntler07a.addChild(lAntler07b);
		setRotationAngle(lAntler07b, 0.2793F, 0.0F, 0.0F);
		lAntler07b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler08a = new ModelPart(this);
		lAntler08a.setPivot(0.0F, 0.1F, 1.2F);
		lAntler03.addChild(lAntler08a);
		setRotationAngle(lAntler08a, 0.6981F, 0.0F, 0.3491F);
		lAntler08a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler08b = new ModelPart(this);
		lAntler08b.setPivot(0.0F, 2.9F, 0.0F);
		lAntler08a.addChild(lAntler08b);
		setRotationAngle(lAntler08b, 0.2793F, 0.0F, 0.3491F);
		lAntler08b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler09a = new ModelPart(this);
		lAntler09a.setPivot(0.0F, -0.4F, -1.0F);
		lAntler03.addChild(lAntler09a);
		setRotationAngle(lAntler09a, 0.6109F, -0.1222F, 0.4363F);
		lAntler09a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler09b = new ModelPart(this);
		lAntler09b.setPivot(0.0F, 4.8F, 0.0F);
		lAntler09a.addChild(lAntler09b);
		setRotationAngle(lAntler09b, 0.2793F, 0.0F, 0.3491F);
		lAntler09b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05a = new ModelPart(this);
		lAntler05a.setPivot(0.0F, 0.4F, 4.8F);
		lAntler02a.addChild(lAntler05a);
		setRotationAngle(lAntler05a, -0.5236F, 0.0F, 0.3665F);
		lAntler05a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05b = new ModelPart(this);
		lAntler05b.setPivot(0.0F, 0.0F, 0.0F);
		lAntler05a.addChild(lAntler05b);
		lAntler05b.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05c = new ModelPart(this);
		lAntler05c.setPivot(-0.1F, -2.9F, -0.2F);
		lAntler05b.addChild(lAntler05c);
		setRotationAngle(lAntler05c, 0.0F, 0.0F, -0.1745F);
		lAntler05c.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05d = new ModelPart(this);
		lAntler05d.setPivot(0.1F, -3.1F, 0.0F);
		lAntler05c.addChild(lAntler05d);
		setRotationAngle(lAntler05d, 0.0F, 0.0F, -0.3491F);
		lAntler05d.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler10a = new ModelPart(this);
		lAntler10a.setPivot(0.4F, -0.4F, 0.7F);
		lAntler02a.addChild(lAntler10a);
		setRotationAngle(lAntler10a, 0.0F, 0.0F, 0.3665F);
		lAntler10a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler10b = new ModelPart(this);
		lAntler10b.setPivot(0.1F, -3.1F, 0.0F);
		lAntler10a.addChild(lAntler10b);
		setRotationAngle(lAntler10b, 0.0F, 0.0F, -0.3142F);
		lAntler10b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler11a = new ModelPart(this);
		lAntler11a.setPivot(0.4F, 0.0F, -0.2F);
		lAntler02a.addChild(lAntler11a);
		setRotationAngle(lAntler11a, 0.4363F, 0.0F, 0.4363F);
		lAntler11a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler11b = new ModelPart(this);
		lAntler11b.setPivot(0.1F, -2.8F, 0.0F);
		lAntler11a.addChild(lAntler11b);
		setRotationAngle(lAntler11b, 0.0F, 0.0F, -0.3142F);
		lAntler11b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler01 = new ModelPart(this);
		rAntler01.setPivot(-1.5F, -4.7F, -0.6F);
		head.addChild(rAntler01);
		setRotationAngle(rAntler01, -0.3491F, 0.0F, -0.2793F);
		rAntler01.setTextureOffset(107, 0).addCuboid(-1.0F, -2.2F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rAntler02a = new ModelPart(this);
		rAntler02a.setPivot(0.2F, -1.7F, -0.3F);
		rAntler01.addChild(rAntler02a);
		setRotationAngle(rAntler02a, 0.7854F, -0.5236F, -0.1396F);
		rAntler02a.setTextureOffset(107, 0).addCuboid(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart rAntler02b = new ModelPart(this);
		rAntler02b.setPivot(0.0F, 0.0F, 0.0F);
		rAntler02a.addChild(rAntler02b);
		rAntler02b.setTextureOffset(107, 0).addCuboid(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart rAntler03 = new ModelPart(this);
		rAntler03.setPivot(-0.2F, 0.0F, 5.4F);
		rAntler02b.addChild(rAntler03);
		setRotationAngle(rAntler03, 0.6109F, 0.2618F, 0.0F);
		rAntler03.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart rAntler04 = new ModelPart(this);
		rAntler04.setPivot(0.0F, 0.0F, 4.8F);
		rAntler03.addChild(rAntler04);
		setRotationAngle(rAntler04, -0.5236F, 0.0F, 0.0F);
		rAntler04.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart rAntler04b = new ModelPart(this);
		rAntler04b.setPivot(0.0F, 0.0F, 4.8F);
		rAntler04.addChild(rAntler04b);
		setRotationAngle(rAntler04b, 0.3491F, 0.2618F, 0.0F);
		rAntler04b.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rAntler07a = new ModelPart(this);
		rAntler07a.setPivot(0.0F, 0.1F, 1.0F);
		rAntler03.addChild(rAntler07a);
		setRotationAngle(rAntler07a, 0.8727F, 0.0F, 0.5934F);
		rAntler07a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler07b = new ModelPart(this);
		rAntler07b.setPivot(0.0F, 2.9F, 0.0F);
		rAntler07a.addChild(rAntler07b);
		setRotationAngle(rAntler07b, 0.2793F, 0.0F, 0.0F);
		rAntler07b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler08a = new ModelPart(this);
		rAntler08a.setPivot(0.0F, 0.1F, 1.2F);
		rAntler03.addChild(rAntler08a);
		setRotationAngle(rAntler08a, 0.6981F, 0.0F, -0.3491F);
		rAntler08a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler08b = new ModelPart(this);
		rAntler08b.setPivot(0.0F, 2.9F, 0.0F);
		rAntler08a.addChild(rAntler08b);
		setRotationAngle(rAntler08b, 0.2793F, 0.0F, -0.3491F);
		rAntler08b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler09a = new ModelPart(this);
		rAntler09a.setPivot(0.0F, -0.4F, -1.0F);
		rAntler03.addChild(rAntler09a);
		setRotationAngle(rAntler09a, 0.6109F, 0.1222F, -0.4363F);
		rAntler09a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler09b = new ModelPart(this);
		rAntler09b.setPivot(0.0F, 4.8F, 0.0F);
		rAntler09a.addChild(rAntler09b);
		setRotationAngle(rAntler09b, 0.2793F, 0.0F, -0.3491F);
		rAntler09b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05a = new ModelPart(this);
		rAntler05a.setPivot(0.0F, 0.4F, 4.8F);
		rAntler02a.addChild(rAntler05a);
		setRotationAngle(rAntler05a, -0.5236F, 0.0F, -0.3665F);
		rAntler05a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05b = new ModelPart(this);
		rAntler05b.setPivot(0.0F, 0.0F, 0.0F);
		rAntler05a.addChild(rAntler05b);
		rAntler05b.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05c = new ModelPart(this);
		rAntler05c.setPivot(0.1F, -2.9F, -0.2F);
		rAntler05b.addChild(rAntler05c);
		setRotationAngle(rAntler05c, 0.0F, 0.0F, 0.1745F);
		rAntler05c.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05d = new ModelPart(this);
		rAntler05d.setPivot(-0.1F, -3.1F, 0.0F);
		rAntler05c.addChild(rAntler05d);
		setRotationAngle(rAntler05d, 0.0F, 0.0F, 0.3491F);
		rAntler05d.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler10a = new ModelPart(this);
		rAntler10a.setPivot(-0.4F, -0.4F, 0.7F);
		rAntler02a.addChild(rAntler10a);
		setRotationAngle(rAntler10a, 0.0F, 0.0F, -0.3665F);
		rAntler10a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler10b = new ModelPart(this);
		rAntler10b.setPivot(-0.1F, -3.1F, 0.0F);
		rAntler10a.addChild(rAntler10b);
		setRotationAngle(rAntler10b, 0.0F, 0.0F, 0.3142F);
		rAntler10b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler11a = new ModelPart(this);
		rAntler11a.setPivot(-0.4F, 0.0F, -0.2F);
		rAntler02a.addChild(rAntler11a);
		setRotationAngle(rAntler11a, 0.4363F, 0.0F, -0.4363F);
		rAntler11a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler11b = new ModelPart(this);
		rAntler11b.setPivot(0.1F, -2.8F, 0.0F);
		rAntler11a.addChild(rAntler11b);
		setRotationAngle(rAntler11b, 0.0F, 0.0F, 0.3142F);
		rAntler11b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart upperJaw00 = new ModelPart(this);
		upperJaw00.setPivot(0.0F, -2.0F, -4.1F);
		head.addChild(upperJaw00);
		upperJaw00.setTextureOffset(76, 8).addCuboid(-2.5F, -0.7F, -3.7F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart neckFur01 = new ModelPart(this);
		neckFur01.setPivot(0.0F, -1.75F, -1.9F);
		neck00.addChild(neckFur01);
		setRotationAngle(neckFur01, -0.6109F, 0.0F, 0.0F);
		neckFur01.setTextureOffset(79, 17).addCuboid(-2.0F, -1.0F, -1.8F, 4.0F, 5.0F, 2.0F, 0.1F, false);
		
		ModelPart neckFur02 = new ModelPart(this);
		neckFur02.setPivot(0.0F, 0.5F, -2.9F);
		neck00.addChild(neckFur02);
		setRotationAngle(neckFur02, -0.1309F, 0.0F, 0.0F);
		neckFur02.setTextureOffset(110, 16).addCuboid(-3.0F, -1.0F, -1.9F, 6.0F, 8.0F, 2.0F, 0.15F, false);
		
		ModelPart mMantle = new ModelPart(this);
		mMantle.setPivot(0.0F, -8.8F, -0.5F);
		chest.addChild(mMantle);
		mMantle.setTextureOffset(75, 26).addCuboid(-6.0F, 0.0F, -4.5F, 12.0F, 10.0F, 9.0F, 0.0F, false);
		
		bipedLeftArm = new ModelPart(this);
		bipedLeftArm.setPivot(5.5F, -6.3F, 1.0F);
		chest.addChild(bipedLeftArm);
		setRotationAngle(bipedLeftArm, 0.0F, 0.0F, -0.1309F);
		bipedLeftArm.setTextureOffset(58, 18).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F, 0.0F, false);
		
		ModelPart lMantle = new ModelPart(this);
		lMantle.setPivot(-0.1F, -0.3F, -0.5F);
		bipedLeftArm.addChild(lMantle);
		setRotationAngle(lMantle, 0.0F, 0.0F, -0.1309F);
		lMantle.setTextureOffset(69, 45).addCuboid(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F, 0.0F, false);
		
		bipedRightArm = new ModelPart(this);
		bipedRightArm.setPivot(-5.5F, -6.3F, 1.0F);
		chest.addChild(bipedRightArm);
		setRotationAngle(bipedRightArm, 0.0F, 0.0F, 0.1309F);
		bipedRightArm.setTextureOffset(58, 18).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F, 0.0F, true);
		
		ModelPart rMantle = new ModelPart(this);
		rMantle.setPivot(0.1F, -0.3F, -0.5F);
		bipedRightArm.addChild(rMantle);
		setRotationAngle(rMantle, 0.0F, 0.0F, 0.1309F);
		rMantle.setTextureOffset(69, 45).addCuboid(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F, 0.0F, true);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		entity.setStackInHand(Hand.MAIN_HAND, HORNED_SPEAR);
		neck00.copyPositionAndRotation(super.head);
		neck00.pivotY = -8.1f;
		bipedLeftArm.copyPositionAndRotation(super.leftArm);
		bipedLeftArm.roll -= 1 / 8f;
		bipedLeftArm.pivotX = 5.5f;
		bipedLeftArm.pivotY = -6.3f;
		bipedRightArm.copyPositionAndRotation(super.rightArm);
		bipedRightArm.roll += 1 / 8f;
		bipedRightArm.pivotX = -5.5f;
		bipedRightArm.pivotY = -6.3f;
		bipedRightLeg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * limbDistance - 0.2793f;
		bipedLeftLeg.pitch = -bipedRightLeg.pitch - 0.5586f;
		moss01.pitch = Math.min(bipedLeftLeg.pitch, bipedRightLeg.pitch);
		mossL.pitch = moss01.pitch;
		mossR.pitch = moss01.pitch;
		moss02.pitch = Math.max(bipedLeftLeg.pitch, bipedRightLeg.pitch) + 1 / 3f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		chest.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}