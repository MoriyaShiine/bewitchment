package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GhostEntityModel<T extends GhostEntity> extends BipedEntityModel<T> {
	private final ModelPart body;
	private final ModelPart bodyTrail00;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart head;
	
	private boolean realArm = false;
	
	public GhostEntityModel() {
		super(1, 0, 64, 64);
		body = new ModelPart(this);
		body.setPivot(0.0F, 0.0F, 0.0F);
		setRotation(body, 0.2094F, 0.0F, 0.0F);
		body.setTextureOffset(14, 16).addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
		
		bodyTrail00 = new ModelPart(this);
		bodyTrail00.setPivot(0.0F, 7.7F, 0.0F);
		body.addChild(bodyTrail00);
		setRotation(bodyTrail00, 0.1047F, 0.0F, 0.0F);
		bodyTrail00.setTextureOffset(33, 51).addCuboid(-4.5F, 0.0F, -2.5F, 9.0F, 6.0F, 5.0F, 0.0F, false);
		
		ModelPart bodyTrail01 = new ModelPart(this);
		bodyTrail01.setPivot(0.0F, 5.8F, 0.1F);
		bodyTrail00.addChild(bodyTrail01);
		setRotation(bodyTrail01, 0.1047F, 0.0F, 0.0F);
		bodyTrail01.setTextureOffset(0, 39).addCuboid(-5.0F, 0.0F, -3.0F, 10.0F, 9.0F, 6.0F, 0.0F, false);
		
		BipedLeftArm = new ModelPart(this);
		BipedLeftArm.setPivot(5.0F, 2.0F, 0.0F);
		setRotation(BipedLeftArm, -1.3963F, 0.0F, -0.1F);
		BipedLeftArm.setTextureOffset(40, 16).addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, 0.0F, true);
		
		ModelPart lArmWisp = new ModelPart(this);
		lArmWisp.setPivot(1.0F, 2.7F, 1.7F);
		BipedLeftArm.addChild(lArmWisp);
		lArmWisp.setTextureOffset(40, 34).addCuboid(-1.5F, -4.5F, 0.1F, 3.0F, 11.0F, 4.0F, 0.0F, true);
		
		BipedRightArm = new ModelPart(this);
		BipedRightArm.setPivot(-5.0F, 2.0F, 0.0F);
		setRotation(BipedRightArm, -1.3963F, 0.0F, 0.1F);
		BipedRightArm.setTextureOffset(40, 16).addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, 0.0F, true);
		
		ModelPart rArmWisp = new ModelPart(this);
		rArmWisp.setPivot(-1.0F, 2.7F, 1.7F);
		BipedRightArm.addChild(rArmWisp);
		rArmWisp.setTextureOffset(40, 34).addCuboid(-1.5F, -4.5F, 0.1F, 3.0F, 11.0F, 4.0F, 0.0F, true);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, 0.0F);
		head.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		
		ModelPart skull = new ModelPart(this);
		skull.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(skull);
		skull.setTextureOffset(34, 0).addCuboid(-3.5F, -7.5F, -3.0F, 7.0F, 5.0F, 6.0F, 0.0F, false);
		
		ModelPart skullJaw = new ModelPart(this);
		skullJaw.setPivot(0.0F, -1.9F, 0.9F);
		skull.addChild(skullJaw);
		setRotation(skullJaw, 0.1745F, 0.0F, 0.0F);
		skullJaw.setTextureOffset(19, 30).addCuboid(-2.5F, -1.0F, -3.5F, 5.0F, 2.0F, 5.0F, 0.0F, false);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(head, super.head);
		bodyTrail00.pitch = MathHelper.sin(animationProgress / 12) / 6;
		CrossbowPosing.method_29352(leftArm, rightArm, false, entity.handSwingProgress, animationProgress);
		if (entity.getDataTracker().get(GhostEntity.HAS_TARGET)) {
			rightArm.pitch += 4.5;
			rightArm.roll = MathHelper.sin(animationProgress) / 2;
			leftArm.pitch += 4.5;
			leftArm.roll = -rightArm.roll;
		}
		else {
			leftArm.roll = -0.1f;
			rightArm.roll = 0.1f;
		}
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		leftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? BipedLeftArm : BipedRightArm) : super.getArm(arm);
	}
	
	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
