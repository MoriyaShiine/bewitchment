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
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
public class HerneEntityModel<T extends HerneEntity> extends BipedEntityModel<T> {
	public static final ItemStack HORNED_SPEAR = new ItemStack(BWObjects.HORNED_SPEAR);
	
	private final ModelPart chest;
	private final ModelPart moss01;
	private final ModelPart moss02;
	private final ModelPart mossL;
	private final ModelPart mossR;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart neck00;
	
	private boolean realArm = false;
	
	public HerneEntityModel() {
		super(1, 0, 128, 64);
		chest = new ModelPart(this);
		chest.setPivot(0.0F, -9.4F, 0.0F);
		chest.setTextureOffset(23, 15).addCuboid(-4.9F, -8.0F, -3.5F, 10.0F, 8.0F, 7.0F, 0.0F, false);
		
		ModelPart lPec = new ModelPart(this);
		lPec.setPivot(2.6F, -4.8F, -1.2F);
		chest.addChild(lPec);
		setRotation(lPec, 0.0F, -0.1047F, 0.0873F);
		lPec.setTextureOffset(19, 50).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F, 0.0F, false);
		
		ModelPart rPec = new ModelPart(this);
		rPec.setPivot(-2.6F, -4.8F, -1.2F);
		chest.addChild(rPec);
		setRotation(rPec, 0.0F, 0.1047F, -0.0873F);
		rPec.setTextureOffset(19, 50).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F, 0.0F, true);
		
		ModelPart stomach = new ModelPart(this);
		stomach.setPivot(0.0F, 6.2F, 0.5F);
		chest.addChild(stomach);
		stomach.setTextureOffset(19, 31).addCuboid(-4.5F, -6.5F, -3.0F, 9.0F, 10.0F, 6.0F, 0.0F, false);
		
		moss01 = new ModelPart(this);
		moss01.setPivot(0.0F, 1.4F, -2.5F);
		stomach.addChild(moss01);
		setRotation(moss01, -0.3665F, 0.0F, 0.0F);
		moss01.setTextureOffset(92, 45).addCuboid(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F, 0.0F, false);
		
		moss02 = new ModelPart(this);
		moss02.setPivot(0.0F, 1.4F, 2.8F);
		stomach.addChild(moss02);
		moss02.setTextureOffset(39, 53).addCuboid(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F, 0.0F, false);
		
		mossL = new ModelPart(this);
		mossL.setPivot(-4.7F, 2.1F, -0.1F);
		stomach.addChild(mossL);
		setRotation(mossL, -0.1745F, 0.0F, 0.1745F);
		mossL.setTextureOffset(111, 48).addCuboid(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F, 0.0F, true);
		
		mossR = new ModelPart(this);
		mossR.setPivot(4.7F, 2.1F, -0.1F);
		stomach.addChild(mossR);
		setRotation(mossR, -0.1745F, 0.0F, -0.1745F);
		mossR.setTextureOffset(111, 48).addCuboid(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F, 0.0F, false);
		
		ModelPart mMantle = new ModelPart(this);
		mMantle.setPivot(0.0F, -8.8F, -0.5F);
		chest.addChild(mMantle);
		mMantle.setTextureOffset(75, 26).addCuboid(-6.0F, 0.0F, -4.5F, 12.0F, 10.0F, 9.0F, 0.0F, false);
		
		bipedLeftArm = new ModelPart(this);
		bipedLeftArm.setPivot(5.5F, -15.7F, 1.0F);
		bipedLeftArm.setTextureOffset(58, 18).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F, 0.0F, false);
		
		ModelPart lMantle = new ModelPart(this);
		lMantle.setPivot(-0.1F, -0.3F, -0.5F);
		bipedLeftArm.addChild(lMantle);
		setRotation(lMantle, 0.0F, 0.0F, -0.1309F);
		lMantle.setTextureOffset(69, 45).addCuboid(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F, 0.0F, false);
		
		bipedRightArm = new ModelPart(this);
		bipedRightArm.setPivot(-5.5F, -15.7F, 1.0F);
		bipedRightArm.setTextureOffset(58, 18).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F, 0.0F, true);
		
		ModelPart rMantle = new ModelPart(this);
		rMantle.setPivot(0.1F, -0.3F, -0.5F);
		bipedRightArm.addChild(rMantle);
		setRotation(rMantle, 0.0F, 0.0F, 0.1309F);
		rMantle.setTextureOffset(69, 45).addCuboid(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F, 0.0F, true);
		
		bipedLeftLeg = new ModelPart(this);
		bipedLeftLeg.setPivot(2.5F, -0.2F, 0.6F);
		bipedLeftLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, -0.2F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.0F, 9.5F, -0.7F);
		bipedLeftLeg.addChild(lLeg02);
		setRotation(lLeg02, 0.733F, 0.0F, 0.0873F);
		lLeg02.setTextureOffset(0, 34).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, -0.3F, false);
		
		ModelPart lLeg03 = new ModelPart(this);
		lLeg03.setPivot(0.0F, 7.1F, 0.1F);
		lLeg02.addChild(lLeg03);
		setRotation(lLeg03, -0.4538F, 0.0F, 0.0F);
		lLeg03.setTextureOffset(0, 47).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, -0.15F, false);
		
		ModelPart lHoofClaw01a = new ModelPart(this);
		lHoofClaw01a.setPivot(0.7F, 8.4F, -1.4F);
		lLeg03.addChild(lHoofClaw01a);
		setRotation(lHoofClaw01a, 0.0524F, -0.1047F, 0.0F);
		lHoofClaw01a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw01b = new ModelPart(this);
		lHoofClaw01b.setPivot(0.0F, 0.0F, -0.8F);
		lHoofClaw01a.addChild(lHoofClaw01b);
		setRotation(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw01b.setTextureOffset(0, 59).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart lHoofClaw02a = new ModelPart(this);
		lHoofClaw02a.setPivot(-0.6F, 8.4F, -1.4F);
		lLeg03.addChild(lHoofClaw02a);
		setRotation(lHoofClaw02a, 0.0524F, 0.0524F, 0.0F);
		lHoofClaw02a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw02b = new ModelPart(this);
		lHoofClaw02b.setPivot(0.0F, 0.0F, -0.8F);
		lHoofClaw02a.addChild(lHoofClaw02b);
		setRotation(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw02b.setTextureOffset(0, 59).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		bipedRightLeg = new ModelPart(this);
		bipedRightLeg.setPivot(-2.5F, -0.2F, 0.6F);
		bipedRightLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, -0.2F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(0.0F, 9.5F, -0.7F);
		bipedRightLeg.addChild(rLeg02);
		setRotation(rLeg02, 0.733F, 0.0F, -0.0873F);
		rLeg02.setTextureOffset(0, 34).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, -0.3F, true);
		
		ModelPart rLeg03 = new ModelPart(this);
		rLeg03.setPivot(0.0F, 7.1F, 0.1F);
		rLeg02.addChild(rLeg03);
		setRotation(rLeg03, -0.4538F, 0.0F, 0.0F);
		rLeg03.setTextureOffset(0, 47).addCuboid(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, -0.15F, true);
		
		ModelPart rHoofClaw01a = new ModelPart(this);
		rHoofClaw01a.setPivot(-0.7F, 8.4F, -1.4F);
		rLeg03.addChild(rHoofClaw01a);
		setRotation(rHoofClaw01a, 0.0524F, 0.1047F, 0.0F);
		rHoofClaw01a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw01b = new ModelPart(this);
		rHoofClaw01b.setPivot(0.0F, 0.0F, -0.8F);
		rHoofClaw01a.addChild(rHoofClaw01b);
		setRotation(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw01b.setTextureOffset(0, 59).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rHoofClaw02a = new ModelPart(this);
		rHoofClaw02a.setPivot(0.6F, 8.4F, -1.4F);
		rLeg03.addChild(rHoofClaw02a);
		setRotation(rHoofClaw02a, 0.0524F, -0.0524F, 0.0F);
		rHoofClaw02a.setTextureOffset(9, 60).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw02b = new ModelPart(this);
		rHoofClaw02b.setPivot(0.0F, 0.0F, -0.8F);
		rHoofClaw02a.addChild(rHoofClaw02b);
		setRotation(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw02b.setTextureOffset(0, 59).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		neck00 = new ModelPart(this);
		neck00.setPivot(0.0F, -17.5F, -0.1F);
		neck00.setTextureOffset(51, 0).addCuboid(-3.5F, -1.7F, -3.0F, 7.0F, 3.0F, 6.0F, 0.0F, false);
		
		ModelPart neck01 = new ModelPart(this);
		neck01.setPivot(0.0F, -1.2206F, -0.584F);
		neck00.addChild(neck01);
		setRotation(neck01, 0.2182F, 0.0F, 0.0F);
		neck01.setTextureOffset(28, 0).addCuboid(-3.11F, -3.0F, -2.5F, 6.0F, 3.0F, 5.0F, 0.0F, false);
		
		ModelPart head = new ModelPart(this);
		head.setPivot(0.0F, -2.8F, 0.2F);
		neck01.addChild(head);
		setRotation(head, -0.1396F, 0.0F, 0.0F);
		head.setTextureOffset(0, 0).addCuboid(-3.0F, -4.8F, -3.6F, 6.0F, 5.0F, 6.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -3.2F, -4.1F);
		head.addChild(snout);
		setRotation(snout, 0.2793F, 0.0F, 0.0F);
		snout.setTextureOffset(78, 0).addCuboid(-2.0F, -1.35F, -3.9F, 4.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, -0.5F, -3.9F);
		head.addChild(lowerJaw);
		lowerJaw.setTextureOffset(97, 10).addCuboid(-2.0F, -0.4F, -3.6F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(2.3F, -3.4F, 0.9F);
		head.addChild(lEar);
		setRotation(lEar, 0.1745F, -0.3491F, -0.4538F);
		lEar.setTextureOffset(20, 0).addCuboid(0.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(-2.3F, -3.4F, 0.9F);
		head.addChild(rEar);
		setRotation(rEar, 0.1745F, 0.3491F, 0.4538F);
		rEar.setTextureOffset(20, 0).addCuboid(-3.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler01 = new ModelPart(this);
		lAntler01.setPivot(1.5F, -4.6F, -0.6F);
		head.addChild(lAntler01);
		setRotation(lAntler01, -0.3491F, 0.0F, 0.2793F);
		lAntler01.setTextureOffset(107, 0).addCuboid(-1.0F, -2.3F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lAntler02a = new ModelPart(this);
		lAntler02a.setPivot(-0.2F, -1.7F, -0.3F);
		lAntler01.addChild(lAntler02a);
		setRotation(lAntler02a, 0.7854F, 0.5236F, 0.1396F);
		lAntler02a.setTextureOffset(107, 0).addCuboid(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart lAntler02b = new ModelPart(this);
		lAntler02b.setPivot(0.0F, 0.0F, 0.0F);
		lAntler02a.addChild(lAntler02b);
		lAntler02b.setTextureOffset(107, 0).addCuboid(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart lAntler03 = new ModelPart(this);
		lAntler03.setPivot(0.2F, 0.0F, 5.4F);
		lAntler02b.addChild(lAntler03);
		setRotation(lAntler03, 0.6109F, -0.2618F, 0.0F);
		lAntler03.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart lAntler04 = new ModelPart(this);
		lAntler04.setPivot(0.0F, 0.0F, 4.8F);
		lAntler03.addChild(lAntler04);
		setRotation(lAntler04, -0.5236F, 0.0F, 0.0F);
		lAntler04.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart lAntler04b = new ModelPart(this);
		lAntler04b.setPivot(0.0F, 0.0F, 4.8F);
		lAntler04.addChild(lAntler04b);
		setRotation(lAntler04b, 0.3491F, -0.2618F, 0.0F);
		lAntler04b.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart lAntler07a = new ModelPart(this);
		lAntler07a.setPivot(0.0F, 0.1F, 1.0F);
		lAntler03.addChild(lAntler07a);
		setRotation(lAntler07a, 0.8727F, 0.0F, -0.5934F);
		lAntler07a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler07b = new ModelPart(this);
		lAntler07b.setPivot(0.0F, 2.9F, 0.0F);
		lAntler07a.addChild(lAntler07b);
		setRotation(lAntler07b, 0.2793F, 0.0F, 0.0F);
		lAntler07b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler08a = new ModelPart(this);
		lAntler08a.setPivot(0.0F, 0.1F, 1.2F);
		lAntler03.addChild(lAntler08a);
		setRotation(lAntler08a, 0.6981F, 0.0F, 0.3491F);
		lAntler08a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler08b = new ModelPart(this);
		lAntler08b.setPivot(0.0F, 2.9F, 0.0F);
		lAntler08a.addChild(lAntler08b);
		setRotation(lAntler08b, 0.2793F, 0.0F, 0.3491F);
		lAntler08b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler09a = new ModelPart(this);
		lAntler09a.setPivot(0.0F, -0.4F, -1.0F);
		lAntler03.addChild(lAntler09a);
		setRotation(lAntler09a, 0.6109F, -0.1222F, 0.4363F);
		lAntler09a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler09b = new ModelPart(this);
		lAntler09b.setPivot(0.0F, 4.8F, 0.0F);
		lAntler09a.addChild(lAntler09b);
		setRotation(lAntler09b, 0.2793F, 0.0F, 0.3491F);
		lAntler09b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05a = new ModelPart(this);
		lAntler05a.setPivot(0.0F, 0.4F, 4.8F);
		lAntler02a.addChild(lAntler05a);
		setRotation(lAntler05a, -0.5236F, 0.0F, 0.3665F);
		lAntler05a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05b = new ModelPart(this);
		lAntler05b.setPivot(0.0F, 0.0F, 0.0F);
		lAntler05a.addChild(lAntler05b);
		lAntler05b.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05c = new ModelPart(this);
		lAntler05c.setPivot(-0.1F, -2.9F, -0.2F);
		lAntler05b.addChild(lAntler05c);
		setRotation(lAntler05c, 0.0F, 0.0F, -0.1745F);
		lAntler05c.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler05d = new ModelPart(this);
		lAntler05d.setPivot(0.1F, -3.1F, 0.0F);
		lAntler05c.addChild(lAntler05d);
		setRotation(lAntler05d, 0.0F, 0.0F, -0.3491F);
		lAntler05d.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler10a = new ModelPart(this);
		lAntler10a.setPivot(0.4F, -0.4F, 0.7F);
		lAntler02a.addChild(lAntler10a);
		setRotation(lAntler10a, 0.0F, 0.0F, 0.3665F);
		lAntler10a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler10b = new ModelPart(this);
		lAntler10b.setPivot(0.1F, -3.1F, 0.0F);
		lAntler10a.addChild(lAntler10b);
		setRotation(lAntler10b, 0.0F, 0.0F, -0.3142F);
		lAntler10b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler11a = new ModelPart(this);
		lAntler11a.setPivot(0.4F, 0.0F, -0.2F);
		lAntler02a.addChild(lAntler11a);
		setRotation(lAntler11a, 0.4363F, 0.0F, 0.4363F);
		lAntler11a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lAntler11b = new ModelPart(this);
		lAntler11b.setPivot(0.1F, -2.8F, 0.0F);
		lAntler11a.addChild(lAntler11b);
		setRotation(lAntler11b, 0.0F, 0.0F, -0.3142F);
		lAntler11b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler01 = new ModelPart(this);
		rAntler01.setPivot(-1.5F, -4.7F, -0.6F);
		head.addChild(rAntler01);
		setRotation(rAntler01, -0.3491F, 0.0F, -0.2793F);
		rAntler01.setTextureOffset(107, 0).addCuboid(-1.0F, -2.2F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rAntler02a = new ModelPart(this);
		rAntler02a.setPivot(0.2F, -1.7F, -0.3F);
		rAntler01.addChild(rAntler02a);
		setRotation(rAntler02a, 0.7854F, -0.5236F, -0.1396F);
		rAntler02a.setTextureOffset(107, 0).addCuboid(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart rAntler02b = new ModelPart(this);
		rAntler02b.setPivot(0.0F, 0.0F, 0.0F);
		rAntler02a.addChild(rAntler02b);
		rAntler02b.setTextureOffset(107, 0).addCuboid(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		
		ModelPart rAntler03 = new ModelPart(this);
		rAntler03.setPivot(-0.2F, 0.0F, 5.4F);
		rAntler02b.addChild(rAntler03);
		setRotation(rAntler03, 0.6109F, 0.2618F, 0.0F);
		rAntler03.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart rAntler04 = new ModelPart(this);
		rAntler04.setPivot(0.0F, 0.0F, 4.8F);
		rAntler03.addChild(rAntler04);
		setRotation(rAntler04, -0.5236F, 0.0F, 0.0F);
		rAntler04.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		
		ModelPart rAntler04b = new ModelPart(this);
		rAntler04b.setPivot(0.0F, 0.0F, 4.8F);
		rAntler04.addChild(rAntler04b);
		setRotation(rAntler04b, 0.3491F, 0.2618F, 0.0F);
		rAntler04b.setTextureOffset(107, 0).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rAntler07a = new ModelPart(this);
		rAntler07a.setPivot(0.0F, 0.1F, 1.0F);
		rAntler03.addChild(rAntler07a);
		setRotation(rAntler07a, 0.8727F, 0.0F, 0.5934F);
		rAntler07a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler07b = new ModelPart(this);
		rAntler07b.setPivot(0.0F, 2.9F, 0.0F);
		rAntler07a.addChild(rAntler07b);
		setRotation(rAntler07b, 0.2793F, 0.0F, 0.0F);
		rAntler07b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler08a = new ModelPart(this);
		rAntler08a.setPivot(0.0F, 0.1F, 1.2F);
		rAntler03.addChild(rAntler08a);
		setRotation(rAntler08a, 0.6981F, 0.0F, -0.3491F);
		rAntler08a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler08b = new ModelPart(this);
		rAntler08b.setPivot(0.0F, 2.9F, 0.0F);
		rAntler08a.addChild(rAntler08b);
		setRotation(rAntler08b, 0.2793F, 0.0F, -0.3491F);
		rAntler08b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler09a = new ModelPart(this);
		rAntler09a.setPivot(0.0F, -0.4F, -1.0F);
		rAntler03.addChild(rAntler09a);
		setRotation(rAntler09a, 0.6109F, 0.1222F, -0.4363F);
		rAntler09a.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler09b = new ModelPart(this);
		rAntler09b.setPivot(0.0F, 4.8F, 0.0F);
		rAntler09a.addChild(rAntler09b);
		setRotation(rAntler09b, 0.2793F, 0.0F, -0.3491F);
		rAntler09b.setTextureOffset(107, 0).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05a = new ModelPart(this);
		rAntler05a.setPivot(0.0F, 0.4F, 4.8F);
		rAntler02a.addChild(rAntler05a);
		setRotation(rAntler05a, -0.5236F, 0.0F, -0.3665F);
		rAntler05a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05b = new ModelPart(this);
		rAntler05b.setPivot(0.0F, 0.0F, 0.0F);
		rAntler05a.addChild(rAntler05b);
		rAntler05b.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05c = new ModelPart(this);
		rAntler05c.setPivot(0.1F, -2.9F, -0.2F);
		rAntler05b.addChild(rAntler05c);
		setRotation(rAntler05c, 0.0F, 0.0F, 0.1745F);
		rAntler05c.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler05d = new ModelPart(this);
		rAntler05d.setPivot(-0.1F, -3.1F, 0.0F);
		rAntler05c.addChild(rAntler05d);
		setRotation(rAntler05d, 0.0F, 0.0F, 0.3491F);
		rAntler05d.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler10a = new ModelPart(this);
		rAntler10a.setPivot(-0.4F, -0.4F, 0.7F);
		rAntler02a.addChild(rAntler10a);
		setRotation(rAntler10a, 0.0F, 0.0F, -0.3665F);
		rAntler10a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler10b = new ModelPart(this);
		rAntler10b.setPivot(-0.1F, -3.1F, 0.0F);
		rAntler10a.addChild(rAntler10b);
		setRotation(rAntler10b, 0.0F, 0.0F, 0.3142F);
		rAntler10b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler11a = new ModelPart(this);
		rAntler11a.setPivot(-0.4F, 0.0F, -0.2F);
		rAntler02a.addChild(rAntler11a);
		setRotation(rAntler11a, 0.4363F, 0.0F, -0.4363F);
		rAntler11a.setTextureOffset(107, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rAntler11b = new ModelPart(this);
		rAntler11b.setPivot(0.1F, -2.8F, 0.0F);
		rAntler11a.addChild(rAntler11b);
		setRotation(rAntler11b, 0.0F, 0.0F, 0.3142F);
		rAntler11b.setTextureOffset(107, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart upperJaw00 = new ModelPart(this);
		upperJaw00.setPivot(0.0F, -2.0F, -4.1F);
		head.addChild(upperJaw00);
		upperJaw00.setTextureOffset(76, 8).addCuboid(-2.5F, -0.7F, -3.7F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart neckFur01 = new ModelPart(this);
		neckFur01.setPivot(0.0F, -1.75F, -1.9F);
		neck00.addChild(neckFur01);
		setRotation(neckFur01, -0.6109F, 0.0F, 0.0F);
		neckFur01.setTextureOffset(79, 17).addCuboid(-2.0F, -1.0F, -1.8F, 4.0F, 5.0F, 2.0F, 0.1F, false);
		
		ModelPart neckFur02 = new ModelPart(this);
		neckFur02.setPivot(0.0F, 0.5F, -2.9F);
		neck00.addChild(neckFur02);
		setRotation(neckFur02, -0.1309F, 0.0F, 0.0F);
		neckFur02.setTextureOffset(110, 16).addCuboid(-3.0F, -1.0F, -1.9F, 6.0F, 8.0F, 2.0F, 0.15F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		entity.setStackInHand(Hand.MAIN_HAND, HORNED_SPEAR);
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(neck00, super.head);
		copyRotation(chest, super.torso);
		copyRotation(bipedLeftArm, super.leftArm);
		bipedLeftArm.roll -= 0.1309f;
		copyRotation(bipedRightArm, super.rightArm);
		bipedRightArm.roll += 0.1309f;
		copyRotation(bipedLeftLeg, super.leftLeg);
		bipedLeftLeg.pitch /= 2;
		bipedLeftLeg.pitch -= 0.2793f;
		bipedLeftLeg.roll -= 0.1047f;
		copyRotation(bipedRightLeg, super.rightLeg);
		bipedRightLeg.pitch /= 2;
		bipedRightLeg.pitch -= 0.2793f;
		bipedRightLeg.roll += 0.1047f;
		moss01.pitch = Math.min(bipedLeftLeg.pitch, bipedRightLeg.pitch);
		mossL.pitch = moss01.pitch;
		mossR.pitch = moss01.pitch;
		moss02.pitch = Math.max(bipedLeftLeg.pitch, bipedRightLeg.pitch) + 1 / 3f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck00.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		chest.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? bipedLeftArm : bipedRightArm) : super.getArm(arm);
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
