package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;

@Environment(EnvType.CLIENT)
public class ToadEntityModel<T extends ToadEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart lLeg00;
	private final ModelPart lLeg01;
	private final ModelPart lFoot;
	private final ModelPart rLeg00;
	private final ModelPart rLeg01;
	private final ModelPart rFoot;
	private final ModelPart lArm00;
	private final ModelPart lHand;
	private final ModelPart rArm00;
	private final ModelPart rHand;
	private final ModelPart head;
	
	public ToadEntityModel(ModelPart root) {
		body = root.getChild("body");
		lLeg00 = body.getChild("lLeg00");
		lLeg01 = lLeg00.getChild("lLeg01");
		lFoot = lLeg01.getChild("lFoot");
		rLeg00 = body.getChild("rLeg00");
		rLeg01 = rLeg00.getChild("rLeg01");
		rFoot = rLeg01.getChild("rFoot");
		lArm00 = body.getChild("lArm00");
		lHand = lArm00.getChild("lArm01").getChild("lHand");
		rArm00 = body.getChild("rArm00");
		rHand = rArm00.getChild("rArm01").getChild("rHand");
		head = body.getChild("head");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData body = root.addChild("body", ModelPartBuilder.create().cuboid(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 8.0F), ModelTransform.of(0.0F, 18.8F, 0.7F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg00 = body.addChild("lLeg00", ModelPartBuilder.create().uv(0, 13).cuboid(-2.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F), ModelTransform.of(3.7F, -0.2795F, 3.6907F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg01 = lLeg00.addChild("lLeg01", ModelPartBuilder.create().uv(0, 21).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(-1.1F, 3.2F, 0.6F, 0.0F, 0.0F, 0.0F));
		lLeg01.addChild("lFoot", ModelPartBuilder.create().uv(0, 27).cuboid(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F), ModelTransform.of(0.0F, 1.3F, 3.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg00 = body.addChild("rLeg00", ModelPartBuilder.create().uv(0, 13).cuboid(0.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F), ModelTransform.of(-3.7F, -0.2795F, 3.6907F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg01 = rLeg00.addChild("rLeg01", ModelPartBuilder.create().uv(0, 21).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(1.1F, 3.2F, 0.6F, 0.0F, 0.0F, 0.0F));
		rLeg01.addChild("rFoot", ModelPartBuilder.create().uv(0, 27).cuboid(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F), ModelTransform.of(0.0F, 1.3F, 3.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData lArm00 = body.addChild("lArm00", ModelPartBuilder.create().uv(10, 13).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(4.1F, 0.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lArm01 = lArm00.addChild("lArm01", ModelPartBuilder.create().uv(11, 18).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F), ModelTransform.of(-1.3F, 1.4F, 0.3F, 0.0F, 0.0F, 0.0F));
		lArm01.addChild("lHand", ModelPartBuilder.create().uv(-2, 0).cuboid(-1.0F, 0.2F, -2.7F, 3.0F, 0.0F, 4.0F), ModelTransform.of(0.0F, 3.8F, -0.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData rArm00 = body.addChild("rArm00", ModelPartBuilder.create().uv(10, 13).cuboid(0.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(-4.1F, 0.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rArm01 = rArm00.addChild("rArm01", ModelPartBuilder.create().uv(11, 18).cuboid(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F), ModelTransform.of(1.3F, 1.4F, 0.3F, 0.0F, 0.0F, 0.0F));
		rArm01.addChild("rHand", ModelPartBuilder.create().uv(-2, 0).cuboid(-2.0F, 0.2F, -2.7F, 3.0F, 0.0F, 4.0F), ModelTransform.of(0.0F, 3.8F, -0.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(12, 20).cuboid(-2.5F, -1.5F, -5.0F, 5.0F, 2.0F, 5.0F), ModelTransform.of(0.0F, -0.9F, -2.9F, 0.0F, 0.0F, 0.0F));
		ModelPartData throat = head.addChild("throat", ModelPartBuilder.create().uv(18, 14).cuboid(-2.49F, -0.5F, -1.5F, 5.0F, 1.0F, 2.0F), ModelTransform.of(0.0F, 0.9F, -0.4F, 0.0F, 0.0F, 0.0F));
		throat.addChild("lowerJaw", ModelPartBuilder.create().uv(13, 28).cuboid(-2.1F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 32, 32);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		boolean altLegs = false;
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 0.5f;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 1 / 3f;
		if (entity.isInSittingPose() || !entity.isOnGround()) {
			head.pitch -= 1 / 3f;
			body.pitch = 0;
			lArm00.pitch = -2 / 3f;
			lHand.pitch = 1.5f;
			lLeg00.pitch = -1 / 3f;
			lLeg00.yaw = 0;
			if (!entity.isOnGround()) {
				lLeg00.pitch = 1;
				altLegs = true;
			}
		}
		else {
			body.pitch = -0.4014f;
			lArm00.pitch = 0.3491f;
			lHand.pitch = 0.4538f;
			lLeg00.pitch = -0.7854f;
			lLeg00.yaw = -0.3491f;
		}
		if (altLegs) {
			lLeg01.pitch = -0.5f;
			lFoot.pitch = 1.5f;
		}
		else {
			lLeg01.pitch = 0.8727f;
			lFoot.pitch = 0.3142f;
		}
		rArm00.pitch = lArm00.pitch;
		rHand.pitch = lHand.pitch;
		rLeg00.pitch = lLeg00.pitch;
		rLeg00.yaw = -lLeg00.yaw;
		rLeg01.pitch = lLeg01.pitch;
		rFoot.pitch = lFoot.pitch;
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body);
	}
}