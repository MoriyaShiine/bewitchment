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
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BaphometEntityModel<T extends BaphometEntity> extends BipedEntityModel<T> {
	private static final ItemStack CADUCEUS = new ItemStack(BWObjects.CADUCEUS);
	
	private final ModelPart body;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart head;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart rWing01;
	private final ModelPart lWing01;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	
	public BaphometEntityModel() {
		super(1);
		textureWidth = 128;
		textureHeight = 64;
		body = new ModelPart(this);
		body.setPivot(0.0F, -12.7F, 0.0F);
		body.setTextureOffset(20, 19).addCuboid(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F, 0.0F, false);
		
		ModelPart boobLeft = new ModelPart(this);
		boobLeft.setPivot(1.6F, 1.9F, -0.8F);
		body.addChild(boobLeft);
		setRotationAngle(boobLeft, -0.6981F, -0.1745F, 0.0F);
		boobLeft.setTextureOffset(18, 52).addCuboid(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F, 0.0F, true);
		
		ModelPart boobRight = new ModelPart(this);
		boobRight.setPivot(-1.6F, 1.9F, -0.8F);
		body.addChild(boobRight);
		setRotationAngle(boobRight, -0.6981F, 0.1745F, 0.0F);
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
		
		leftLeg = new ModelPart(this);
		leftLeg.setPivot(2.6F, 2.8F, 0.3F);
		hips.addChild(leftLeg);
		setRotationAngle(leftLeg, -0.2793F, 0.0F, -0.096F);
		leftLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F, 0.0F, false);
		
		ModelPart lLeg02 = new ModelPart(this);
		lLeg02.setPivot(0.2F, 7.6F, -1.3F);
		leftLeg.addChild(lLeg02);
		setRotationAngle(lLeg02, 0.7679F, -0.1396F, -0.0436F);
		lLeg02.setTextureOffset(0, 32).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, false);
		
		ModelPart lLeg03 = new ModelPart(this);
		lLeg03.setPivot(0.0F, 6.0F, 0.2F);
		lLeg02.addChild(lLeg03);
		setRotationAngle(lLeg03, -0.4887F, 0.0F, 0.1047F);
		lLeg03.setTextureOffset(0, 44).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, false);
		
		ModelPart lHoofClaw01a = new ModelPart(this);
		lHoofClaw01a.setPivot(0.5F, 9.5F, -1.3F);
		lLeg03.addChild(lHoofClaw01a);
		setRotationAngle(lHoofClaw01a, 0.0F, -0.1396F, -0.0349F);
		lHoofClaw01a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw01b = new ModelPart(this);
		lHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
		lHoofClaw01a.addChild(lHoofClaw01b);
		setRotationAngle(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw01b.setTextureOffset(0, 57).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		ModelPart lHoofClaw02a = new ModelPart(this);
		lHoofClaw02a.setPivot(-0.5F, 9.5F, -1.3F);
		lLeg03.addChild(lHoofClaw02a);
		setRotationAngle(lHoofClaw02a, 0.0F, 0.1396F, 0.0349F);
		lHoofClaw02a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart lHoofClaw02b = new ModelPart(this);
		lHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
		lHoofClaw02a.addChild(lHoofClaw02b);
		setRotationAngle(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		lHoofClaw02b.setTextureOffset(0, 57).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		
		rightLeg = new ModelPart(this);
		rightLeg.setPivot(-2.4F, 2.8F, 0.3F);
		hips.addChild(rightLeg);
		setRotationAngle(rightLeg, -0.2793F, 0.0F, 0.096F);
		rightLeg.setTextureOffset(0, 16).addCuboid(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F, 0.0F, true);
		
		ModelPart rLeg02 = new ModelPart(this);
		rLeg02.setPivot(-0.2F, 7.6F, -1.3F);
		rightLeg.addChild(rLeg02);
		setRotationAngle(rLeg02, 0.7679F, 0.1396F, 0.0436F);
		rLeg02.setTextureOffset(0, 32).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, true);
		
		ModelPart rLeg03 = new ModelPart(this);
		rLeg03.setPivot(0.0F, 6.0F, 0.2F);
		rLeg02.addChild(rLeg03);
		setRotationAngle(rLeg03, -0.4887F, 0.0F, -0.1047F);
		rLeg03.setTextureOffset(0, 44).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, true);
		
		ModelPart rHoofClaw01a = new ModelPart(this);
		rHoofClaw01a.setPivot(-0.5F, 9.5F, -1.3F);
		rLeg03.addChild(rHoofClaw01a);
		setRotationAngle(rHoofClaw01a, 0.0F, 0.1396F, 0.0349F);
		rHoofClaw01a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw01b = new ModelPart(this);
		rHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
		rHoofClaw01a.addChild(rHoofClaw01b);
		setRotationAngle(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw01b.setTextureOffset(0, 57).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		ModelPart rHoofClaw02a = new ModelPart(this);
		rHoofClaw02a.setPivot(0.5F, 9.5F, -1.3F);
		rLeg03.addChild(rHoofClaw02a);
		setRotationAngle(rHoofClaw02a, 0.0F, -0.1396F, -0.0349F);
		rHoofClaw02a.setTextureOffset(9, 58).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart rHoofClaw02b = new ModelPart(this);
		rHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
		rHoofClaw02a.addChild(rHoofClaw02b);
		setRotationAngle(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
		rHoofClaw02b.setTextureOffset(0, 57).addCuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
		
		frontLoincloth = new ModelPart(this);
		frontLoincloth.setPivot(0.0F, 0.0F, -2.0F);
		hips.addChild(frontLoincloth);
		setRotationAngle(frontLoincloth, -0.3491F, 0.0F, 0.0F);
		frontLoincloth.setTextureOffset(65, 0).addCuboid(-5.0F, 0.4698F, -0.329F, 10.0F, 10.0F, 2.0F, 0.0F, false);
		
		backLoincloth = new ModelPart(this);
		backLoincloth.setPivot(0.0F, 1.1F, 1.8F);
		hips.addChild(backLoincloth);
		backLoincloth.setTextureOffset(91, 0).addCuboid(-4.5F, 0.0F, -1.0F, 9.0F, 10.0F, 2.0F, 0.0F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, 1.0F);
		body.addChild(head);
		head.setTextureOffset(1, 2).addCuboid(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, 0.0F, false);
		
		ModelPart rHorn01 = new ModelPart(this);
		rHorn01.setPivot(-2.9F, -7.2F, -0.2F);
		head.addChild(rHorn01);
		setRotationAngle(rHorn01, -0.1745F, -0.1396F, -0.4014F);
		rHorn01.setTextureOffset(23, 0).addCuboid(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
		
		ModelPart rHorn02 = new ModelPart(this);
		rHorn02.setPivot(0.0F, -2.2F, 0.0F);
		rHorn01.addChild(rHorn02);
		setRotationAngle(rHorn02, -0.1745F, 0.0F, -0.2618F);
		rHorn02.setTextureOffset(0, 0).addCuboid(-0.4F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.3F, true);
		
		ModelPart rHorn03 = new ModelPart(this);
		rHorn03.setPivot(0.0F, -2.7F, 0.0F);
		rHorn02.addChild(rHorn03);
		setRotationAngle(rHorn03, -0.1047F, 0.0F, -0.1745F);
		rHorn03.setTextureOffset(0, 4).addCuboid(-0.3F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, 0.15F, true);
		
		ModelPart rHorn04 = new ModelPart(this);
		rHorn04.setPivot(0.0F, -2.8F, 0.0F);
		rHorn03.addChild(rHorn04);
		setRotationAngle(rHorn04, 0.0524F, 0.0F, -0.1396F);
		rHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		
		ModelPart rHorn05 = new ModelPart(this);
		rHorn05.setPivot(0.0F, -2.7F, 0.0F);
		rHorn04.addChild(rHorn05);
		setRotationAngle(rHorn05, 0.0524F, 0.0F, 0.1396F);
		rHorn05.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart lHorn01 = new ModelPart(this);
		lHorn01.setPivot(2.9F, -7.2F, -0.2F);
		head.addChild(lHorn01);
		setRotationAngle(lHorn01, -0.1745F, 0.1396F, 0.4014F);
		lHorn01.setTextureOffset(23, 0).addCuboid(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		
		ModelPart lHorn02 = new ModelPart(this);
		lHorn02.setPivot(0.0F, -2.2F, 0.0F);
		lHorn01.addChild(lHorn02);
		setRotationAngle(lHorn02, -0.1745F, 0.0F, 0.2618F);
		lHorn02.setTextureOffset(0, 0).addCuboid(-0.6F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.3F, false);
		
		ModelPart lHorn03 = new ModelPart(this);
		lHorn03.setPivot(0.0F, -2.7F, 0.0F);
		lHorn02.addChild(lHorn03);
		setRotationAngle(lHorn03, -0.1047F, 0.0F, 0.1745F);
		lHorn03.setTextureOffset(0, 4).addCuboid(-0.7F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, 0.15F, false);
		
		ModelPart lHorn04 = new ModelPart(this);
		lHorn04.setPivot(0.0F, -2.8F, 0.0F);
		lHorn03.addChild(lHorn04);
		setRotationAngle(lHorn04, 0.0524F, 0.0F, 0.1396F);
		lHorn04.setTextureOffset(0, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		
		ModelPart lHorn05 = new ModelPart(this);
		lHorn05.setPivot(0.0F, -2.7F, 0.0F);
		lHorn04.addChild(lHorn05);
		setRotationAngle(lHorn05, 0.0524F, 0.0F, -0.1396F);
		lHorn05.setTextureOffset(0, 0).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -4.6F, -2.5F);
		head.addChild(snout);
		setRotationAngle(snout, 0.5236F, 0.0F, 0.0F);
		snout.setTextureOffset(29, 2).addCuboid(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F, 0.0F, false);
		
		ModelPart jawUpper00 = new ModelPart(this);
		jawUpper00.setPivot(0.0F, -2.5F, -2.2F);
		head.addChild(jawUpper00);
		setRotationAngle(jawUpper00, 0.0698F, 0.0F, 0.0F);
		jawUpper00.setTextureOffset(43, 11).addCuboid(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		
		ModelPart jawLower = new ModelPart(this);
		jawLower.setPivot(0.0F, -1.0F, -3.0F);
		head.addChild(jawLower);
		setRotationAngle(jawLower, -0.0349F, 0.0F, 0.0F);
		jawLower.setTextureOffset(48, 5).addCuboid(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		
		ModelPart beard = new ModelPart(this);
		beard.setPivot(0.0F, 0.3F, -2.4F);
		jawLower.addChild(beard);
		setRotationAngle(beard, -0.0349F, 0.0F, 0.0F);
		beard.setTextureOffset(18, 59).addCuboid(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
		
		ModelPart lEar = new ModelPart(this);
		lEar.setPivot(2.6F, -6.0F, 0.8F);
		head.addChild(lEar);
		setRotationAngle(lEar, -0.3491F, 0.0F, 0.3142F);
		lEar.setTextureOffset(48, 0).addCuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		
		ModelPart rEar = new ModelPart(this);
		rEar.setPivot(-2.6F, -6.0F, 0.8F);
		head.addChild(rEar);
		setRotationAngle(rEar, -0.3491F, 0.0F, -0.3142F);
		rEar.setTextureOffset(48, 0).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, true);
		
		ModelPart torch00 = new ModelPart(this);
		torch00.setPivot(0.0F, -6.6F, -1.2F);
		head.addChild(torch00);
		setRotationAngle(torch00, 0.0F, -0.7854F, 0.0F);
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
		setRotationAngle(torch03a, 0.2443F, -0.1396F, -0.2793F);
		torch03a.setTextureOffset(122, 6).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart torch03b = new ModelPart(this);
		torch03b.setPivot(0.6F, -1.5F, -0.6F);
		torch02a.addChild(torch03b);
		setRotationAngle(torch03b, 0.2443F, 0.2094F, 0.2793F);
		torch03b.setTextureOffset(122, 6).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart torch03d = new ModelPart(this);
		torch03d.setPivot(-0.6F, -1.5F, 0.6F);
		torch02a.addChild(torch03d);
		setRotationAngle(torch03d, -0.2443F, 0.2094F, -0.2793F);
		torch03d.setTextureOffset(122, 10).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
		
		ModelPart torch03c = new ModelPart(this);
		torch03c.setPivot(0.6F, -1.5F, 0.6F);
		torch02a.addChild(torch03c);
		setRotationAngle(torch03c, -0.2443F, -0.1396F, 0.2793F);
		torch03c.setTextureOffset(122, 10).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		ModelPart flame00 = new ModelPart(this);
		flame00.setPivot(0.0F, -1.6F, 0.0F);
		torch02a.addChild(flame00);
		flame00.setTextureOffset(89, 10).addCuboid(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart flame01 = new ModelPart(this);
		flame01.setPivot(0.0F, -1.6F, 0.0F);
		torch02a.addChild(flame01);
		setRotationAngle(flame01, 0.0F, -1.5708F, 0.0F);
		flame01.setTextureOffset(89, 17).addCuboid(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, false);
		
		ModelPart lCheekFur = new ModelPart(this);
		lCheekFur.setPivot(2.4F, -2.4F, -0.3F);
		head.addChild(lCheekFur);
		setRotationAngle(lCheekFur, -0.6981F, -0.3491F, 0.576F);
		lCheekFur.setTextureOffset(31, 54).addCuboid(0.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, 0.0F, false);
		
		ModelPart rCheekFur = new ModelPart(this);
		rCheekFur.setPivot(-2.4F, -2.4F, -0.3F);
		head.addChild(rCheekFur);
		setRotationAngle(rCheekFur, -0.6981F, 0.3491F, -0.576F);
		rCheekFur.setTextureOffset(31, 54).addCuboid(-5.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, 0.0F, true);
		
		leftArm = new ModelPart(this);
		leftArm.setPivot(5.0F, 1.9F, 0.0F);
		body.addChild(leftArm);
		setRotationAngle(leftArm, 0.0F, 0.0F, -0.1F);
		leftArm.setTextureOffset(46, 19).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, false);
		
		rightArm = new ModelPart(this);
		rightArm.setPivot(-5.0F, 1.9F, 0.0F);
		body.addChild(rightArm);
		setRotationAngle(rightArm, 0.0F, 0.0F, 0.1F);
		rightArm.setTextureOffset(46, 19).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F, 0.0F, true);
		
		rWing01 = new ModelPart(this);
		rWing01.setPivot(-1.8F, 2.8F, 1.4F);
		body.addChild(rWing01);
		setRotationAngle(rWing01, 0.3491F, -0.6458F, 0.0F);
		rWing01.setTextureOffset(57, 40).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
		
		ModelPart rWing02 = new ModelPart(this);
		rWing02.setPivot(-0.1F, 0.2F, 5.6F);
		rWing01.addChild(rWing02);
		setRotationAngle(rWing02, 0.5236F, -0.1396F, 0.0F);
		rWing02.setTextureOffset(43, 41).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);
		
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
		
		ModelPart rWing05 = new ModelPart(this);
		rWing05.setPivot(0.0F, 2.0F, 0.0F);
		rWing04.addChild(rWing05);
		rWing05.setTextureOffset(61, 5).addCuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F, 0.0F, true);
		
		ModelPart rFeathers02 = new ModelPart(this);
		rFeathers02.setPivot(0.1F, 2.3F, 0.0F);
		rWing03.addChild(rFeathers02);
		setRotationAngle(rFeathers02, -0.4363F, 0.0F, 0.0F);
		rFeathers02.setTextureOffset(90, 13).addCuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F, 0.0F, true);
		
		ModelPart rFeathers01 = new ModelPart(this);
		rFeathers01.setPivot(-0.2F, 0.0F, 0.0F);
		rWing02.addChild(rFeathers01);
		setRotationAngle(rFeathers01, -0.1745F, 0.0F, 0.0F);
		rFeathers01.setTextureOffset(65, 31).addCuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F, 0.0F, true);
		
		lWing01 = new ModelPart(this);
		lWing01.setPivot(1.8F, 2.8F, 1.4F);
		body.addChild(lWing01);
		setRotationAngle(lWing01, 0.3491F, 0.6458F, 0.0F);
		lWing01.setTextureOffset(57, 40).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
		
		ModelPart lWing02 = new ModelPart(this);
		lWing02.setPivot(0.1F, 0.2F, 5.6F);
		lWing01.addChild(lWing02);
		setRotationAngle(lWing02, 0.5236F, 0.1396F, 0.0F);
		lWing02.setTextureOffset(43, 41).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);
		
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
		
		ModelPart lWing05 = new ModelPart(this);
		lWing05.setPivot(0.0F, 2.0F, 0.0F);
		lWing04.addChild(lWing05);
		lWing05.setTextureOffset(61, 5).addCuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F, 0.0F, true);
		
		ModelPart lFeathers02 = new ModelPart(this);
		lFeathers02.setPivot(-0.1F, 2.3F, 0.0F);
		lWing03.addChild(lFeathers02);
		setRotationAngle(lFeathers02, -0.4363F, 0.0F, 0.0F);
		lFeathers02.setTextureOffset(90, 13).addCuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F, 0.0F, true);
		
		ModelPart lFeathers01 = new ModelPart(this);
		lFeathers01.setPivot(0.2F, 0.0F, 0.0F);
		lWing02.addChild(lFeathers01);
		setRotationAngle(lFeathers01, -0.1745F, 0.0F, 0.0F);
		lFeathers01.setTextureOffset(65, 31).addCuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F, 0.0F, true);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		entity.setStackInHand(Hand.MAIN_HAND, CADUCEUS);
		head.copyPositionAndRotation(super.head);
		leftArm.copyPositionAndRotation(super.leftArm);
		leftArm.roll -= 1 / 8f;
		rightArm.copyPositionAndRotation(super.rightArm);
		rightArm.roll += 1 / 8f;
		rightLeg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * limbDistance - 1 / 3f;
		leftLeg.pitch = -rightLeg.pitch - 2 / 3f;
		lWing01.yaw = MathHelper.cos(animationProgress / 8) / 3 + 1 / 3f;
		rWing01.yaw = -lWing01.yaw;
		frontLoincloth.pitch = Math.min(leftLeg.pitch, rightLeg.pitch) + 1 / 3f - 0.3491f;
		backLoincloth.pitch = Math.max(leftLeg.pitch, rightLeg.pitch) + 1 / 3f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}