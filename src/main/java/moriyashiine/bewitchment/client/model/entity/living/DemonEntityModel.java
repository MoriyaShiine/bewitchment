package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DemonEntityModel<T extends DemonEntity> extends BipedEntityModel<T> {
	private final Model male = new Male();
	private final Model female = new Female();
	
	private Model model;
	
	public DemonEntityModel() {
		super(1);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		model = entity.getDataTracker().get(DemonEntity.MALE) ? male : female;
		model.head.copyPositionAndRotation(super.head);
		model.leftArm.copyPositionAndRotation(super.leftArm);
		model.rightArm.copyPositionAndRotation(super.rightArm);
		model.leftLeg.pitch = MathHelper.cos((float) (limbAngle * 2 / 3 + Math.PI)) * limbDistance - 1 / 3f;
		model.rightLeg.pitch = MathHelper.cos(limbAngle * 2 / 3) * limbDistance - 1 / 3f;
		model.lWing01.yaw = MathHelper.cos(animationProgress / 8) / 3 + 1 / 3f;
		model.rWing01.yaw = MathHelper.cos((float) (animationProgress / 8 + Math.PI)) / 3 - 1 / 3f;
		model.tail01.roll = MathHelper.sin(animationProgress / 8) / 8;
		if (model.loincloth != null) {
			model.loincloth.pitch = Math.min(model.leftLeg.pitch, model.rightLeg.pitch);
		}
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		model.body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
	
	private abstract class Model extends EntityModel<T> {
		protected ModelPart head;
		protected ModelPart body;
		protected ModelPart lWing01;
		protected ModelPart rWing01;
		protected ModelPart leftArm;
		protected ModelPart rightArm;
		protected ModelPart leftLeg;
		protected ModelPart rightLeg;
		protected ModelPart tail01;
		protected ModelPart loincloth;
	}
	
	private class Male extends Model {
		public Male() {
			textureWidth = 64;
			textureHeight = 64;
			body = new ModelPart(this);
			body.setPivot(0.0F, -5.5F, 0.0F);
			body.setTextureOffset(19, 17).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F, 0.0F, false);
			
			tail01 = new ModelPart(this);
			tail01.setPivot(0.0F, 11.2F, 1.3F);
			body.addChild(tail01);
			setRotationAngle(tail01, -0.8727F, 0.0F, 0.0F);
			tail01.setTextureOffset(13, 37).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail02 = new ModelPart(this);
			tail02.setPivot(0.0F, 0.0F, 3.8F);
			tail01.addChild(tail02);
			setRotationAngle(tail02, -0.1396F, 0.0F, 0.0F);
			tail02.setTextureOffset(13, 37).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail03 = new ModelPart(this);
			tail03.setPivot(0.0F, 0.0F, 2.9F);
			tail02.addChild(tail03);
			setRotationAngle(tail03, 0.0698F, 0.0F, 0.0F);
			tail03.setTextureOffset(15, 45).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail04 = new ModelPart(this);
			tail04.setPivot(0.0F, 0.0F, 4.9F);
			tail03.addChild(tail04);
			setRotationAngle(tail04, 0.1396F, 0.0F, 0.0F);
			tail04.setTextureOffset(15, 45).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail05 = new ModelPart(this);
			tail05.setPivot(0.0F, 0.0F, 4.9F);
			tail04.addChild(tail05);
			setRotationAngle(tail05, 0.2269F, 0.0F, 0.0F);
			tail05.setTextureOffset(15, 45).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tailTip01 = new ModelPart(this);
			tailTip01.setPivot(0.0F, 0.0F, 4.5F);
			tail05.addChild(tailTip01);
			setRotationAngle(tailTip01, 0.2618F, 0.0F, 0.0F);
			tailTip01.setTextureOffset(16, 53).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart tailTip02 = new ModelPart(this);
			tailTip02.setPivot(0.0F, 0.1F, 0.8F);
			tailTip01.addChild(tailTip02);
			setRotationAngle(tailTip02, 0.0F, -0.7854F, 0.0F);
			tailTip02.setTextureOffset(15, 58).addCuboid(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			lWing01 = new ModelPart(this);
			lWing01.setPivot(2.5F, 3.2F, 1.4F);
			body.addChild(lWing01);
			setRotationAngle(lWing01, 0.2731F, 0.5236F, 0.0F);
			lWing01.setTextureOffset(26, 35).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart lWing02 = new ModelPart(this);
			lWing02.setPivot(0.1F, 0.2F, 4.3F);
			lWing01.addChild(lWing02);
			setRotationAngle(lWing02, 0.5236F, 0.0F, 0.0F);
			lWing02.setTextureOffset(27, 44).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart lWing03 = new ModelPart(this);
			lWing03.setPivot(0.1F, -0.5F, 7.1F);
			lWing02.addChild(lWing03);
			setRotationAngle(lWing03, 0.2094F, 0.0F, 0.0F);
			lWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart lWing04 = new ModelPart(this);
			lWing04.setPivot(0.0F, 7.7F, 0.0F);
			lWing03.addChild(lWing04);
			setRotationAngle(lWing04, -0.4189F, 0.0F, 0.0F);
			lWing04.setTextureOffset(24, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart lWingMembrane = new ModelPart(this);
			lWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			lWing02.addChild(lWingMembrane);
			setRotationAngle(lWingMembrane, -0.0911F, 0.0F, 0.0F);
			lWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			rWing01 = new ModelPart(this);
			rWing01.setPivot(-2.5F, 3.2F, 1.4F);
			body.addChild(rWing01);
			setRotationAngle(rWing01, 0.2731F, -0.5236F, 0.0F);
			rWing01.setTextureOffset(26, 35).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart rWing02 = new ModelPart(this);
			rWing02.setPivot(-0.1F, 0.2F, 4.3F);
			rWing01.addChild(rWing02);
			setRotationAngle(rWing02, 0.5236F, 0.0F, 0.0F);
			rWing02.setTextureOffset(27, 44).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart rWing03 = new ModelPart(this);
			rWing03.setPivot(-0.1F, -0.5F, 7.1F);
			rWing02.addChild(rWing03);
			setRotationAngle(rWing03, 0.2094F, 0.0F, 0.0F);
			rWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart rWing04 = new ModelPart(this);
			rWing04.setPivot(0.0F, 7.7F, 0.0F);
			rWing03.addChild(rWing04);
			setRotationAngle(rWing04, -0.4189F, 0.0F, 0.0F);
			rWing04.setTextureOffset(24, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart rWingMembrane = new ModelPart(this);
			rWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			rWing02.addChild(rWingMembrane);
			setRotationAngle(rWingMembrane, -0.0911F, 0.0F, 0.0F);
			rWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			head = new ModelPart(this);
			head.setPivot(0.0F, 0.0F, 0.0F);
			body.addChild(head);
			head.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			
			ModelPart rHorn01 = new ModelPart(this);
			rHorn01.setPivot(-2.9F, -7.4F, -1.3F);
			head.addChild(rHorn01);
			setRotationAngle(rHorn01, 0.1047F, 0.0F, -0.4189F);
			rHorn01.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
			
			ModelPart rHorn02 = new ModelPart(this);
			rHorn02.setPivot(0.0F, -1.7F, 0.0F);
			rHorn01.addChild(rHorn02);
			setRotationAngle(rHorn02, -0.1047F, 0.0F, 0.192F);
			rHorn02.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, -0.1F, true);
			
			ModelPart rHorn03 = new ModelPart(this);
			rHorn03.setPivot(0.0F, -1.6F, 0.0F);
			rHorn02.addChild(rHorn03);
			setRotationAngle(rHorn03, -0.1396F, 0.0F, 0.0698F);
			rHorn03.setTextureOffset(35, 5).addCuboid(-0.4F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, 0.15F, true);
			
			ModelPart rHorn04 = new ModelPart(this);
			rHorn04.setPivot(0.0F, -1.7F, 0.0F);
			rHorn03.addChild(rHorn04);
			setRotationAngle(rHorn04, -0.1396F, 0.0F, 0.1047F);
			rHorn04.setTextureOffset(35, 10).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
			
			ModelPart lHorn01 = new ModelPart(this);
			lHorn01.setPivot(2.9F, -7.4F, -1.3F);
			head.addChild(lHorn01);
			setRotationAngle(lHorn01, 0.1047F, 0.0F, 0.4189F);
			lHorn01.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
			
			ModelPart lHorn02 = new ModelPart(this);
			lHorn02.setPivot(0.0F, -1.7F, 0.0F);
			lHorn01.addChild(lHorn02);
			setRotationAngle(lHorn02, -0.1047F, 0.0F, -0.192F);
			lHorn02.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, -0.1F, false);
			
			ModelPart lHorn03 = new ModelPart(this);
			lHorn03.setPivot(0.0F, -1.6F, 0.0F);
			lHorn02.addChild(lHorn03);
			setRotationAngle(lHorn03, -0.1396F, 0.0F, -0.0698F);
			lHorn03.setTextureOffset(35, 5).addCuboid(-0.6F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, 0.15F, false);
			
			ModelPart lHorn04 = new ModelPart(this);
			lHorn04.setPivot(0.0F, -1.7F, 0.0F);
			lHorn03.addChild(lHorn04);
			setRotationAngle(lHorn04, -0.1396F, 0.0F, -0.1047F);
			lHorn04.setTextureOffset(35, 10).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
			
			leftLeg = new ModelPart(this);
			leftLeg.setPivot(2.1F, 12.6F, 0.1F);
			body.addChild(leftLeg);
			setRotationAngle(leftLeg, -0.2618F, 0.0F, -0.1047F);
			leftLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, false);
			
			ModelPart lLeg02 = new ModelPart(this);
			lLeg02.setPivot(0.0F, 5.7F, -0.4F);
			leftLeg.addChild(lLeg02);
			setRotationAngle(lLeg02, 0.6981F, 0.0F, 0.1047F);
			lLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);
			
			ModelPart lLeg03 = new ModelPart(this);
			lLeg03.setPivot(0.0F, 5.2F, 0.2F);
			lLeg02.addChild(lLeg03);
			setRotationAngle(lLeg03, -0.4189F, 0.0F, 0.0F);
			lLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw01a = new ModelPart(this);
			lHoofClaw01a.setPivot(0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw01a);
			setRotationAngle(lHoofClaw01a, 0.1745F, -0.1396F, -0.0349F);
			lHoofClaw01a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw01b = new ModelPart(this);
			lHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw01a.addChild(lHoofClaw01b);
			setRotationAngle(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw01b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw02a = new ModelPart(this);
			lHoofClaw02a.setPivot(-0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw02a);
			setRotationAngle(lHoofClaw02a, 0.1745F, 0.0873F, 0.0349F);
			lHoofClaw02a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw02b = new ModelPart(this);
			lHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw02a.addChild(lHoofClaw02b);
			setRotationAngle(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw02b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			rightLeg = new ModelPart(this);
			rightLeg.setPivot(-2.1F, 12.6F, 0.1F);
			body.addChild(rightLeg);
			setRotationAngle(rightLeg, -0.2618F, 0.0F, 0.1047F);
			rightLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, true);
			
			ModelPart rLeg02 = new ModelPart(this);
			rLeg02.setPivot(0.0F, 5.7F, -0.4F);
			rightLeg.addChild(rLeg02);
			setRotationAngle(rLeg02, 0.6981F, 0.0F, -0.1047F);
			rLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, true);
			
			ModelPart rLeg03 = new ModelPart(this);
			rLeg03.setPivot(0.0F, 5.2F, 0.2F);
			rLeg02.addChild(rLeg03);
			setRotationAngle(rLeg03, -0.4189F, 0.0F, 0.0F);
			rLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw01a = new ModelPart(this);
			rHoofClaw01a.setPivot(-0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw01a);
			setRotationAngle(rHoofClaw01a, 0.1745F, 0.1396F, 0.0349F);
			rHoofClaw01a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw01b = new ModelPart(this);
			rHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw01a.addChild(rHoofClaw01b);
			setRotationAngle(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw01b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw02a = new ModelPart(this);
			rHoofClaw02a.setPivot(0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw02a);
			setRotationAngle(rHoofClaw02a, 0.1745F, -0.0873F, -0.0349F);
			rHoofClaw02a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw02b = new ModelPart(this);
			rHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw02a.addChild(rHoofClaw02b);
			setRotationAngle(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw02b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			leftArm = new ModelPart(this);
			leftArm.setPivot(5.0F, 2.0F, 0.0F);
			body.addChild(leftArm);
			setRotationAngle(leftArm, 0.0F, 0.0F, -0.1F);
			leftArm.setTextureOffset(44, 16).addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);
			
			rightArm = new ModelPart(this);
			rightArm.setPivot(-5.0F, 2.0F, 0.0F);
			body.addChild(rightArm);
			setRotationAngle(rightArm, 0.0F, 0.0F, 0.1F);
			rightArm.setTextureOffset(44, 16).addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, true);
		}
		
		@Override
		public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		
		}
		
		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		
		}
	}
	
	private class Female extends Model {
		public Female() {
			textureWidth = 64;
			textureHeight = 64;
			body = new ModelPart(this);
			body.setPivot(0.0F, -6.2F, 0.0F);
			body.setTextureOffset(19, 17).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, 0.0F, false);
			
			lWing01 = new ModelPart(this);
			lWing01.setPivot(2.5F, 3.2F, 1.4F);
			body.addChild(lWing01);
			setRotationAngle(lWing01, 0.2731F, 0.5236F, 0.0F);
			lWing01.setTextureOffset(50, 52).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart lWing02 = new ModelPart(this);
			lWing02.setPivot(0.1F, 0.2F, 4.3F);
			lWing01.addChild(lWing02);
			setRotationAngle(lWing02, 0.5236F, 0.0F, 0.0F);
			lWing02.setTextureOffset(38, 52).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart lWing03 = new ModelPart(this);
			lWing03.setPivot(0.1F, -0.5F, 7.1F);
			lWing02.addChild(lWing03);
			setRotationAngle(lWing03, 0.2094F, 0.0F, 0.0F);
			lWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart lWing04 = new ModelPart(this);
			lWing04.setPivot(0.0F, 7.7F, 0.0F);
			lWing03.addChild(lWing04);
			setRotationAngle(lWing04, -0.4189F, 0.0F, 0.0F);
			lWing04.setTextureOffset(25, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart lWingMembrane = new ModelPart(this);
			lWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			lWing02.addChild(lWingMembrane);
			setRotationAngle(lWingMembrane, -0.0911F, 0.0F, 0.0F);
			lWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			rWing01 = new ModelPart(this);
			rWing01.setPivot(-2.5F, 3.2F, 1.4F);
			body.addChild(rWing01);
			setRotationAngle(rWing01, 0.2731F, -0.5236F, 0.0F);
			rWing01.setTextureOffset(50, 52).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart rWing02 = new ModelPart(this);
			rWing02.setPivot(-0.1F, 0.2F, 4.3F);
			rWing01.addChild(rWing02);
			setRotationAngle(rWing02, 0.5236F, 0.0F, 0.0F);
			rWing02.setTextureOffset(38, 52).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart rWing03 = new ModelPart(this);
			rWing03.setPivot(-0.1F, -0.5F, 7.1F);
			rWing02.addChild(rWing03);
			setRotationAngle(rWing03, 0.2094F, 0.0F, 0.0F);
			rWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart rWing04 = new ModelPart(this);
			rWing04.setPivot(0.0F, 7.7F, 0.0F);
			rWing03.addChild(rWing04);
			setRotationAngle(rWing04, -0.4189F, 0.0F, 0.0F);
			rWing04.setTextureOffset(25, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart rWingMembrane = new ModelPart(this);
			rWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			rWing02.addChild(rWingMembrane);
			setRotationAngle(rWingMembrane, -0.0911F, 0.0F, 0.0F);
			rWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			ModelPart boobs = new ModelPart(this);
			boobs.setPivot(0.0F, 1.9F, -0.3F);
			body.addChild(boobs);
			setRotationAngle(boobs, -0.6109F, 0.0F, 0.0F);
			boobs.setTextureOffset(0, 57).addCuboid(-3.5F, 0.0F, -2.0F, 7.0F, 4.0F, 3.0F, 0.0F, false);
			
			head = new ModelPart(this);
			head.setPivot(0.0F, 0.0F, 0.0F);
			body.addChild(head);
			head.setTextureOffset(0, 1).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			
			ModelPart rHorn01 = new ModelPart(this);
			rHorn01.setPivot(-2.9F, -7.4F, -0.5F);
			head.addChild(rHorn01);
			setRotationAngle(rHorn01, -0.1745F, -0.1396F, -0.1396F);
			rHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
			
			ModelPart rHorn02a = new ModelPart(this);
			rHorn02a.setPivot(0.0F, -1.7F, 0.0F);
			rHorn01.addChild(rHorn02a);
			setRotationAngle(rHorn02a, -0.1745F, 0.0F, -0.0524F);
			rHorn02a.setTextureOffset(0, 4).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.25F, true);
			
			ModelPart rHorn03a = new ModelPart(this);
			rHorn03a.setPivot(0.0F, -1.6F, 0.0F);
			rHorn02a.addChild(rHorn03a);
			setRotationAngle(rHorn03a, -0.1047F, 0.0F, -0.1047F);
			rHorn03a.setTextureOffset(0, 4).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.15F, true);
			
			ModelPart rHorn04 = new ModelPart(this);
			rHorn04.setPivot(0.0F, -1.7F, 0.0F);
			rHorn03a.addChild(rHorn04);
			setRotationAngle(rHorn04, 0.0524F, 0.0F, -0.1396F);
			rHorn04.setTextureOffset(4, 4).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.1F, true);
			
			ModelPart rHorn05 = new ModelPart(this);
			rHorn05.setPivot(0.0F, -2.7F, 0.0F);
			rHorn04.addChild(rHorn05);
			setRotationAngle(rHorn05, 0.0524F, 0.0F, 0.1396F);
			rHorn05.setTextureOffset(43, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
			
			ModelPart lHorn01 = new ModelPart(this);
			lHorn01.setPivot(2.9F, -7.4F, -0.5F);
			head.addChild(lHorn01);
			setRotationAngle(lHorn01, -0.1745F, 0.1396F, 0.1396F);
			lHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
			
			ModelPart lHorn02 = new ModelPart(this);
			lHorn02.setPivot(0.0F, -1.7F, 0.0F);
			lHorn01.addChild(lHorn02);
			setRotationAngle(lHorn02, -0.1745F, 0.0F, 0.0524F);
			lHorn02.setTextureOffset(0, 4).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.25F, false);
			
			ModelPart lHorn03 = new ModelPart(this);
			lHorn03.setPivot(0.0F, -1.6F, 0.0F);
			lHorn02.addChild(lHorn03);
			setRotationAngle(lHorn03, -0.1047F, 0.0F, 0.1047F);
			lHorn03.setTextureOffset(0, 4).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.15F, false);
			
			ModelPart lHorn04 = new ModelPart(this);
			lHorn04.setPivot(0.0F, -1.7F, 0.0F);
			lHorn03.addChild(lHorn04);
			setRotationAngle(lHorn04, 0.0524F, 0.0F, 0.1396F);
			lHorn04.setTextureOffset(4, 4).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.1F, false);
			
			ModelPart lHorn05 = new ModelPart(this);
			lHorn05.setPivot(0.0F, -2.7F, 0.0F);
			lHorn04.addChild(lHorn05);
			setRotationAngle(lHorn05, 0.0524F, 0.0F, -0.1396F);
			lHorn05.setTextureOffset(43, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
			
			ModelPart HAIR = new ModelPart(this);
			HAIR.setPivot(0.0F, -8.8F, 2.0F);
			head.addChild(HAIR);
			HAIR.setTextureOffset(25, 0).addCuboid(-4.0F, 7.0F, 1.5F, 8.0F, 8.0F, 1.0F, 0.1F, false);
			
			ModelPart base_r1 = new ModelPart(this);
			base_r1.setPivot(0.0F, 2.0F, -3.0F);
			HAIR.addChild(base_r1);
			setRotationAngle(base_r1, 0.4363F, 0.7854F, 0.3054F);
			base_r1.setTextureOffset(14, 39).addCuboid(-5.0F, 0.0F, -1.0F, 6.0F, 9.0F, 6.0F, 0.1F, false);
			
			ModelPart stomach = new ModelPart(this);
			stomach.setPivot(0.0F, 6.0F, 0.0F);
			body.addChild(stomach);
			stomach.setTextureOffset(19, 27).addCuboid(-3.5F, 0.0F, -2.0F, 7.0F, 7.0F, 4.0F, 0.0F, false);
			
			loincloth = new ModelPart(this);
			loincloth.setPivot(0.0F, 4.6F, -1.9F);
			stomach.addChild(loincloth);
			setRotationAngle(loincloth, -0.2269F, 0.0F, 0.0F);
			loincloth.setTextureOffset(48, 0).addCuboid(-3.0F, 0.0F, -0.5F, 6.0F, 8.0F, 1.0F, 0.0F, false);
			
			ModelPart loincloth02 = new ModelPart(this);
			loincloth02.setPivot(0.0F, 7.8F, 0.0F);
			loincloth.addChild(loincloth02);
			setRotationAngle(loincloth02, 0.2269F, 0.0F, 0.0F);
			loincloth02.setTextureOffset(48, 8).addCuboid(-3.0F, 0.05F, -0.53F, 6.0F, 6.0F, 1.0F, 0.0F, false);
			
			tail01 = new ModelPart(this);
			tail01.setPivot(0.0F, 5.5F, 1.3F);
			stomach.addChild(tail01);
			setRotationAngle(tail01, -0.8727F, 0.0F, 0.0F);
			tail01.setTextureOffset(33, 11).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail02 = new ModelPart(this);
			tail02.setPivot(0.0F, 0.0F, 3.8F);
			tail01.addChild(tail02);
			setRotationAngle(tail02, -0.1396F, 0.0F, 0.0F);
			tail02.setTextureOffset(33, 11).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail03 = new ModelPart(this);
			tail03.setPivot(0.0F, 0.0F, 2.9F);
			tail02.addChild(tail03);
			setRotationAngle(tail03, 0.0698F, 0.0F, 0.0F);
			tail03.setTextureOffset(13, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail04 = new ModelPart(this);
			tail04.setPivot(0.0F, 0.0F, 4.9F);
			tail03.addChild(tail04);
			setRotationAngle(tail04, 0.1396F, 0.0F, 0.0F);
			tail04.setTextureOffset(13, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail05 = new ModelPart(this);
			tail05.setPivot(0.0F, 0.0F, 4.9F);
			tail04.addChild(tail05);
			setRotationAngle(tail05, 0.2269F, 0.0F, 0.0F);
			tail05.setTextureOffset(13, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tailTip01 = new ModelPart(this);
			tailTip01.setPivot(0.0F, 0.0F, 4.5F);
			tail05.addChild(tailTip01);
			setRotationAngle(tailTip01, 0.2618F, 0.0F, 0.0F);
			tailTip01.setTextureOffset(16, 54).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart tailTip02 = new ModelPart(this);
			tailTip02.setPivot(0.0F, 0.1F, 0.8F);
			tailTip01.addChild(tailTip02);
			setRotationAngle(tailTip02, 0.0F, -0.7854F, 0.0F);
			tailTip02.setTextureOffset(36, 52).addCuboid(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			leftLeg = new ModelPart(this);
			leftLeg.setPivot(2.1F, 6.2F, 0.1F);
			stomach.addChild(leftLeg);
			setRotationAngle(leftLeg, -0.2618F, 0.0F, -0.1047F);
			leftLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, false);
			
			ModelPart lLeg02 = new ModelPart(this);
			lLeg02.setPivot(0.0F, 5.7F, -0.4F);
			leftLeg.addChild(lLeg02);
			setRotationAngle(lLeg02, 0.6981F, 0.0F, 0.1047F);
			lLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);
			
			ModelPart lLeg03 = new ModelPart(this);
			lLeg03.setPivot(0.0F, 5.2F, 0.2F);
			lLeg02.addChild(lLeg03);
			setRotationAngle(lLeg03, -0.4189F, 0.0F, 0.0F);
			lLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw01a = new ModelPart(this);
			lHoofClaw01a.setPivot(0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw01a);
			setRotationAngle(lHoofClaw01a, 0.1745F, -0.1396F, -0.0349F);
			lHoofClaw01a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw01b = new ModelPart(this);
			lHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw01a.addChild(lHoofClaw01b);
			setRotationAngle(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw01b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw02a = new ModelPart(this);
			lHoofClaw02a.setPivot(-0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw02a);
			setRotationAngle(lHoofClaw02a, 0.1745F, 0.0873F, 0.0349F);
			lHoofClaw02a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw02b = new ModelPart(this);
			lHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw02a.addChild(lHoofClaw02b);
			setRotationAngle(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw02b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			rightLeg = new ModelPart(this);
			rightLeg.setPivot(-2.1F, 6.2F, 0.1F);
			stomach.addChild(rightLeg);
			setRotationAngle(rightLeg, -0.2618F, 0.0F, 0.1047F);
			rightLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, true);
			
			ModelPart rLeg02 = new ModelPart(this);
			rLeg02.setPivot(0.0F, 5.7F, -0.4F);
			rightLeg.addChild(rLeg02);
			setRotationAngle(rLeg02, 0.6981F, 0.0F, -0.1047F);
			rLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, true);
			
			ModelPart rLeg03 = new ModelPart(this);
			rLeg03.setPivot(0.0F, 5.2F, 0.2F);
			rLeg02.addChild(rLeg03);
			setRotationAngle(rLeg03, -0.4189F, 0.0F, 0.0F);
			rLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw01a = new ModelPart(this);
			rHoofClaw01a.setPivot(-0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw01a);
			setRotationAngle(rHoofClaw01a, 0.1745F, 0.1396F, 0.0349F);
			rHoofClaw01a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw01b = new ModelPart(this);
			rHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw01a.addChild(rHoofClaw01b);
			setRotationAngle(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw01b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw02a = new ModelPart(this);
			rHoofClaw02a.setPivot(0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw02a);
			setRotationAngle(rHoofClaw02a, 0.1745F, -0.0873F, -0.0349F);
			rHoofClaw02a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw02b = new ModelPart(this);
			rHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw02a.addChild(rHoofClaw02b);
			setRotationAngle(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw02b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			leftArm = new ModelPart(this);
			leftArm.setPivot(5.0F, 2.0F, 0.0F);
			body.addChild(leftArm);
			setRotationAngle(leftArm, 0.0F, 0.0F, -0.1F);
			leftArm.setTextureOffset(44, 16).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, 0.0F, false);
			
			rightArm = new ModelPart(this);
			rightArm.setPivot(-5.0F, 2.0F, 0.0F);
			body.addChild(rightArm);
			setRotationAngle(rightArm, 0.0F, 0.0F, 0.1F);
			rightArm.setTextureOffset(44, 16).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, 0.0F, true);
		}
		
		@Override
		public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		
		}
		
		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		
		}
	}
}