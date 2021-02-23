package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

@Environment(EnvType.CLIENT)
public class VampireEntityModel<T extends VampireEntity> extends BipedEntityModel<T> {
	private final ModelPart body;
	private final ModelPart robe;
	private final ModelPart downArms;
	private final ModelPart lArm;
	private final ModelPart rArm;
	private final ModelPart head;
	private final ModelPart lLeg;
	private final ModelPart rLeg;
	private final ModelPart crossedArms;
	
	private boolean realArm = false;
	
	public VampireEntityModel() {
		super(1, 0, 64, 64);
		body = new ModelPart(this);
		body.setPivot(0.0F, 0.0F, 0.0F);
		body.setTextureOffset(16, 20).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
		
		robe = new ModelPart(this);
		robe.setPivot(0.0F, 0.0F, 0.0F);
		robe.setTextureOffset(0, 39).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.25F, false);
		
		downArms = new ModelPart(this);
		downArms.setPivot(0.0F, 24.0F, 0.0F);
		
		lArm = new ModelPart(this);
		lArm.setPivot(5.0F, -22.0F, 0.0F);
		downArms.addChild(lArm);
		lArm.setTextureOffset(40, 46).addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		ModelPart lClaw01 = new ModelPart(this);
		lClaw01.setPivot(2.3F, 8.6F, -1.4F);
		lArm.addChild(lClaw01);
		setRotation(lClaw01, 0.0F, 0.0F, 0.1745F);
		lClaw01.setTextureOffset(0, 0).addCuboid(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw02 = new ModelPart(this);
		lClaw02.setPivot(2.3F, 8.8F, -0.1F);
		lArm.addChild(lClaw02);
		setRotation(lClaw02, 0.0F, 0.0F, 0.1745F);
		lClaw02.setTextureOffset(0, 0).addCuboid(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		
		ModelPart lClaw03 = new ModelPart(this);
		lClaw03.setPivot(2.3F, 8.6F, 1.2F);
		lArm.addChild(lClaw03);
		setRotation(lClaw03, 0.0F, 0.0F, 0.1745F);
		lClaw03.setTextureOffset(0, 0).addCuboid(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		
		rArm = new ModelPart(this);
		rArm.setPivot(-10.0F, 0.0F, 0.0F);
		lArm.addChild(rArm);
		rArm.setTextureOffset(40, 46).addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		
		ModelPart rClaw01 = new ModelPart(this);
		rClaw01.setPivot(-2.3F, 8.6F, -1.4F);
		rArm.addChild(rClaw01);
		setRotation(rClaw01, 0.0F, 0.0F, -0.1745F);
		rClaw01.setTextureOffset(0, 0).addCuboid(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw02 = new ModelPart(this);
		rClaw02.setPivot(-2.3F, 8.8F, -0.1F);
		rArm.addChild(rClaw02);
		setRotation(rClaw02, 0.0F, 0.0F, -0.1745F);
		rClaw02.setTextureOffset(0, 0).addCuboid(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, true);
		
		ModelPart rClaw03 = new ModelPart(this);
		rClaw03.setPivot(-2.3F, 8.6F, 1.2F);
		rArm.addChild(rClaw03);
		setRotation(rClaw03, 0.0F, 0.0F, -0.1745F);
		rClaw03.setTextureOffset(0, 0).addCuboid(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, true);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, 0.0F);
		head.setTextureOffset(0, 0).addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		
		ModelPart nose = new ModelPart(this);
		nose.setPivot(0.0F, -3.2F, -4.1F);
		head.addChild(nose);
		setRotation(nose, -0.7854F, 0.0F, 0.0F);
		nose.setTextureOffset(24, 0).addCuboid(-1.0F, -1.3F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		
		lLeg = new ModelPart(this);
		lLeg.setPivot(2.0F, 12.0F, 0.0F);
		lLeg.setTextureOffset(0, 22).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		rLeg = new ModelPart(this);
		rLeg.setPivot(-2.0F, 12.0F, 0.0F);
		rLeg.setTextureOffset(0, 22).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		
		crossedArms = new ModelPart(this);
		crossedArms.setPivot(0.0F, 3.0F, -1.0F);
		setRotation(crossedArms, -0.75F, 0.0F, 0.0F);
		crossedArms.setTextureOffset(44, 22).addCuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
		crossedArms.setTextureOffset(44, 22).addCuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);
		crossedArms.setTextureOffset(40, 38).addCuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(head, super.head);
		copyRotation(body, super.torso);
		copyRotation(lArm, super.leftArm);
		copyRotation(rArm, super.rightArm);
		copyRotation(lLeg, super.leftLeg);
		copyRotation(rLeg, super.rightLeg);
		boolean hasTarget = entity.getDataTracker().get(VampireEntity.HAS_TARGET);
		crossedArms.visible = !hasTarget;
		downArms.visible = hasTarget;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		robe.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		crossedArms.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		downArms.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? lArm : rArm) : super.getArm(arm);
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
