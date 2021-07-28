package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class RavenEntityModel<T extends RavenEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart lLeg01;
	private final ModelPart rLeg01;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWing04;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWing04;
	
	public RavenEntityModel(ModelPart root) {
		body = root.getChild("body");
		head = body.getChild("neck").getChild("head");
		lLeg01 = body.getChild("lLeg01");
		rLeg01 = body.getChild("rLeg01");
		lWing01 = body.getChild("lWing01");
		lWing02 = lWing01.getChild("lWing02");
		lWing03 = lWing02.getChild("lWing03");
		lWing04 = lWing03.getChild("lWing04");
		rWing01 = body.getChild("rWing01");
		rWing02 = rWing01.getChild("rWing02");
		rWing03 = rWing02.getChild("rWing03");
		rWing04 = rWing03.getChild("rWing04");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData body = root.addChild("body", ModelPartBuilder.create().cuboid(-2.0F, -1.5F, -2.5F, 4.0F, 3.0F, 6.0F), ModelTransform.of(0.0F, 18.2F, -1.6F, 0.0F, 0.0F, 0.0F));
		ModelPartData neck = body.addChild("neck", ModelPartBuilder.create().uv(16, 0).cuboid(-1.5F, -1.3F, -3.0F, 3.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -0.9F, 0.0F, 0.0F, 0.0F));
		neck.addChild("neckFeathers01", ModelPartBuilder.create().uv(13, 27).cuboid(-1.49F, -0.5F, -0.2F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 1.1F, -1.7F, 0.0F, 0.0F, 0.0F));
		neck.addChild("neckFeathers02", ModelPartBuilder.create().uv(0, 27).cuboid(-1.5F, -0.5F, -0.2F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.7F, -2.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(40, 0).cuboid(-1.49F, -1.5F, -2.7F, 3.0F, 3.0F, 3.0F), ModelTransform.of(0.0F, -0.4F, -2.5F, 0.0F, 0.0F, 0.0F));
		head.addChild("beakBottom", ModelPartBuilder.create().uv(54, 0).cuboid(-0.49F, -0.5F, -2.8F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.8F, -1.9F, 0.0F, 0.0F, 0.0F));
		head.addChild("beakTop", ModelPartBuilder.create().uv(54, 0).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, -0.1F, -2.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail01 = body.addChild("tail01", ModelPartBuilder.create().uv(0, 10).cuboid(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, -0.9F, 3.4F, 0.0F, 0.0F, 0.0F));
		tail01.addChild("tail02", ModelPartBuilder.create().uv(0, 15).cuboid(-1.49F, -0.5F, -0.4F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.8F, 0.0F, 0.0F, 0.0F, 0.0F));
		tail01.addChild("middleTalePlume", ModelPartBuilder.create().uv(49, 6).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F), ModelTransform.of(0.0F, -0.2F, 1.7F, 0.0F, 0.0F, 0.0F));
		tail01.addChild("lTailPlume", ModelPartBuilder.create().uv(51, 13).cuboid(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F), ModelTransform.of(-0.5F, 0.7F, 1.2F, 0.0F, 0.0F, 0.0F));
		tail01.addChild("rTailPlume", ModelPartBuilder.create().uv(51, 13).cuboid(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F), ModelTransform.of(0.5F, 0.7F, 1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg01 = body.addChild("lLeg01", ModelPartBuilder.create().uv(34, 0).cuboid(-1.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F), ModelTransform.of(-1.2F, 0.1F, 2.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg02 = lLeg01.addChild("lLeg02", ModelPartBuilder.create().uv(29, 0).cuboid(-0.49F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(-0.5F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lLeg02.addChild("lFoot", ModelPartBuilder.create().uv(29, 5).cuboid(-0.51F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), ModelTransform.of(0.0F, 2.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg01 = body.addChild("rLeg01", ModelPartBuilder.create().uv(34, 0).cuboid(0.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F), ModelTransform.of(1.2F, 0.1F, 2.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg02 = rLeg01.addChild("rLeg02", ModelPartBuilder.create().uv(29, 0).cuboid(-0.51F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.5F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rLeg02.addChild("rFoot", ModelPartBuilder.create().uv(29, 5).cuboid(-0.49F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), ModelTransform.of(0.0F, 2.6F, 0.1F, 0.0F, 0.0F, 0.0F));
		body.addChild("tail03", ModelPartBuilder.create().uv(0, 20).cuboid(-1.5F, 0.0F, -0.8F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.4F, 3.5F, 0.0F, 0.0F, 0.0F));
		body.addChild("bodyFeathers", ModelPartBuilder.create().uv(26, 26).cuboid(-1.5F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F), ModelTransform.of(0.0F, 1.1F, 1.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData lWing01 = body.addChild("lWing01", ModelPartBuilder.create().uv(10, 10).cuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F), ModelTransform.of(-1.9F, -0.3F, -1.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData lWing02 = lWing01.addChild("lWing02", ModelPartBuilder.create().uv(12, 18).cuboid(-4.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F), ModelTransform.of(-3.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lWing03 = lWing02.addChild("lWing03", ModelPartBuilder.create().uv(31, 9).cuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 7.0F), ModelTransform.of(-3.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lWing03.addChild("lWing04", ModelPartBuilder.create().uv(29, 18).cuboid(-8.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F), ModelTransform.of(-1.8F, 0.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rWing01 = body.addChild("rWing01", ModelPartBuilder.create().uv(10, 10).cuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F), ModelTransform.of(1.9F, -0.3F, -1.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData rWing02 = rWing01.addChild("rWing02", ModelPartBuilder.create().uv(12, 18).cuboid(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F), ModelTransform.of(3.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rWing03 = rWing02.addChild("rWing03", ModelPartBuilder.create().uv(31, 9).cuboid(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F), ModelTransform.of(3.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rWing03.addChild("rWing04", ModelPartBuilder.create().uv(29, 18).cuboid(0.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F), ModelTransform.of(1.8F, 0.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 64, 32);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f)) + 1;
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		boolean flying = !entity.isOnGround();
		lWing03.visible = lWing04.visible = rWing03.visible = rWing04.visible = flying;
		if (flying) {
			head.pitch += -2 / 3f;
			body.pitch = 0;
			lWing01.yaw = 0;
			lWing01.roll = (float) -(MathHelper.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing02.yaw = 0;
			lWing02.roll = lWing01.roll / 4;
			lWing03.yaw = 0;
			lWing03.roll = lWing02.roll / 4;
			lWing04.roll = lWing03.roll / 4;
			rWing01.yaw = 0;
			rWing01.roll = -lWing01.roll;
			rWing02.yaw = 0;
			rWing02.roll = -lWing02.roll;
			rWing03.yaw = 0;
			rWing03.roll = -lWing03.roll;
			rWing04.roll = -lWing04.roll;
			lLeg01.pitch = 1;
			rLeg01.pitch = 1;
		}
		else {
			body.pitch = -0.3142f;
			lWing01.yaw = 1.2741f;
			lWing01.roll = 0;
			lWing02.yaw = 0.4538f;
			lWing02.roll = 0;
			lWing03.yaw = 0.4887f;
			lWing03.roll = 0;
			lWing04.roll = 0;
			rWing01.yaw = -1.2741f;
			rWing01.roll = 0;
			rWing02.yaw = -0.4538f;
			rWing02.roll = 0;
			rWing03.yaw = -0.4887f;
			rWing03.roll = 0;
			rWing04.roll = 0;
			lLeg01.pitch = (MathHelper.cos((float) (limbAngle + Math.PI)) * limbDistance) + 0.3142f;
			rLeg01.pitch = (MathHelper.cos(limbAngle) * limbDistance) + 0.3142f;
		}
		boolean sitting = entity.isInSittingPose();
		lLeg01.visible = rLeg01.visible = !sitting;
		if (sitting) {
			body.pivotY = 21.5f;
		}
		else {
			body.pivotY = 18.2f;
		}
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