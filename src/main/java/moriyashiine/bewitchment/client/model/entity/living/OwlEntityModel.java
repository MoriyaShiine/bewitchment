/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class OwlEntityModel<T extends OwlEntity> extends AnimalModel<T> {
	private final ModelPart body;
	private final ModelPart rFoot;
	private final ModelPart lFoot;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWing04;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWing04;
	private final ModelPart tail01;
	private final ModelPart head;

	public OwlEntityModel(ModelPart root) {
		body = root.getChild("body");
		rFoot = body.getChild("rFoot");
		lFoot = body.getChild("lFoot");
		rWing01 = body.getChild("rWing01");
		rWing02 = rWing01.getChild("rWing02");
		rWing03 = rWing02.getChild("rWing03");
		rWing04 = rWing03.getChild("rWing04");
		lWing01 = body.getChild("lWing01");
		lWing02 = lWing01.getChild("lWing02");
		lWing03 = lWing02.getChild("lWing03");
		lWing04 = lWing03.getChild("lWing04");
		tail01 = body.getChild("tail01");
		head = body.getChild("head");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 14).cuboid(-4.0F, 0.0F, -3.5F, 8.0F, 9.0F, 7.0F), ModelTransform.of(0.0F, 13.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lFoot = body.addChild("lFoot", ModelPartBuilder.create().uv(32, 22).cuboid(-1.0F, 0.0F, -3.4F, 2.0F, 2.0F, 4.0F), ModelTransform.of(-1.8F, 8.9F, -0.4F, 0.0873F, 0.0873F, 0.0F));
		lFoot.addChild("lBackTalon01", ModelPartBuilder.create().uv(39, 28).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), ModelTransform.of(-0.5F, 1.2F, 0.2F, -0.1745F, -0.0698F, 0.0F));
		lFoot.addChild("lBackTalon02", ModelPartBuilder.create().uv(39, 28).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), ModelTransform.of(0.5F, 1.2F, 0.2F, -0.1745F, 0.0698F, 0.0F));
		lFoot.addChild("lFrontTalon02", ModelPartBuilder.create().uv(32, 28).cuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), ModelTransform.of(0.6F, 1.0F, -2.5F, 0.0F, -0.0873F, 0.0F));
		lFoot.addChild("lFrontTalon01", ModelPartBuilder.create().uv(32, 28).cuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), ModelTransform.of(-0.6F, 1.0F, -2.5F, 0.0F, 0.0873F, 0.0F));
		ModelPartData rFoot = body.addChild("rFoot", ModelPartBuilder.create().uv(32, 22).cuboid(-1.0F, 0.0F, -3.4F, 2.0F, 2.0F, 4.0F), ModelTransform.of(1.8F, 8.9F, -0.4F, 0.0873F, -0.0873F, 0.0F));
		rFoot.addChild("rBackTalon01", ModelPartBuilder.create().uv(39, 28).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), ModelTransform.of(0.5F, 1.2F, 0.2F, -0.1745F, 0.0698F, 0.0F));
		rFoot.addChild("rBackTalon02", ModelPartBuilder.create().uv(39, 28).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), ModelTransform.of(-0.5F, 1.2F, 0.2F, -0.1745F, -0.0698F, 0.0F));
		rFoot.addChild("rFrontTalon02", ModelPartBuilder.create().uv(32, 28).cuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), ModelTransform.of(-0.6F, 1.0F, -2.5F, 0.0F, 0.0873F, 0.0F));
		rFoot.addChild("rFrontTalon01", ModelPartBuilder.create().uv(32, 28).cuboid(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), ModelTransform.of(0.6F, 1.0F, -2.5F, 0.0F, -0.0873F, 0.0F));
		ModelPartData lWing01 = body.addChild("lWing01", ModelPartBuilder.create().uv(34, 0).mirrored(true).cuboid(-5.0F, -0.7F, -0.5F, 5.0F, 8.0F, 1.0F), ModelTransform.of(-3.9F, 1.0F, -0.2F, 0.0F, 0.9599F, -0.8727F));
		ModelPartData lWing02 = lWing01.addChild("lWing02", ModelPartBuilder.create().uv(47, 0).mirrored(true).cuboid(-6.0F, -0.7F, -0.5F, 6.0F, 9.0F, 1.0F), ModelTransform.of(-4.5F, 0.0F, 0.1F, 0.0F, 0.0F, -0.6981F));
		ModelPartData lWing03 = lWing02.addChild("lWing03", ModelPartBuilder.create().uv(32, 10).mirrored(true).cuboid(-6.0F, -0.6F, -0.5F, 6.0F, 10.0F, 1.0F), ModelTransform.of(-4.2F, -0.1F, 0.1F, 0.0F, 0.0F, -0.6109F));
		lWing03.addChild("lWing04", ModelPartBuilder.create().uv(47, 26).mirrored(true).cuboid(-8.0F, -0.6F, 0.0F, 8.0F, 6.0F, 0.0F), ModelTransform.of(-2.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rWing01 = body.addChild("rWing01", ModelPartBuilder.create().uv(34, 0).cuboid(0.0F, -0.7F, -0.5F, 5.0F, 8.0F, 1.0F), ModelTransform.of(3.9F, 1.0F, -0.2F, 0.0F, -0.9599F, 0.8727F));
		ModelPartData rWing02 = rWing01.addChild("rWing02", ModelPartBuilder.create().uv(47, 0).cuboid(0.0F, -0.7F, -0.5F, 6.0F, 9.0F, 1.0F), ModelTransform.of(4.5F, 0.0F, 0.1F, 0.0F, 0.0F, 1.0F));
		ModelPartData rWing03 = rWing02.addChild("rWing03", ModelPartBuilder.create().uv(32, 10).cuboid(0.0F, -0.6F, -0.5F, 6.0F, 10.0F, 1.0F), ModelTransform.of(4.2F, -0.1F, 0.1F, 0.0F, 0.0F, 0.6109F));
		rWing03.addChild("rWing04", ModelPartBuilder.create().uv(47, 26).cuboid(1.0F, -0.6F, 0.0F, 8.0F, 6.0F, 0.0F), ModelTransform.of(2.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail01 = body.addChild("tail01", ModelPartBuilder.create().uv(49, 10).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 8.1F, 2.9F, -0.5236F, 0.0F, 0.0F));
		tail01.addChild("mTailPlume", ModelPartBuilder.create().uv(44, 14).cuboid(-2.5F, -0.4F, -0.9F, 5.0F, 0.0F, 6.0F), ModelTransform.of(0.0F, -0.5F, 1.9F, 0.0F, 0.0F, 0.0F));
		tail01.addChild("lTailPlume", ModelPartBuilder.create().uv(44, 20).cuboid(-4.0F, -0.7F, -0.2F, 4.0F, 0.0F, 6.0F), ModelTransform.of(0.9F, -0.3F, 1.2F, 0.0F, -0.3142F, -0.1745F));
		tail01.addChild("rTailPlume", ModelPartBuilder.create().uv(44, 20).cuboid(0.0F, -0.7F, -0.2F, 4.0F, 0.0F, 6.0F), ModelTransform.of(-0.9F, -0.3F, 1.2F, 0.0F, 0.3142F, 0.1745F));
		ModelPartData head = body.addChild("head", ModelPartBuilder.create().cuboid(-3.5F, -5.5F, -3.7F, 7.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, -0.3F, 0.0F, -0.0698F, 0.0F, 0.0F));
		head.addChild("lEar", ModelPartBuilder.create().uv(25, 1).mirrored(true).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F), ModelTransform.of(-2.3F, -4.1F, 0.0F, -0.2618F, 0.0F, -0.4014F));
		head.addChild("rEar", ModelPartBuilder.create().uv(25, 1).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F), ModelTransform.of(2.3F, -4.1F, 0.0F, -0.2618F, 0.0F, 0.4014F));
		ModelPartData beak = head.addChild("beak", ModelPartBuilder.create().cuboid(-0.5F, -0.5F, -0.7F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.0F, -3.3F, -0.0873F, 0.0F, 0.0F));
		beak.addChild("lBeak", ModelPartBuilder.create().cuboid(-0.5F, -0.5F, -0.55F, 1.0F, 2.0F, 1.0F), ModelTransform.of(-0.6F, 0.1F, 0.0F, 0.0F, 0.0F, -0.3491F));
		beak.addChild("rBeak", ModelPartBuilder.create().cuboid(-0.5F, -0.5F, -0.55F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.6F, 0.1F, 0.0F, 0.0F, 0.0F, 0.3491F));
		return TexturedModelData.of(data, 64, 32);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f));
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		if (!entity.isOnGround()) {
			head.pitch -= 1;
			body.pivotY = 19;
			body.pivotZ = -4;
			body.pitch = 1.5f;
			tail01.pitch = -1.5f;
			lWing01.yaw = (float) -(MathHelper.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing01.roll = 0;
			lWing02.yaw = lWing01.yaw / 4;
			lWing02.roll = 0;
			lWing03.yaw = lWing01.yaw / 4;
			lWing03.roll = 0;
			lWing04.visible = true;
			rWing01.yaw = -lWing01.yaw;
			rWing01.roll = 0;
			rWing02.yaw = -lWing02.yaw;
			rWing02.roll = 0;
			rWing03.yaw = -lWing03.yaw;
			rWing03.roll = 0;
			rWing04.visible = true;
			lFoot.pitch = 1;
			rFoot.pitch = 1;
		} else {
			body.pivotY = 13.5f;
			body.pivotZ = 0;
			body.pitch = 0;
			tail01.pitch = -0.5236f;
			lWing01.yaw = 0.9599f;
			lWing01.roll = -0.8727f;
			lWing02.yaw = 0;
			lWing02.roll = -0.6981f;
			lWing03.yaw = 0;
			lWing03.roll = -0.6109f;
			lWing04.visible = false;
			rWing01.yaw = -0.9599f;
			rWing01.roll = 0.8727f;
			rWing02.yaw = 0;
			rWing02.roll = 0.6981f;
			rWing03.yaw = 0;
			rWing03.roll = 0.6109f;
			rWing04.visible = false;
			lFoot.pitch = (MathHelper.cos((float) (limbAngle / 4f + Math.PI)) * limbDistance) + 0.0873f;
			rFoot.pitch = (MathHelper.cos(limbAngle / 4f) * limbDistance) + 0.0873f;
		}
		if (entity.isInSittingPose()) {
			lFoot.yaw = 2 / 3f;
		} else {
			lFoot.yaw = 0.0873f;
		}
		rFoot.yaw = -lFoot.yaw;
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
