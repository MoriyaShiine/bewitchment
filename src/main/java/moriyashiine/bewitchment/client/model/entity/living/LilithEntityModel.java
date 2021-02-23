package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class LilithEntityModel<T extends LilithEntity> extends BipedEntityModel<T> {
	private final ModelPart body;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	private final ModelPart lWing01;
	private final ModelPart rWing01;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart head;
	
	private boolean realArm = false;
	
	public LilithEntityModel() {
		super(1, 0, 128, 64);
		body = new ModelPart(this);
		body.setPivot(0.0F, -12.0F, 1.2F);
		body.setTextureOffset(20, 16).addCuboid(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F, 0.0F, false);
		
		ModelPart stomach = new ModelPart(this);
		stomach.setPivot(0.0F, 6.0F, 0.0F);
		body.addChild(stomach);
		stomach.setTextureOffset(19, 28).addCuboid(-3.5F, 0.0F, -2.5F, 7.0F, 6.0F, 5.0F, 0.0F, false);
		
		ModelPart hips = new ModelPart(this);
		hips.setPivot(0.0F, 5.9F, 0.0F);
		stomach.addChild(hips);
		hips.setTextureOffset(19, 39).addCuboid(-4.0F, 0.0F, -2.5F, 8.0F, 3.0F, 5.0F, 0.0F, false);
		
		frontLoincloth = new ModelPart(this);
		frontLoincloth.setPivot(0.0F, 0.0F, -2.7F);
		hips.addChild(frontLoincloth);
		setRotationAngle(frontLoincloth, -0.0698F, 0.0F, 0.0F);
		frontLoincloth.setTextureOffset(73, 45).addCuboid(-5.6F, 0.0F, 0.0F, 10.0F, 14.0F, 5.0F, 0.0F, false);
		
		backLoincloth = new ModelPart(this);
		backLoincloth.setPivot(0.0F, 0.1F, 1.8F);
		hips.addChild(backLoincloth);
		backLoincloth.setTextureOffset(103, 45).addCuboid(-5.5F, 0.0F, -1.0F, 9.0F, 12.0F, 2.0F, 0.0F, false);
		
		ModelPart boobs = new ModelPart(this);
		boobs.setPivot(0.0F, 1.9F, -0.8F);
		body.addChild(boobs);
		setRotationAngle(boobs, -0.6981F, 0.0F, 0.0F);
		boobs.setTextureOffset(19, 48).addCuboid(-3.5F, 0.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, false);
		
		ModelPart boobWrap = new ModelPart(this);
		boobWrap.setPivot(0.0F, 3.7F, -1.9F);
		boobs.addChild(boobWrap);
		setRotationAngle(boobWrap, -0.8727F, 0.0F, 0.0F);
		boobWrap.setTextureOffset(20, 59).addCuboid(-3.5F, 0.27F, 0.15F, 7.0F, 0.0F, 4.0F, 0.0F, false);
		
		lWing01 = new ModelPart(this);
		lWing01.setPivot(1.8F, 2.8F, 1.4F);
		body.addChild(lWing01);
		setRotationAngle(lWing01, 0.3491F, 0.6458F, 0.0F);
		lWing01.setTextureOffset(55, 40).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
		
		ModelPart lWing02 = new ModelPart(this);
		lWing02.setPivot(0.1F, 0.2F, 5.6F);
		lWing01.addChild(lWing02);
		setRotationAngle(lWing02, 0.5236F, 0.1396F, 0.0F);
		lWing02.setTextureOffset(41, 41).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);
		
		ModelPart lWing03 = new ModelPart(this);
		lWing03.setPivot(0.1F, 0.4F, 8.0F);
		lWing02.addChild(lWing03);
		setRotationAngle(lWing03, -0.1745F, 0.0F, 0.0F);
		lWing03.setTextureOffset(42, 53).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
		
		ModelPart lWing04 = new ModelPart(this);
		lWing04.setPivot(0.0F, 8.6F, 0.2F);
		lWing03.addChild(lWing04);
		setRotationAngle(lWing04, -0.4712F, 0.0F, 0.0F);
		lWing04.setTextureOffset(51, 52).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, true);
		
		ModelPart lFeathers03 = new ModelPart(this);
		lFeathers03.setPivot(0.0F, 2.0F, 0.0F);
		lWing04.addChild(lFeathers03);
		lFeathers03.setTextureOffset(60, 0).addCuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F, 0.0F, true);
		
		ModelPart lFeathers02 = new ModelPart(this);
		lFeathers02.setPivot(-0.1F, 2.3F, 0.0F);
		lWing03.addChild(lFeathers02);
		setRotationAngle(lFeathers02, -0.4363F, 0.0F, 0.0F);
		lFeathers02.setTextureOffset(90, 0).addCuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F, 0.0F, true);
		
		ModelPart lFeathers01 = new ModelPart(this);
		lFeathers01.setPivot(0.2F, 0.0F, 0.0F);
		lWing02.addChild(lFeathers01);
		setRotationAngle(lFeathers01, -0.1745F, 0.0F, 0.0F);
		lFeathers01.setTextureOffset(65, 13).addCuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F, 0.0F, true);
		
		rWing01 = new ModelPart(this);
		rWing01.setPivot(-1.8F, 2.8F, 1.4F);
		body.addChild(rWing01);
		setRotationAngle(rWing01, 0.3491F, -0.6458F, 0.0F);
		rWing01.setTextureOffset(55, 40).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
		
		ModelPart rWing02 = new ModelPart(this);
		rWing02.setPivot(-0.1F, 0.2F, 5.6F);
		rWing01.addChild(rWing02);
		setRotationAngle(rWing02, 0.5236F, -0.1396F, 0.0F);
		rWing02.setTextureOffset(41, 41).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);
		
		ModelPart rWing03 = new ModelPart(this);
		rWing03.setPivot(-0.1F, 0.4F, 8.0F);
		rWing02.addChild(rWing03);
		setRotationAngle(rWing03, -0.1745F, 0.0F, 0.0F);
		rWing03.setTextureOffset(42, 53).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
		
		ModelPart rWing04 = new ModelPart(this);
		rWing04.setPivot(0.0F, 8.6F, 0.2F);
		rWing03.addChild(rWing04);
		setRotationAngle(rWing04, -0.4712F, 0.0F, 0.0F);
		rWing04.setTextureOffset(51, 52).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, true);
		
		ModelPart rFeathers03 = new ModelPart(this);
		rFeathers03.setPivot(0.0F, 2.0F, 0.0F);
		rWing04.addChild(rFeathers03);
		rFeathers03.setTextureOffset(60, 0).addCuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F, 0.0F, true);
		
		ModelPart rFeathers02 = new ModelPart(this);
		rFeathers02.setPivot(0.1F, 2.3F, 0.0F);
		rWing03.addChild(rFeathers02);
		setRotationAngle(rFeathers02, -0.4363F, 0.0F, 0.0F);
		rFeathers02.setTextureOffset(90, 0).addCuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F, 0.0F, true);
		
		ModelPart rFeathers01 = new ModelPart(this);
		rFeathers01.setPivot(-0.2F, 0.0F, 0.0F);
		rWing02.addChild(rFeathers01);
		setRotationAngle(rFeathers01, -0.1745F, 0.0F, 0.0F);
		rFeathers01.setTextureOffset(65, 13).addCuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F, 0.0F, true);
		
		bipedLeftArm = new ModelPart(this);
		bipedLeftArm.setPivot(4.5F, -10.1F, 1.2F);
		bipedLeftArm.setTextureOffset(46, 16).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, false);
		
		ModelPart lClaw01 = new ModelPart(this);
		lClaw01.setPivot(0.6F, 11.2F, -1.6F);
		bipedLeftArm.addChild(lClaw01);
		setRotationAngle(lClaw01, 0.0F, 0.0F, 0.1745F);
		lClaw01.setTextureOffset(11, 53).addCuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw02 = new ModelPart(this);
		lClaw02.setPivot(0.6F, 11.8F, -0.5F);
		bipedLeftArm.addChild(lClaw02);
		setRotationAngle(lClaw02, 0.0F, 0.0F, 0.1222F);
		lClaw02.setTextureOffset(11, 53).addCuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw03 = new ModelPart(this);
		lClaw03.setPivot(0.6F, 11.8F, 0.6F);
		bipedLeftArm.addChild(lClaw03);
		setRotationAngle(lClaw03, 0.0F, 0.0F, 0.1222F);
		lClaw03.setTextureOffset(11, 53).addCuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw04 = new ModelPart(this);
		lClaw04.setPivot(0.6F, 11.2F, 1.6F);
		bipedLeftArm.addChild(lClaw04);
		setRotationAngle(lClaw04, 0.0F, 0.0F, 0.1745F);
		lClaw04.setTextureOffset(11, 53).addCuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		
		bipedRightArm = new ModelPart(this);
		bipedRightArm.setPivot(-4.5F, -10.1F, 1.2F);
		bipedRightArm.setTextureOffset(46, 16).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, true);
		
		ModelPart rClaw01 = new ModelPart(this);
		rClaw01.setPivot(-0.6F, 11.2F, -1.6F);
		bipedRightArm.addChild(rClaw01);
		setRotationAngle(rClaw01, 0.0F, 0.0F, -0.1745F);
		rClaw01.setTextureOffset(11, 53).addCuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw02 = new ModelPart(this);
		rClaw02.setPivot(-0.6F, 11.8F, -0.5F);
		bipedRightArm.addChild(rClaw02);
		setRotationAngle(rClaw02, 0.0F, 0.0F, -0.1222F);
		rClaw02.setTextureOffset(11, 53).addCuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw03 = new ModelPart(this);
		rClaw03.setPivot(-0.6F, 11.8F, 0.6F);
		bipedRightArm.addChild(rClaw03);
		setRotationAngle(rClaw03, 0.0F, 0.0F, -0.1222F);
		rClaw03.setTextureOffset(11, 53).addCuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw04 = new ModelPart(this);
		rClaw04.setPivot(-0.6F, 11.2F, 1.6F);
		bipedRightArm.addChild(rClaw04);
		setRotationAngle(rClaw04, 0.0F, 0.0F, -0.1745F);
		rClaw04.setTextureOffset(11, 53).addCuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, true);
		
		bipedLeftLeg = new ModelPart(this);
		bipedLeftLeg.setPivot(2.3F, 1.9F, 1.2F);
		bipedLeftLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.0F, 11.5F, -0.2F);
		bipedLeftLeg.addChild(lLeg02);
		setRotationAngle(lLeg02, 0.0873F, 0.0F, 0.0349F);
		lLeg02.setTextureOffset(0, 34).addCuboid(-2.0F, -1.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		ModelPart lTalon01 = new ModelPart(this);
		lTalon01.setPivot(1.1F, 9.7F, -1.4F);
		lLeg02.addChild(lTalon01);
		setRotationAngle(lTalon01, 0.0349F, -0.1745F, 0.0F);
		lTalon01.setTextureOffset(0, 52).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		ModelPart lTalon02 = new ModelPart(this);
		lTalon02.setPivot(0.0F, 9.7F, -1.7F);
		lLeg02.addChild(lTalon02);
		setRotationAngle(lTalon02, 0.0349F, 0.0F, 0.0F);
		lTalon02.setTextureOffset(0, 52).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		ModelPart lTalon03 = new ModelPart(this);
		lTalon03.setPivot(-1.1F, 9.7F, -1.4F);
		lLeg02.addChild(lTalon03);
		setRotationAngle(lTalon03, 0.0349F, 0.1745F, 0.0F);
		lTalon03.setTextureOffset(0, 52).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		ModelPart lTalon04 = new ModelPart(this);
		lTalon04.setPivot(0.0F, 9.7F, 1.6F);
		lLeg02.addChild(lTalon04);
		setRotationAngle(lTalon04, -0.0349F, 0.0F, 0.0F);
		lTalon04.setTextureOffset(0, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		bipedRightLeg = new ModelPart(this);
		bipedRightLeg.setPivot(-2.3F, 1.9F, 1.2F);
		bipedRightLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(0.0F, 11.5F, -0.2F);
		bipedRightLeg.addChild(rLeg02);
		setRotationAngle(rLeg02, 0.0873F, 0.0F, -0.0349F);
		rLeg02.setTextureOffset(0, 34).addCuboid(-2.0F, -1.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		
		ModelPart rTalon01 = new ModelPart(this);
		rTalon01.setPivot(-1.1F, 9.7F, -1.4F);
		rLeg02.addChild(rTalon01);
		setRotationAngle(rTalon01, 0.0349F, 0.1745F, 0.0F);
		rTalon01.setTextureOffset(0, 52).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);
		
		ModelPart rTalon02 = new ModelPart(this);
		rTalon02.setPivot(0.0F, 9.7F, -1.7F);
		rLeg02.addChild(rTalon02);
		setRotationAngle(rTalon02, 0.0349F, 0.0F, 0.0F);
		rTalon02.setTextureOffset(0, 52).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);
		
		ModelPart rTalon03 = new ModelPart(this);
		rTalon03.setPivot(1.1F, 9.7F, -1.4F);
		rLeg02.addChild(rTalon03);
		setRotationAngle(rTalon03, 0.0349F, -0.1745F, 0.0F);
		rTalon03.setTextureOffset(0, 52).addCuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);
		
		ModelPart rTalon04 = new ModelPart(this);
		rTalon04.setPivot(0.0F, 9.7F, 1.6F);
		rLeg02.addChild(rTalon04);
		setRotationAngle(rTalon04, -0.0349F, 0.0F, 0.0F);
		rTalon04.setTextureOffset(0, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, -12.0F, 1.2F);
		head.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		
		ModelPart lHorn01 = new ModelPart(this);
		lHorn01.setPivot(2.3F, -6.9F, -1.3F);
		head.addChild(lHorn01);
		setRotationAngle(lHorn01, -0.2618F, 0.0F, 0.5236F);
		lHorn01.setTextureOffset(109, 0).addCuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, true);
		
		ModelPart lHorn02 = new ModelPart(this);
		lHorn02.setPivot(0.0F, -3.5F, 0.0F);
		lHorn01.addChild(lHorn02);
		setRotationAngle(lHorn02, -0.4538F, 0.0F, -0.1745F);
		lHorn02.setTextureOffset(109, 7).addCuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lHorn03 = new ModelPart(this);
		lHorn03.setPivot(0.0F, -2.5F, 0.0F);
		lHorn02.addChild(lHorn03);
		setRotationAngle(lHorn03, -0.4363F, 0.0F, 0.0F);
		lHorn03.setTextureOffset(118, 7).addCuboid(-0.99F, -3.0F, -0.99F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart lHorn04a = new ModelPart(this);
		lHorn04a.setPivot(0.2F, -2.7F, -0.3F);
		lHorn03.addChild(lHorn04a);
		setRotationAngle(lHorn04a, -0.6981F, 0.0F, -0.4014F);
		lHorn04a.setTextureOffset(0, 0).addCuboid(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn04b = new ModelPart(this);
		lHorn04b.setPivot(0.0F, 0.0F, 0.0F);
		lHorn04a.addChild(lHorn04b);
		lHorn04b.setTextureOffset(0, 0).addCuboid(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn04c = new ModelPart(this);
		lHorn04c.setPivot(0.0F, 0.0F, 0.0F);
		lHorn04a.addChild(lHorn04c);
		lHorn04c.setTextureOffset(0, 0).addCuboid(-0.7F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn04d = new ModelPart(this);
		lHorn04d.setPivot(0.0F, 0.0F, 0.0F);
		lHorn04a.addChild(lHorn04d);
		lHorn04d.setTextureOffset(0, 0).addCuboid(-0.3F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn05 = new ModelPart(this);
		lHorn05.setPivot(0.0F, -2.8F, 0.0F);
		lHorn04d.addChild(lHorn05);
		setRotationAngle(lHorn05, -0.4363F, 0.0F, 0.2269F);
		lHorn05.setTextureOffset(0, 4).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn06 = new ModelPart(this);
		lHorn06.setPivot(0.0F, -2.7F, 0.0F);
		lHorn05.addChild(lHorn06);
		setRotationAngle(lHorn06, 0.4887F, 0.0F, 0.2269F);
		lHorn06.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn01 = new ModelPart(this);
		rHorn01.setPivot(-2.3F, -6.9F, -1.3F);
		head.addChild(rHorn01);
		setRotationAngle(rHorn01, -0.2618F, 0.0F, -0.5236F);
		rHorn01.setTextureOffset(109, 0).addCuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, true);
		
		ModelPart rHorn02 = new ModelPart(this);
		rHorn02.setPivot(0.0F, -3.5F, 0.0F);
		rHorn01.addChild(rHorn02);
		setRotationAngle(rHorn02, -0.4538F, 0.0F, 0.1745F);
		rHorn02.setTextureOffset(109, 7).addCuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn03 = new ModelPart(this);
		rHorn03.setPivot(0.0F, -2.5F, 0.0F);
		rHorn02.addChild(rHorn03);
		setRotationAngle(rHorn03, -0.4363F, 0.0F, 0.0F);
		rHorn03.setTextureOffset(118, 7).addCuboid(-1.01F, -3.0F, -0.99F, 2.0F, 3.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn04a = new ModelPart(this);
		rHorn04a.setPivot(-0.2F, -2.7F, 0.3F);
		rHorn03.addChild(rHorn04a);
		setRotationAngle(rHorn04a, -0.6981F, 0.0F, 0.4014F);
		rHorn04a.setTextureOffset(0, 0).addCuboid(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn04b = new ModelPart(this);
		rHorn04b.setPivot(0.0F, 0.0F, 0.0F);
		rHorn04a.addChild(rHorn04b);
		rHorn04b.setTextureOffset(0, 0).addCuboid(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn04c = new ModelPart(this);
		rHorn04c.setPivot(0.0F, 0.0F, 0.0F);
		rHorn04a.addChild(rHorn04c);
		rHorn04c.setTextureOffset(0, 0).addCuboid(-0.7F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn04d = new ModelPart(this);
		rHorn04d.setPivot(0.0F, 0.0F, 0.0F);
		rHorn04a.addChild(rHorn04d);
		rHorn04d.setTextureOffset(0, 0).addCuboid(-0.3F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn05 = new ModelPart(this);
		rHorn05.setPivot(0.0F, -2.8F, 0.0F);
		rHorn04d.addChild(rHorn05);
		setRotationAngle(rHorn05, -0.4363F, 0.0F, -0.2269F);
		rHorn05.setTextureOffset(0, 4).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn06 = new ModelPart(this);
		rHorn06.setPivot(0.0F, -2.7F, 0.0F);
		rHorn05.addChild(rHorn06);
		setRotationAngle(rHorn06, 0.4887F, 0.0F, -0.2269F);
		rHorn06.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart lHair = new ModelPart(this);
		lHair.setPivot(-2.4F, -6.3F, 1.1F);
		head.addChild(lHair);
		setRotationAngle(lHair, 0.3491F, 0.0F, 0.3142F);
		lHair.setTextureOffset(45, 0).addCuboid(-2.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, false);
		
		ModelPart rHair = new ModelPart(this);
		rHair.setPivot(2.4F, -6.3F, 1.1F);
		head.addChild(rHair);
		setRotationAngle(rHair, 0.3491F, 0.0F, -0.3142F);
		rHair.setTextureOffset(45, 0).addCuboid(0.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, false);
		
		ModelPart crown00 = new ModelPart(this);
		crown00.setPivot(0.0F, -8.0F, -3.5F);
		head.addChild(crown00);
		crown00.setTextureOffset(27, 0).addCuboid(-3.0F, -2.5F, 0.0F, 6.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart crownL01 = new ModelPart(this);
		crownL01.setPivot(2.3F, -1.0F, 0.5F);
		crown00.addChild(crownL01);
		setRotationAngle(crownL01, 0.0F, 0.0F, -0.6981F);
		crownL01.setTextureOffset(27, 0).addCuboid(-0.2F, -1.5F, -0.49F, 2.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart crownL02 = new ModelPart(this);
		crownL02.setPivot(1.5F, -0.1F, 0.1F);
		crownL01.addChild(crownL02);
		setRotationAngle(crownL02, 0.0F, 0.0F, -0.4189F);
		crownL02.setTextureOffset(27, 0).addCuboid(-0.7F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart crownL03 = new ModelPart(this);
		crownL03.setPivot(2.2F, -0.2F, 0.0F);
		crownL02.addChild(crownL03);
		setRotationAngle(crownL03, 0.0F, 0.0F, -0.6109F);
		crownL03.setTextureOffset(27, 0).addCuboid(-0.7F, -1.0F, -0.49F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart crownL04 = new ModelPart(this);
		crownL04.setPivot(2.0F, -0.3F, 0.0F);
		crownL03.addChild(crownL04);
		setRotationAngle(crownL04, 0.0F, 0.0F, -0.4363F);
		crownL04.setTextureOffset(27, 0).addCuboid(-0.1F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, true);
		
		ModelPart crownL05 = new ModelPart(this);
		crownL05.setPivot(0.1F, 0.6F, 0.0F);
		crownL04.addChild(crownL05);
		setRotationAngle(crownL05, 0.0F, 0.0F, -0.2094F);
		crownL05.setTextureOffset(27, 0).addCuboid(-0.7F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, true);
		
		ModelPart crownR01 = new ModelPart(this);
		crownR01.setPivot(-2.3F, -1.0F, 0.5F);
		crown00.addChild(crownR01);
		setRotationAngle(crownR01, 0.0F, 0.0F, 0.6981F);
		crownR01.setTextureOffset(27, 0).addCuboid(-1.8F, -1.5F, -0.49F, 2.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart crownR02 = new ModelPart(this);
		crownR02.setPivot(-1.5F, -0.1F, 0.1F);
		crownR01.addChild(crownR02);
		setRotationAngle(crownR02, 0.0F, 0.0F, 0.4189F);
		crownR02.setTextureOffset(27, 0).addCuboid(-2.3F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart crownR03 = new ModelPart(this);
		crownR03.setPivot(-2.2F, -0.2F, 0.0F);
		crownR02.addChild(crownR03);
		setRotationAngle(crownR03, 0.0F, 0.0F, 0.6109F);
		crownR03.setTextureOffset(27, 0).addCuboid(-2.3F, -1.0F, -0.49F, 3.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart crownR04 = new ModelPart(this);
		crownR04.setPivot(-2.0F, -0.3F, 0.0F);
		crownR03.addChild(crownR04);
		setRotationAngle(crownR04, 0.0F, 0.0F, 0.4363F);
		crownR04.setTextureOffset(27, 0).addCuboid(-2.9F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, true);
		
		ModelPart crownR05 = new ModelPart(this);
		crownR05.setPivot(-0.1F, 0.6F, 0.0F);
		crownR04.addChild(crownR05);
		setRotationAngle(crownR05, 0.0F, 0.0F, 0.2094F);
		crownR05.setTextureOffset(27, 0).addCuboid(-2.3F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, true);
		
		ModelPart HAIR = new ModelPart(this);
		HAIR.setPivot(0.0F, -8.0F, 1.8F);
		head.addChild(HAIR);
		HAIR.setTextureOffset(56, 0).addCuboid(-4.0F, 6.0F, 1.5F, 8.0F, 11.0F, 1.0F, 0.1F, false);
		
		ModelPart base_r1 = new ModelPart(this);
		base_r1.setPivot(0.0F, 3.0F, -3.0F);
		HAIR.addChild(base_r1);
		setRotationAngle(base_r1, 0.4363F, 0.7854F, 0.3054F);
		base_r1.setTextureOffset(85, 0).addCuboid(-5.0F, -2.0F, -1.0F, 6.0F, 10.0F, 6.0F, 0.1F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(head, super.head);
		copyRotation(body, super.torso);
		copyRotation(bipedLeftArm, super.leftArm);
		bipedLeftArm.roll -= 0.1f;
		copyRotation(bipedRightArm, super.rightArm);
		bipedRightArm.roll += 0.1f;
		copyRotation(bipedLeftLeg, super.leftLeg);
		bipedLeftLeg.pitch /= 2;
		bipedLeftLeg.pitch -= 0.0698f;
		bipedLeftLeg.roll -= 0.0349f;
		copyRotation(bipedRightLeg, super.rightLeg);
		bipedRightLeg.pitch /= 2;
		bipedRightLeg.pitch -= 0.0698f;
		bipedRightLeg.roll += 0.0349f;
		lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yaw = -lWing01.yaw;
		frontLoincloth.pitch = Math.min(bipedLeftLeg.pitch, bipedRightLeg.pitch);
		backLoincloth.pitch = Math.max(bipedLeftLeg.pitch, bipedRightLeg.pitch);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? bipedLeftArm : bipedRightArm) : super.getArm(arm);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
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
