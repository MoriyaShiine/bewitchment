package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DemonEntityModel<T extends DemonEntity> extends BipedEntityModel<T> {
	private final BipedEntityModel<T> male = new Male();
	private final BipedEntityModel<T> female = new Female();
	
	private BipedEntityModel<T> model;
	
	public DemonEntityModel() {
		super(1);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		model = entity.getDataTracker().get(DemonEntity.MALE) ? this.male : female;
		model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		if (model == male) {
			return ((Male) model).getArm(arm);
		}
		else if (model == female) {
			return ((Female) model).getArm(arm);
		}
		return super.getArm(arm);
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
	
	private class Male extends BipedEntityModel<T> {
		private final ModelPart body;
		private final ModelPart tail01;
		private final ModelPart lWing01;
		private final ModelPart rWing01;
		private final ModelPart BipedLeftArm;
		private final ModelPart BipedRightArm;
		private final ModelPart BipedLeftLeg;
		private final ModelPart BipedRightLeg;
		private final ModelPart head;
		
		private boolean realArm = false;
		
		public Male() {
			super(1, 0, 64, 64);
			body = new ModelPart(this);
			body.setPivot(0.0F, -4.4F, 0.0F);
			body.setTextureOffset(19, 17).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F, 0.0F, false);
			
			tail01 = new ModelPart(this);
			tail01.setPivot(0.0F, 10.1F, 1.3F);
			body.addChild(tail01);
			setRotation(tail01, -0.8727F, 0.0F, 0.0F);
			tail01.setTextureOffset(13, 37).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail02 = new ModelPart(this);
			tail02.setPivot(0.0F, 0.0F, 3.8F);
			tail01.addChild(tail02);
			setRotation(tail02, -0.1396F, 0.0F, 0.0F);
			tail02.setTextureOffset(13, 37).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail03 = new ModelPart(this);
			tail03.setPivot(0.0F, 0.0F, 2.9F);
			tail02.addChild(tail03);
			setRotation(tail03, 0.0698F, 0.0F, 0.0F);
			tail03.setTextureOffset(15, 45).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail04 = new ModelPart(this);
			tail04.setPivot(0.0F, 0.0F, 4.9F);
			tail03.addChild(tail04);
			setRotation(tail04, 0.1396F, 0.0F, 0.0F);
			tail04.setTextureOffset(15, 45).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail05 = new ModelPart(this);
			tail05.setPivot(0.0F, 0.0F, 4.9F);
			tail04.addChild(tail05);
			setRotation(tail05, 0.2269F, 0.0F, 0.0F);
			tail05.setTextureOffset(15, 45).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tailTip01 = new ModelPart(this);
			tailTip01.setPivot(0.0F, 0.0F, 4.5F);
			tail05.addChild(tailTip01);
			setRotation(tailTip01, 0.2618F, 0.0F, 0.0F);
			tailTip01.setTextureOffset(16, 53).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart tailTip02 = new ModelPart(this);
			tailTip02.setPivot(0.0F, 0.1F, 0.8F);
			tailTip01.addChild(tailTip02);
			setRotation(tailTip02, 0.0F, -0.7854F, 0.0F);
			tailTip02.setTextureOffset(15, 58).addCuboid(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			lWing01 = new ModelPart(this);
			lWing01.setPivot(2.5F, 2.1F, 1.4F);
			body.addChild(lWing01);
			setRotation(lWing01, 0.2731F, 0.5236F, 0.0F);
			lWing01.setTextureOffset(26, 35).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart lWing02 = new ModelPart(this);
			lWing02.setPivot(0.1F, 0.2F, 4.3F);
			lWing01.addChild(lWing02);
			setRotation(lWing02, 0.5236F, 0.0F, 0.0F);
			lWing02.setTextureOffset(27, 44).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart lWing03 = new ModelPart(this);
			lWing03.setPivot(0.1F, -0.5F, 7.1F);
			lWing02.addChild(lWing03);
			setRotation(lWing03, 0.2094F, 0.0F, 0.0F);
			lWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart lWing04 = new ModelPart(this);
			lWing04.setPivot(0.0F, 7.7F, 0.0F);
			lWing03.addChild(lWing04);
			setRotation(lWing04, -0.4189F, 0.0F, 0.0F);
			lWing04.setTextureOffset(24, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart lWingMembrane = new ModelPart(this);
			lWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			lWing02.addChild(lWingMembrane);
			setRotation(lWingMembrane, -0.0911F, 0.0F, 0.0F);
			lWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			rWing01 = new ModelPart(this);
			rWing01.setPivot(-2.5F, 2.1F, 1.4F);
			body.addChild(rWing01);
			setRotation(rWing01, 0.2731F, -0.5236F, 0.0F);
			rWing01.setTextureOffset(26, 35).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart rWing02 = new ModelPart(this);
			rWing02.setPivot(-0.1F, 0.2F, 4.3F);
			rWing01.addChild(rWing02);
			setRotation(rWing02, 0.5236F, 0.0F, 0.0F);
			rWing02.setTextureOffset(27, 44).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart rWing03 = new ModelPart(this);
			rWing03.setPivot(-0.1F, -0.5F, 7.1F);
			rWing02.addChild(rWing03);
			setRotation(rWing03, 0.2094F, 0.0F, 0.0F);
			rWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart rWing04 = new ModelPart(this);
			rWing04.setPivot(0.0F, 7.7F, 0.0F);
			rWing03.addChild(rWing04);
			setRotation(rWing04, -0.4189F, 0.0F, 0.0F);
			rWing04.setTextureOffset(24, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart rWingMembrane = new ModelPart(this);
			rWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			rWing02.addChild(rWingMembrane);
			setRotation(rWingMembrane, -0.0911F, 0.0F, 0.0F);
			rWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			BipedLeftArm = new ModelPart(this);
			BipedLeftArm.setPivot(5.0F, -2.4F, 0.0F);
			BipedLeftArm.setTextureOffset(44, 16).addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);
			
			BipedRightArm = new ModelPart(this);
			BipedRightArm.setPivot(-5.0F, -2.4F, 0.0F);
			BipedRightArm.setTextureOffset(44, 16).addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, true);
			
			BipedLeftLeg = new ModelPart(this);
			BipedLeftLeg.setPivot(2.1F, 6F, 0.1F);
			BipedLeftLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, false);
			
			ModelPart lLeg02 = new ModelPart(this);
			lLeg02.setPivot(0.0F, 5.7F, -0.4F);
			BipedLeftLeg.addChild(lLeg02);
			setRotation(lLeg02, 0.6981F, 0.0F, 0.1047F);
			lLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);
			
			ModelPart lLeg03 = new ModelPart(this);
			lLeg03.setPivot(0.0F, 5.2F, 0.2F);
			lLeg02.addChild(lLeg03);
			setRotation(lLeg03, -0.4189F, 0.0F, 0.0F);
			lLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw01a = new ModelPart(this);
			lHoofClaw01a.setPivot(0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw01a);
			setRotation(lHoofClaw01a, 0.1745F, -0.1396F, -0.0349F);
			lHoofClaw01a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw01b = new ModelPart(this);
			lHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw01a.addChild(lHoofClaw01b);
			setRotation(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw01b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw02a = new ModelPart(this);
			lHoofClaw02a.setPivot(-0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw02a);
			setRotation(lHoofClaw02a, 0.1745F, 0.0873F, 0.0349F);
			lHoofClaw02a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw02b = new ModelPart(this);
			lHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw02a.addChild(lHoofClaw02b);
			setRotation(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw02b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			BipedRightLeg = new ModelPart(this);
			BipedRightLeg.setPivot(-2.1F, 6F, 0.1F);
			BipedRightLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, true);
			
			ModelPart rLeg02 = new ModelPart(this);
			rLeg02.setPivot(0.0F, 5.7F, -0.4F);
			BipedRightLeg.addChild(rLeg02);
			setRotation(rLeg02, 0.6981F, 0.0F, -0.1047F);
			rLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, true);
			
			ModelPart rLeg03 = new ModelPart(this);
			rLeg03.setPivot(0.0F, 5.2F, 0.2F);
			rLeg02.addChild(rLeg03);
			setRotation(rLeg03, -0.4189F, 0.0F, 0.0F);
			rLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw01a = new ModelPart(this);
			rHoofClaw01a.setPivot(-0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw01a);
			setRotation(rHoofClaw01a, 0.1745F, 0.1396F, 0.0349F);
			rHoofClaw01a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw01b = new ModelPart(this);
			rHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw01a.addChild(rHoofClaw01b);
			setRotation(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw01b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw02a = new ModelPart(this);
			rHoofClaw02a.setPivot(0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw02a);
			setRotation(rHoofClaw02a, 0.1745F, -0.0873F, -0.0349F);
			rHoofClaw02a.setTextureOffset(0, 57).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw02b = new ModelPart(this);
			rHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw02a.addChild(rHoofClaw02b);
			setRotation(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw02b.setTextureOffset(7, 56).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			head = new ModelPart(this);
			head.setPivot(0.0F, -4.4F, 0.0F);
			head.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			
			ModelPart rHorn01 = new ModelPart(this);
			rHorn01.setPivot(-2.9F, -7.4F, -1.3F);
			head.addChild(rHorn01);
			setRotation(rHorn01, 0.1047F, 0.0F, -0.4189F);
			rHorn01.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
			
			ModelPart rHorn02 = new ModelPart(this);
			rHorn02.setPivot(0.0F, -1.7F, 0.0F);
			rHorn01.addChild(rHorn02);
			setRotation(rHorn02, -0.1047F, 0.0F, 0.192F);
			rHorn02.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, -0.1F, true);
			
			ModelPart rHorn03 = new ModelPart(this);
			rHorn03.setPivot(0.0F, -1.6F, 0.0F);
			rHorn02.addChild(rHorn03);
			setRotation(rHorn03, -0.1396F, 0.0F, 0.0698F);
			rHorn03.setTextureOffset(35, 5).addCuboid(-0.4F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, 0.15F, true);
			
			ModelPart rHorn04 = new ModelPart(this);
			rHorn04.setPivot(0.0F, -1.7F, 0.0F);
			rHorn03.addChild(rHorn04);
			setRotation(rHorn04, -0.1396F, 0.0F, 0.1047F);
			rHorn04.setTextureOffset(35, 10).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
			
			ModelPart lHorn01 = new ModelPart(this);
			lHorn01.setPivot(2.9F, -7.4F, -1.3F);
			head.addChild(lHorn01);
			setRotation(lHorn01, 0.1047F, 0.0F, 0.4189F);
			lHorn01.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
			
			ModelPart lHorn02 = new ModelPart(this);
			lHorn02.setPivot(0.0F, -1.7F, 0.0F);
			lHorn01.addChild(lHorn02);
			setRotation(lHorn02, -0.1047F, 0.0F, -0.192F);
			lHorn02.setTextureOffset(32, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, -0.1F, false);
			
			ModelPart lHorn03 = new ModelPart(this);
			lHorn03.setPivot(0.0F, -1.6F, 0.0F);
			lHorn02.addChild(lHorn03);
			setRotation(lHorn03, -0.1396F, 0.0F, -0.0698F);
			lHorn03.setTextureOffset(35, 5).addCuboid(-0.6F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, 0.15F, false);
			
			ModelPart lHorn04 = new ModelPart(this);
			lHorn04.setPivot(0.0F, -1.7F, 0.0F);
			lHorn03.addChild(lHorn04);
			setRotation(lHorn04, -0.1396F, 0.0F, -0.1047F);
			lHorn04.setTextureOffset(35, 10).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		}
		
		@Override
		public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
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
			BipedLeftLeg.pitch -= 0.2618f;
			BipedLeftLeg.roll -= 0.1047f;
			copyRotation(BipedRightLeg, super.rightLeg);
			BipedRightLeg.pitch /= 2;
			BipedRightLeg.pitch -= 0.2618f;
			BipedRightLeg.roll += 0.1047f;
			lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
			rWing01.yaw = -lWing01.yaw;
			tail01.roll = MathHelper.sin(animationProgress / 8) / 8;
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
	}
	
	private class Female extends BipedEntityModel<T> {
		private final ModelPart body;
		private final ModelPart lWing01;
		private final ModelPart rWing01;
		private final ModelPart loincloth;
		private final ModelPart tail01;
		private final ModelPart BipedLeftArm;
		private final ModelPart BipedRightArm;
		private final ModelPart BipedLeftLeg;
		private final ModelPart BipedRightLeg;
		private final ModelPart head;
		
		private boolean realArm = false;
		
		public Female() {
			super(1, 0, 64, 64);
			body = new ModelPart(this);
			body.setPivot(0.0F, -6.2F, 0.0F);
			body.setTextureOffset(19, 17).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, 0.0F, false);
			
			lWing01 = new ModelPart(this);
			lWing01.setPivot(2.5F, 3.2F, 1.4F);
			body.addChild(lWing01);
			setRotation(lWing01, 0.2731F, 0.5236F, 0.0F);
			lWing01.setTextureOffset(50, 52).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart lWing02 = new ModelPart(this);
			lWing02.setPivot(0.1F, 0.2F, 4.3F);
			lWing01.addChild(lWing02);
			setRotation(lWing02, 0.5236F, 0.0F, 0.0F);
			lWing02.setTextureOffset(38, 52).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart lWing03 = new ModelPart(this);
			lWing03.setPivot(0.1F, -0.5F, 7.1F);
			lWing02.addChild(lWing03);
			setRotation(lWing03, 0.2094F, 0.0F, 0.0F);
			lWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart lWing04 = new ModelPart(this);
			lWing04.setPivot(0.0F, 7.7F, 0.0F);
			lWing03.addChild(lWing04);
			setRotation(lWing04, -0.4189F, 0.0F, 0.0F);
			lWing04.setTextureOffset(25, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart lWingMembrane = new ModelPart(this);
			lWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			lWing02.addChild(lWingMembrane);
			setRotation(lWingMembrane, -0.0911F, 0.0F, 0.0F);
			lWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			rWing01 = new ModelPart(this);
			rWing01.setPivot(-2.5F, 3.2F, 1.4F);
			body.addChild(rWing01);
			setRotation(rWing01, 0.2731F, -0.5236F, 0.0F);
			rWing01.setTextureOffset(50, 52).addCuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F, 0.0F, true);
			
			ModelPart rWing02 = new ModelPart(this);
			rWing02.setPivot(-0.1F, 0.2F, 4.3F);
			rWing01.addChild(rWing02);
			setRotation(rWing02, 0.5236F, 0.0F, 0.0F);
			rWing02.setTextureOffset(38, 52).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
			
			ModelPart rWing03 = new ModelPart(this);
			rWing03.setPivot(-0.1F, -0.5F, 7.1F);
			rWing02.addChild(rWing03);
			setRotation(rWing03, 0.2094F, 0.0F, 0.0F);
			rWing03.setTextureOffset(29, 54).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
			
			ModelPart rWing04 = new ModelPart(this);
			rWing04.setPivot(0.0F, 7.7F, 0.0F);
			rWing03.addChild(rWing04);
			setRotation(rWing04, -0.4189F, 0.0F, 0.0F);
			rWing04.setTextureOffset(25, 55).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, true);
			
			ModelPart rWingMembrane = new ModelPart(this);
			rWingMembrane.setPivot(0.0F, 0.0F, 0.0F);
			rWing02.addChild(rWingMembrane);
			setRotation(rWingMembrane, -0.0911F, 0.0F, 0.0F);
			rWingMembrane.setTextureOffset(41, 26).addCuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F, 0.0F, true);
			
			ModelPart boobs = new ModelPart(this);
			boobs.setPivot(0.0F, 1.9F, -0.3F);
			body.addChild(boobs);
			setRotation(boobs, -0.6109F, 0.0F, 0.0F);
			boobs.setTextureOffset(0, 57).addCuboid(-3.5F, 0.0F, -2.0F, 7.0F, 4.0F, 3.0F, 0.0F, false);
			
			ModelPart stomach = new ModelPart(this);
			stomach.setPivot(0.0F, 6.0F, 0.0F);
			body.addChild(stomach);
			stomach.setTextureOffset(19, 27).addCuboid(-3.5F, 0.0F, -2.0F, 7.0F, 7.0F, 4.0F, 0.0F, false);
			
			loincloth = new ModelPart(this);
			loincloth.setPivot(0.0F, 4.6F, -1.9F);
			stomach.addChild(loincloth);
			setRotation(loincloth, -0.2269F, 0.0F, 0.0F);
			loincloth.setTextureOffset(48, 0).addCuboid(-3.0F, 0.0F, -0.5F, 6.0F, 8.0F, 1.0F, 0.0F, false);
			
			ModelPart loincloth02 = new ModelPart(this);
			loincloth02.setPivot(0.0F, 7.8F, 0.0F);
			loincloth.addChild(loincloth02);
			setRotation(loincloth02, 0.2269F, 0.0F, 0.0F);
			loincloth02.setTextureOffset(48, 8).addCuboid(-3.0F, 0.05F, -0.53F, 6.0F, 6.0F, 1.0F, 0.0F, false);
			
			tail01 = new ModelPart(this);
			tail01.setPivot(0.0F, 5.5F, 1.3F);
			stomach.addChild(tail01);
			setRotation(tail01, -0.8727F, 0.0F, 0.0F);
			tail01.setTextureOffset(33, 11).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail02 = new ModelPart(this);
			tail02.setPivot(0.0F, 0.0F, 3.8F);
			tail01.addChild(tail02);
			setRotation(tail02, -0.1396F, 0.0F, 0.0F);
			tail02.setTextureOffset(33, 11).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
			
			ModelPart tail03 = new ModelPart(this);
			tail03.setPivot(0.0F, 0.0F, 2.9F);
			tail02.addChild(tail03);
			setRotation(tail03, 0.0698F, 0.0F, 0.0F);
			tail03.setTextureOffset(13, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail04 = new ModelPart(this);
			tail04.setPivot(0.0F, 0.0F, 4.9F);
			tail03.addChild(tail04);
			setRotation(tail04, 0.1396F, 0.0F, 0.0F);
			tail04.setTextureOffset(13, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tail05 = new ModelPart(this);
			tail05.setPivot(0.0F, 0.0F, 4.9F);
			tail04.addChild(tail05);
			setRotation(tail05, 0.2269F, 0.0F, 0.0F);
			tail05.setTextureOffset(13, 58).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			
			ModelPart tailTip01 = new ModelPart(this);
			tailTip01.setPivot(0.0F, 0.0F, 4.5F);
			tail05.addChild(tailTip01);
			setRotation(tailTip01, 0.2618F, 0.0F, 0.0F);
			tailTip01.setTextureOffset(16, 54).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart tailTip02 = new ModelPart(this);
			tailTip02.setPivot(0.0F, 0.1F, 0.8F);
			tailTip01.addChild(tailTip02);
			setRotation(tailTip02, 0.0F, -0.7854F, 0.0F);
			tailTip02.setTextureOffset(36, 52).addCuboid(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
			
			BipedLeftArm = new ModelPart(this);
			BipedLeftArm.setPivot(5.0F, -4.2F, 0.0F);
			BipedLeftArm.setTextureOffset(44, 16).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, 0.0F, false);
			
			BipedRightArm = new ModelPart(this);
			BipedRightArm.setPivot(-5.0F, -4.2F, 0.0F);
			BipedRightArm.setTextureOffset(44, 16).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, 0.0F, true);
			
			BipedLeftLeg = new ModelPart(this);
			BipedLeftLeg.setPivot(2.1F, 6.0F, 0.1F);
			BipedLeftLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, false);
			
			ModelPart lLeg02 = new ModelPart(this);
			lLeg02.setPivot(0.0F, 5.7F, -0.4F);
			BipedLeftLeg.addChild(lLeg02);
			setRotation(lLeg02, 0.6981F, 0.0F, 0.1047F);
			lLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);
			
			ModelPart lLeg03 = new ModelPart(this);
			lLeg03.setPivot(0.0F, 5.2F, 0.2F);
			lLeg02.addChild(lLeg03);
			setRotation(lLeg03, -0.4189F, 0.0F, 0.0F);
			lLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw01a = new ModelPart(this);
			lHoofClaw01a.setPivot(0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw01a);
			setRotation(lHoofClaw01a, 0.1745F, -0.1396F, -0.0349F);
			lHoofClaw01a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw01b = new ModelPart(this);
			lHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw01a.addChild(lHoofClaw01b);
			setRotation(lHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw01b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			ModelPart lHoofClaw02a = new ModelPart(this);
			lHoofClaw02a.setPivot(-0.5F, 7.4F, -1.3F);
			lLeg03.addChild(lHoofClaw02a);
			setRotation(lHoofClaw02a, 0.1745F, 0.0873F, 0.0349F);
			lHoofClaw02a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			
			ModelPart lHoofClaw02b = new ModelPart(this);
			lHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			lHoofClaw02a.addChild(lHoofClaw02b);
			setRotation(lHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			lHoofClaw02b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			
			BipedRightLeg = new ModelPart(this);
			BipedRightLeg.setPivot(-2.1F, 6.0F, 0.1F);
			BipedRightLeg.setTextureOffset(0, 17).addCuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F, 0.0F, true);
			
			ModelPart rLeg02 = new ModelPart(this);
			rLeg02.setPivot(0.0F, 5.7F, -0.4F);
			BipedRightLeg.addChild(rLeg02);
			setRotation(rLeg02, 0.6981F, 0.0F, -0.1047F);
			rLeg02.setTextureOffset(0, 30).addCuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, true);
			
			ModelPart rLeg03 = new ModelPart(this);
			rLeg03.setPivot(0.0F, 5.2F, 0.2F);
			rLeg02.addChild(rLeg03);
			setRotation(rLeg03, -0.4189F, 0.0F, 0.0F);
			rLeg03.setTextureOffset(0, 41).addCuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw01a = new ModelPart(this);
			rHoofClaw01a.setPivot(-0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw01a);
			setRotation(rHoofClaw01a, 0.1745F, 0.1396F, 0.0349F);
			rHoofClaw01a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw01b = new ModelPart(this);
			rHoofClaw01b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw01a.addChild(rHoofClaw01b);
			setRotation(rHoofClaw01b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw01b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			ModelPart rHoofClaw02a = new ModelPart(this);
			rHoofClaw02a.setPivot(0.5F, 7.4F, -1.3F);
			rLeg03.addChild(rHoofClaw02a);
			setRotation(rHoofClaw02a, 0.1745F, -0.0873F, -0.0349F);
			rHoofClaw02a.setTextureOffset(0, 54).addCuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
			
			ModelPart rHoofClaw02b = new ModelPart(this);
			rHoofClaw02b.setPivot(0.0F, 0.0F, -1.0F);
			rHoofClaw02a.addChild(rHoofClaw02b);
			setRotation(rHoofClaw02b, 0.3491F, 0.0F, 0.0F);
			rHoofClaw02b.setTextureOffset(7, 53).addCuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F, 0.0F, true);
			
			head = new ModelPart(this);
			head.setPivot(0.0F, -6.2F, 0.0F);
			head.setTextureOffset(0, 1).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			
			ModelPart rHorn01 = new ModelPart(this);
			rHorn01.setPivot(-2.9F, -7.4F, -0.5F);
			head.addChild(rHorn01);
			setRotation(rHorn01, -0.1745F, -0.1396F, -0.1396F);
			rHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
			
			ModelPart rHorn02a = new ModelPart(this);
			rHorn02a.setPivot(0.0F, -1.7F, 0.0F);
			rHorn01.addChild(rHorn02a);
			setRotation(rHorn02a, -0.1745F, 0.0F, -0.0524F);
			rHorn02a.setTextureOffset(0, 4).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.25F, true);
			
			ModelPart rHorn03a = new ModelPart(this);
			rHorn03a.setPivot(0.0F, -1.6F, 0.0F);
			rHorn02a.addChild(rHorn03a);
			setRotation(rHorn03a, -0.1047F, 0.0F, -0.1047F);
			rHorn03a.setTextureOffset(0, 4).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.15F, true);
			
			ModelPart rHorn04 = new ModelPart(this);
			rHorn04.setPivot(0.0F, -1.7F, 0.0F);
			rHorn03a.addChild(rHorn04);
			setRotation(rHorn04, 0.0524F, 0.0F, -0.1396F);
			rHorn04.setTextureOffset(4, 4).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.1F, true);
			
			ModelPart rHorn05 = new ModelPart(this);
			rHorn05.setPivot(0.0F, -2.7F, 0.0F);
			rHorn04.addChild(rHorn05);
			setRotation(rHorn05, 0.0524F, 0.0F, 0.1396F);
			rHorn05.setTextureOffset(43, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
			
			ModelPart lHorn01 = new ModelPart(this);
			lHorn01.setPivot(2.9F, -7.4F, -0.5F);
			head.addChild(lHorn01);
			setRotation(lHorn01, -0.1745F, 0.1396F, 0.1396F);
			lHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
			
			ModelPart lHorn02 = new ModelPart(this);
			lHorn02.setPivot(0.0F, -1.7F, 0.0F);
			lHorn01.addChild(lHorn02);
			setRotation(lHorn02, -0.1745F, 0.0F, 0.0524F);
			lHorn02.setTextureOffset(0, 4).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.25F, false);
			
			ModelPart lHorn03 = new ModelPart(this);
			lHorn03.setPivot(0.0F, -1.6F, 0.0F);
			lHorn02.addChild(lHorn03);
			setRotation(lHorn03, -0.1047F, 0.0F, 0.1047F);
			lHorn03.setTextureOffset(0, 4).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.15F, false);
			
			ModelPart lHorn04 = new ModelPart(this);
			lHorn04.setPivot(0.0F, -1.7F, 0.0F);
			lHorn03.addChild(lHorn04);
			setRotation(lHorn04, 0.0524F, 0.0F, 0.1396F);
			lHorn04.setTextureOffset(4, 4).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.1F, false);
			
			ModelPart lHorn05 = new ModelPart(this);
			lHorn05.setPivot(0.0F, -2.7F, 0.0F);
			lHorn04.addChild(lHorn05);
			setRotation(lHorn05, 0.0524F, 0.0F, -0.1396F);
			lHorn05.setTextureOffset(43, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
			
			ModelPart HAIR = new ModelPart(this);
			HAIR.setPivot(0.0F, -8.8F, 2.0F);
			head.addChild(HAIR);
			HAIR.setTextureOffset(25, 0).addCuboid(-4.0F, 7.0F, 1.5F, 8.0F, 8.0F, 1.0F, 0.1F, false);
			
			ModelPart base_r1 = new ModelPart(this);
			base_r1.setPivot(0.0F, 2.0F, -3.0F);
			HAIR.addChild(base_r1);
			setRotation(base_r1, 0.4363F, 0.7854F, 0.3054F);
			base_r1.setTextureOffset(14, 39).addCuboid(-5.0F, 0.0F, -1.0F, 6.0F, 9.0F, 6.0F, 0.1F, false);
		}
		
		@Override
		public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
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
			BipedLeftLeg.pitch -= 0.2618f;
			BipedLeftLeg.roll -= 0.1047f;
			copyRotation(BipedRightLeg, super.rightLeg);
			BipedRightLeg.pitch /= 2;
			BipedRightLeg.pitch -= 0.2618f;
			BipedRightLeg.roll += 0.1047f;
			lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
			rWing01.yaw = -lWing01.yaw;
			tail01.roll = MathHelper.sin(animationProgress / 8) / 8;
			loincloth.pitch = Math.min(BipedLeftLeg.pitch, BipedRightLeg.pitch);
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
	}
}
