package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
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
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BaphometEntityModel<T extends BaphometEntity> extends BipedEntityModel<T> {
	private static final ItemStack CADUCEUS = new ItemStack(BWObjects.CADUCEUS);
	
	private final ModelPart body;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	private final ModelPart rWing01;
	private final ModelPart lWing01;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart head;
	
	private boolean realArm = false;
	
	public BaphometEntityModel() {
		super(1, 0, 128, 64);
		body = new ModelPart(this);
		body.setPivot(0.0F, -12.7F, 0.0F);
		body.setTextureOffset(20, 19).addCuboid(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F, 0.0F, false);
		
		ModelPart boobLeft = new ModelPart(this);
		boobLeft.setPivot(1.6F, 1.9F, -0.8F);
		body.addChild(boobLeft);
		setRotation(boobLeft, -0.6981F, -0.1745F, 0.0F);
		boobLeft.setTextureOffset(18, 52).addCuboid(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F, 0.0F, true);
		
		ModelPart boobRight = new ModelPart(this);
		boobRight.setPivot(-1.6F, 1.9F, -0.8F);
		body.addChild(boobRight);
		setRotation(boobRight, -0.6981F, 0.1745F, 0.0F);
		boobRight.setTextureOffset(18, 52).addCuboid(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F, 0.0F, false);
		
		ModelPart stomach = new ModelPart(this);
		stomach.setPivot(0.0F, 6.0F, 0.0F);
		body.addChild(stomach);
		stomach.setTextureOffset(19, 31).addCuboid(-3.5F, 0.0F, -2.5F, 7.0F, 6.0F, 5.0F, 0.0F, false);
		stomach.setTextureOffset(90, 48).addCuboid(-4.5F, 3.7F, -3.0F, 9.0F, 3.0F, 2.0F, 0.0F, false);
		stomach.setTextureOffset(108, 18).addCuboid(-4.0F, 1.7F, -3.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart hips = new ModelPart(this);
		hips.setPivot(0.0F, 6.0F, 0.0F);
		stomach.addChild(hips);
		hips.setTextureOffset(16, 43).addCuboid(-4.0F, 0.0F, -2.5F, 8.0F, 3.0F, 5.0F, 0.0F, false);
		
		frontLoincloth = new ModelPart(this);
		frontLoincloth.setPivot(0.0F, 0.0F, -2.0F);
		hips.addChild(frontLoincloth);
		frontLoincloth.setTextureOffset(65, 0).addCuboid(-5.0F, 0.4698F, -0.329F, 10.0F, 10.0F, 2.0F, 0.0F, false);
		
		backLoincloth = new ModelPart(this);
		backLoincloth.setPivot(0.0F, 1.1F, 1.8F);
		hips.addChild(backLoincloth);
		backLoincloth.setTextureOffset(91, 0).addCuboid(-4.5F, 0.0F, -1.0F, 9.0F, 10.0F, 2.0F, 0.0F, false);
		
		rWing01 = new ModelPart(this);
		rWing01.setPivot(-1.8F, 2.8F, 1.4F);
		body.addChild(rWing01);
		setRotation(rWing01, 0.3491F, -0.6458F, 0.0F);
		rWing01.setTextureOffset(57, 40).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
		
		ModelPart rWing02 = new ModelPart(this);
		rWing02.setPivot(-0.1F, 0.2F, 5.6F);
		rWing01.addChild(rWing02);
		setRotation(rWing02, 0.5236F, -0.1396F, 0.0F);
		rWing02.setTextureOffset(43, 41).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);
		
		ModelPart rWing03 = new ModelPart(this);
		rWing03.setPivot(-0.1F, 0.4F, 8.0F);
		rWing02.addChild(rWing03);
		setRotation(rWing03, -0.1745F, 0.0F, 0.0F);
		rWing03.setTextureOffset(42, 53).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
		
		ModelPart rWing04 = new ModelPart(this);
		rWing04.setPivot(0.0F, 8.6F, 0.2F);
		rWing03.addChild(rWing04);
		setRotation(rWing04, -0.4712F, 0.0F, 0.0F);
		rWing04.setTextureOffset(51, 52).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, true);
		
		ModelPart rWing05 = new ModelPart(this);
		rWing05.setPivot(0.0F, 2.0F, 0.0F);
		rWing04.addChild(rWing05);
		rWing05.setTextureOffset(61, 5).addCuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F, 0.0F, true);
		
		ModelPart rFeathers02 = new ModelPart(this);
		rFeathers02.setPivot(0.1F, 2.3F, 0.0F);
		rWing03.addChild(rFeathers02);
		setRotation(rFeathers02, -0.4363F, 0.0F, 0.0F);
		rFeathers02.setTextureOffset(90, 13).addCuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F, 0.0F, true);
		
		ModelPart rFeathers01 = new ModelPart(this);
		rFeathers01.setPivot(-0.2F, 0.0F, 0.0F);
		rWing02.addChild(rFeathers01);
		setRotation(rFeathers01, -0.1745F, 0.0F, 0.0F);
		rFeathers01.setTextureOffset(65, 31).addCuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F, 0.0F, true);
		
		lWing01 = new ModelPart(this);
		lWing01.setPivot(1.8F, 2.8F, 1.4F);
		body.addChild(lWing01);
		setRotation(lWing01, 0.3491F, 0.6458F, 0.0F);
		lWing01.setTextureOffset(57, 40).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
		
		ModelPart lWing02 = new ModelPart(this);
		lWing02.setPivot(0.1F, 0.2F, 5.6F);
		lWing01.addChild(lWing02);
		setRotation(lWing02, 0.5236F, 0.1396F, 0.0F);
		lWing02.setTextureOffset(43, 41).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);
		
		ModelPart lWing03 = new ModelPart(this);
		lWing03.setPivot(0.1F, 0.4F, 8.0F);
		lWing02.addChild(lWing03);
		setRotation(lWing03, -0.1745F, 0.0F, 0.0F);
		lWing03.setTextureOffset(42, 53).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
		
		ModelPart lWing04 = new ModelPart(this);
		lWing04.setPivot(0.0F, 8.6F, 0.2F);
		lWing03.addChild(lWing04);
		setRotation(lWing04, -0.4712F, 0.0F, 0.0F);
		lWing04.setTextureOffset(51, 52).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, true);
		
		ModelPart lWing05 = new ModelPart(this);
		lWing05.setPivot(0.0F, 2.0F, 0.0F);
		lWing04.addChild(lWing05);
		lWing05.setTextureOffset(61, 5).addCuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F, 0.0F, true);
		
		ModelPart lFeathers02 = new ModelPart(this);
		lFeathers02.setPivot(-0.1F, 2.3F, 0.0F);
		lWing03.addChild(lFeathers02);
		setRotation(lFeathers02, -0.4363F, 0.0F, 0.0F);
		lFeathers02.setTextureOffset(90, 13).addCuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F, 0.0F, true);
		
		ModelPart lFeathers01 = new ModelPart(this);
		lFeathers01.setPivot(0.2F, 0.0F, 0.0F);
		lWing02.addChild(lFeathers01);
		setRotation(lFeathers01, -0.1745F, 0.0F, 0.0F);
		lFeathers01.setTextureOffset(65, 31).addCuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F, 0.0F, true);
		
		BipedLeftArm = new ModelPart(this);
		BipedLeftArm.setPivot(5.0F, -10.8F, 0.0F);
		setRotation(BipedLeftArm, 0.0F, 0.0F, -0.1F);
		BipedLeftArm.setTextureOffset(46, 19).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, false);
		
		BipedRightArm = new ModelPart(this);
		BipedRightArm.setPivot(-5.0F, -10.8F, 0.0F);
		setRotation(BipedRightArm, 0.0F, 0.0F, 0.1F);
		BipedRightArm.setTextureOffset(46, 19).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, true);
		
		BipedLeftLeg = new ModelPart(this);
		BipedLeftLeg.setPivot(2.6F, 2.1F, 0.3F);
		BipedLeftLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F, 0.0F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.2F, 7.6F, -1.3F);
		BipedLeftLeg.addChild(lLeg02);
		setRotation(lLeg02, 0.7679F, -0.1396F, -0.0436F);
		lLeg02.setTextureOffset(0, 32).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, false);
		
		ModelPart lLeg03 = new ModelPart(this);
		lLeg03.setPivot(0.0F, 6.0F, 0.2F);
		lLeg02.addChild(lLeg03);
		setRotation(lLeg03, -0.4887F, 0.0F, 0.1047F);
		lLeg03.setTextureOffset(0, 44).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, false);
		
		ModelPart lHoofClaw01a = new ModelPart(this);
		lHoofClaw01a.setPivot(0.5F, 9.5F, -1.3F);
		lLeg03.addChild(lHoofClaw01a);
		setRotation(lHoofClaw01a, 0.0F, -0.1396F, -0.0349F);
		lHoofClaw01a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw01b = new ModelPart(this);
		lHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
		lHoofClaw01a.addChild(lHoofClaw01b);
		setRotation(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw01b.setTextureOffset(0, 57).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart lHoofClaw02a = new ModelPart(this);
		lHoofClaw02a.setPivot(-0.5F, 9.5F, -1.3F);
		lLeg03.addChild(lHoofClaw02a);
		setRotation(lHoofClaw02a, 0.0F, 0.1396F, 0.0349F);
		lHoofClaw02a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw02b = new ModelPart(this);
		lHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
		lHoofClaw02a.addChild(lHoofClaw02b);
		setRotation(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw02b.setTextureOffset(0, 57).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		BipedRightLeg = new ModelPart(this);
		BipedRightLeg.setPivot(-2.4F, 2.1F, 0.3F);
		BipedRightLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F, 0.0F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(-0.2F, 7.6F, -1.3F);
		BipedRightLeg.addChild(rLeg02);
		setRotation(rLeg02, 0.7679F, 0.1396F, 0.0436F);
		rLeg02.setTextureOffset(0, 32).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, true);
		
		ModelPart rLeg03 = new ModelPart(this);
		rLeg03.setPivot(0.0F, 6.0F, 0.2F);
		rLeg02.addChild(rLeg03);
		setRotation(rLeg03, -0.4887F, 0.0F, -0.1047F);
		rLeg03.setTextureOffset(0, 44).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, true);
		
		ModelPart rHoofClaw01a = new ModelPart(this);
		rHoofClaw01a.setPivot(-0.5F, 9.5F, -1.3F);
		rLeg03.addChild(rHoofClaw01a);
		setRotation(rHoofClaw01a, 0.0F, 0.1396F, 0.0349F);
		rHoofClaw01a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw01b = new ModelPart(this);
		rHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
		rHoofClaw01a.addChild(rHoofClaw01b);
		setRotation(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw01b.setTextureOffset(0, 57).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rHoofClaw02a = new ModelPart(this);
		rHoofClaw02a.setPivot(0.5F, 9.5F, -1.3F);
		rLeg03.addChild(rHoofClaw02a);
		setRotation(rHoofClaw02a, 0.0F, -0.1396F, -0.0349F);
		rHoofClaw02a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw02b = new ModelPart(this);
		rHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
		rHoofClaw02a.addChild(rHoofClaw02b);
		setRotation(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw02b.setTextureOffset(0, 57).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, -12.7F, 1.0F);
		head.setTextureOffset(1, 2).addCuboid(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, 0.0F, false);
		
		ModelPart rHorn01 = new ModelPart(this);
		rHorn01.setPivot(-2.9F, -7.2F, -0.2F);
		head.addChild(rHorn01);
		setRotation(rHorn01, -0.1745F, -0.1396F, -0.4014F);
		rHorn01.setTextureOffset(23, 0).addCuboid(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn02 = new ModelPart(this);
		rHorn02.setPivot(0.0F, -2.2F, 0.0F);
		rHorn01.addChild(rHorn02);
		setRotation(rHorn02, -0.1745F, 0.0F, -0.2618F);
		rHorn02.setTextureOffset(0, 0).addCuboid(-0.4F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.3F, true);
		
		ModelPart rHorn03 = new ModelPart(this);
		rHorn03.setPivot(0.0F, -2.7F, 0.0F);
		rHorn02.addChild(rHorn03);
		setRotation(rHorn03, -0.1047F, 0.0F, -0.1745F);
		rHorn03.setTextureOffset(0, 4).addCuboid(-0.3F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, 0.15F, true);
		
		ModelPart rHorn04 = new ModelPart(this);
		rHorn04.setPivot(0.0F, -2.8F, 0.0F);
		rHorn03.addChild(rHorn04);
		setRotation(rHorn04, 0.0524F, 0.0F, -0.1396F);
		rHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn05 = new ModelPart(this);
		rHorn05.setPivot(0.0F, -2.7F, 0.0F);
		rHorn04.addChild(rHorn05);
		setRotation(rHorn05, 0.0524F, 0.0F, 0.1396F);
		rHorn05.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn01 = new ModelPart(this);
		lHorn01.setPivot(2.9F, -7.2F, -0.2F);
		head.addChild(lHorn01);
		setRotation(lHorn01, -0.1745F, 0.1396F, 0.4014F);
		lHorn01.setTextureOffset(23, 0).addCuboid(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart lHorn02 = new ModelPart(this);
		lHorn02.setPivot(0.0F, -2.2F, 0.0F);
		lHorn01.addChild(lHorn02);
		setRotation(lHorn02, -0.1745F, 0.0F, 0.2618F);
		lHorn02.setTextureOffset(0, 0).addCuboid(-0.6F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.3F, false);
		
		ModelPart lHorn03 = new ModelPart(this);
		lHorn03.setPivot(0.0F, -2.7F, 0.0F);
		lHorn02.addChild(lHorn03);
		setRotation(lHorn03, -0.1047F, 0.0F, 0.1745F);
		lHorn03.setTextureOffset(0, 4).addCuboid(-0.7F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, 0.15F, false);
		
		ModelPart lHorn04 = new ModelPart(this);
		lHorn04.setPivot(0.0F, -2.8F, 0.0F);
		lHorn03.addChild(lHorn04);
		setRotation(lHorn04, 0.0524F, 0.0F, 0.1396F);
		lHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lHorn05 = new ModelPart(this);
		lHorn05.setPivot(0.0F, -2.7F, 0.0F);
		lHorn04.addChild(lHorn05);
		setRotation(lHorn05, 0.0524F, 0.0F, -0.1396F);
		lHorn05.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -4.6F, -2.5F);
		head.addChild(snout);
		setRotation(snout, 0.5236F, 0.0F, 0.0F);
		snout.setTextureOffset(29, 2).addCuboid(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F, 0.0F, false);
		
		ModelPart jawUpper00 = new ModelPart(this);
		jawUpper00.setPivot(0.0F, -2.5F, -2.2F);
		head.addChild(jawUpper00);
		setRotation(jawUpper00, 0.0698F, 0.0F, 0.0F);
		jawUpper00.setTextureOffset(43, 11).addCuboid(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart jawLower = new ModelPart(this);
		jawLower.setPivot(0.0F, -1.0F, -3.0F);
		head.addChild(jawLower);
		setRotation(jawLower, -0.0349F, 0.0F, 0.0F);
		jawLower.setTextureOffset(48, 5).addCuboid(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		
		ModelPart beard = new ModelPart(this);
		beard.setPivot(0.0F, 0.3F, -2.4F);
		jawLower.addChild(beard);
		setRotation(beard, -0.0349F, 0.0F, 0.0F);
		beard.setTextureOffset(18, 59).addCuboid(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(2.6F, -6.0F, 0.8F);
		head.addChild(lEar);
		setRotation(lEar, -0.3491F, 0.0F, 0.3142F);
		lEar.setTextureOffset(48, 0).addCuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(-2.6F, -6.0F, 0.8F);
		head.addChild(rEar);
		setRotation(rEar, -0.3491F, 0.0F, -0.3142F);
		rEar.setTextureOffset(48, 0).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart torch00 = new ModelPart(this);
		torch00.setPivot(0.0F, -6.6F, -1.2F);
		head.addChild(torch00);
		setRotation(torch00, 0.0F, -0.7854F, 0.0F);
		torch00.setTextureOffset(117, 0).addCuboid(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart torch01a = new ModelPart(this);
		torch01a.setPivot(0.0F, -2.0F, 0.0F);
		torch00.addChild(torch01a);
		torch01a.setTextureOffset(117, 6).addCuboid(-0.8F, -3.8F, -0.8F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart torch01b = new ModelPart(this);
		torch01b.setPivot(0.0F, 0.0F, 0.0F);
		torch01a.addChild(torch01b);
		torch01b.setTextureOffset(117, 6).addCuboid(-0.2F, -3.8F, -0.8F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart torch01c = new ModelPart(this);
		torch01c.setPivot(0.0F, 0.0F, 0.0F);
		torch01a.addChild(torch01c);
		torch01c.setTextureOffset(117, 6).addCuboid(-0.8F, -3.8F, -0.2F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart torch01d = new ModelPart(this);
		torch01d.setPivot(0.0F, 0.0F, 0.0F);
		torch01a.addChild(torch01d);
		torch01d.setTextureOffset(117, 6).addCuboid(-0.2F, -3.8F, -0.2F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart torch02a = new ModelPart(this);
		torch02a.setPivot(0.0F, -3.6F, 0.0F);
		torch01a.addChild(torch02a);
		torch02a.setTextureOffset(115, 12).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		
		ModelPart torch03a = new ModelPart(this);
		torch03a.setPivot(-0.6F, -1.5F, -0.6F);
		torch02a.addChild(torch03a);
		setRotation(torch03a, 0.2443F, -0.1396F, -0.2793F);
		torch03a.setTextureOffset(122, 6).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart torch03b = new ModelPart(this);
		torch03b.setPivot(0.6F, -1.5F, -0.6F);
		torch02a.addChild(torch03b);
		setRotation(torch03b, 0.2443F, 0.2094F, 0.2793F);
		torch03b.setTextureOffset(122, 6).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart torch03d = new ModelPart(this);
		torch03d.setPivot(-0.6F, -1.5F, 0.6F);
		torch02a.addChild(torch03d);
		setRotation(torch03d, -0.2443F, 0.2094F, -0.2793F);
		torch03d.setTextureOffset(122, 10).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart torch03c = new ModelPart(this);
		torch03c.setPivot(0.6F, -1.5F, 0.6F);
		torch02a.addChild(torch03c);
		setRotation(torch03c, -0.2443F, -0.1396F, 0.2793F);
		torch03c.setTextureOffset(122, 10).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart flame00 = new ModelPart(this);
		flame00.setPivot(0.0F, -1.6F, 0.0F);
		torch02a.addChild(flame00);
		flame00.setTextureOffset(89, 10).addCuboid(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart flame01 = new ModelPart(this);
		flame01.setPivot(0.0F, -1.6F, 0.0F);
		torch02a.addChild(flame01);
		setRotation(flame01, 0.0F, -1.5708F, 0.0F);
		flame01.setTextureOffset(89, 17).addCuboid(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart lCheekFur = new ModelPart(this);
		lCheekFur.setPivot(2.4F, -2.4F, -0.3F);
		head.addChild(lCheekFur);
		setRotation(lCheekFur, -0.6981F, -0.3491F, 0.576F);
		lCheekFur.setTextureOffset(31, 54).addCuboid(0.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, 0.0F, false);
		
		ModelPart rCheekFur = new ModelPart(this);
		rCheekFur.setPivot(-2.4F, -2.4F, -0.3F);
		head.addChild(rCheekFur);
		setRotation(rCheekFur, -0.6981F, 0.3491F, -0.576F);
		rCheekFur.setTextureOffset(31, 54).addCuboid(-5.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, 0.0F, true);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		entity.setStackInHand(Hand.MAIN_HAND, CADUCEUS);
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
		BipedLeftLeg.pitch -= 0.2793f;
		BipedLeftLeg.roll -= 0.096f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.pitch /= 2;
		BipedRightLeg.pitch -= 0.2793f;
		BipedRightLeg.roll += 0.096f;
		lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yaw = -lWing01.yaw;
		frontLoincloth.pitch = Math.min(BipedLeftLeg.pitch, BipedRightLeg.pitch) + 1 / 3f - 0.3491f;
		backLoincloth.pitch = Math.max(BipedLeftLeg.pitch, BipedRightLeg.pitch) + 1 / 3.75f;
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
