package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SnakeEntityModel<T extends SnakeEntity> extends AnimalModel<T> {
	private final ModelPart bodyBase;
	private final ModelPart tail01;
	private final ModelPart tail02;
	private final ModelPart tail03;
	private final ModelPart tail04;
	private final ModelPart tail05;
	private final ModelPart tail06;
	private final ModelPart neck00;
	private final ModelPart neck01;
	private final ModelPart head;
	private final ModelPart lowerJaw;
	private final ModelPart lFang;
	private final ModelPart rFang;
	private final ModelPart tongue;
	
	public SnakeEntityModel(ModelPart root) {
		bodyBase = root.getChild("bodyBase");
		tail01 = bodyBase.getChild("tail01");
		tail02 = tail01.getChild("tail02");
		tail03 = tail02.getChild("tail03");
		tail04 = tail03.getChild("tail04");
		tail05 = tail04.getChild("tail05");
		tail06 = tail05.getChild("tail06");
		neck00 = bodyBase.getChild("neck00");
		neck01 = neck00.getChild("neck01");
		head = neck01.getChild("head");
		lowerJaw = head.getChild("lowerJaw");
		lFang = head.getChild("snout").getChild("lFang");
		rFang = head.getChild("snout").getChild("rFang");
		tongue = head.getChild("tongue");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData bodyBase = root.addChild("bodyBase", ModelPartBuilder.create().uv(0, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 23.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail01 = bodyBase.addChild("tail01", ModelPartBuilder.create().uv(0, 21).cuboid(-0.99F, -0.99F, 0.0F, 2.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail02 = tail01.addChild("tail02", ModelPartBuilder.create().uv(0, 27).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail03 = tail02.addChild("tail03", ModelPartBuilder.create().uv(13, 21).cuboid(-0.99F, -0.99F, 0.0F, 2.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail04 = tail03.addChild("tail04", ModelPartBuilder.create().uv(24, 20).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail05 = tail04.addChild("tail05", ModelPartBuilder.create().uv(35, 20).cuboid(-0.49F, -0.99F, 0.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 3.3F, 0.0F, 0.0F, 0.0F));
		tail05.addChild("tail06", ModelPartBuilder.create().uv(46, 21).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F), ModelTransform.of(0.0F, 0.4F, 3.8F, 0.0F, 0.0F, 0.0F));
		ModelPartData neck00 = bodyBase.addChild("neck00", ModelPartBuilder.create().uv(0, 7).cuboid(-0.99F, -0.99F, -3.0F, 2.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData neck01 = neck00.addChild("neck01", ModelPartBuilder.create().cuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData head = neck01.addChild("head", ModelPartBuilder.create().uv(20, 0).cuboid(-1.5F, -1.52F, -2.1F, 3.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, 0.0F, -2.6F, 0.0F, 0.0F, 0.0F));
		head.addChild("lowerJaw", ModelPartBuilder.create().uv(19, 13).cuboid(-1.09F, -0.5F, -3.8F, 2.0F, 1.0F, 4.0F), ModelTransform.of(0.0F, 0.9F, -0.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData snout = head.addChild("snout", ModelPartBuilder.create().uv(20, 7).cuboid(-1.0F, -1.0F, -2.9F, 2.0F, 2.0F, 3.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -0.6F, -2.0F, 0.0F, 0.0F, 0.0F));
		snout.addChild("lFang", ModelPartBuilder.create().uv(16, 0).cuboid(-0.3F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), ModelTransform.of(-0.7F, 0.1F, -2.1F, 0.0F, 0.0F, 0.0F));
		snout.addChild("rFang", ModelPartBuilder.create().uv(16, 0).cuboid(0.1F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), ModelTransform.of(0.7F, 0.1F, -2.1F, 0.0F, 0.0F, 0.0F));
		head.addChild("tongue", ModelPartBuilder.create().uv(31, 0).cuboid(-0.5F, 0.0F, -2.7F, 1.0F, 0.0F, 4.0F), ModelTransform.of(0.0F, 0.9F, -4.7F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 64, 32);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f));
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 0.5f;
		tongue.pitch = MathHelper.cos(animationProgress * 3 + entity.getId()) / 3;
		tongue.visible = entity.tongueFlick > 0;
		boolean attacking = entity.attackTick > 0;
		lFang.visible = rFang.visible = attacking;
		if (attacking) {
			head.pitch = -1;
			lowerJaw.pitch = 1;
		}
		else {
			head.pitch = 0;
			lowerJaw.pitch = 0;
		}
		if (entity.isInSittingPose()) {
			neck00.yaw = -0.35f;
			neck01.yaw = -0.7f;
			tail01.yaw = 0.7f;
			tail02.yaw = 0.6f;
			tail03.yaw = 0.6f;
			tail04.yaw = 0.5f;
			tail05.yaw = 1.2f;
			tail06.yaw = 1.075f;
		}
		else {
			bodyBase.yaw = MathHelper.cos(limbAngle + entity.getId());
			neck00.yaw = -bodyBase.yaw / 3;
			neck01.yaw = neck00.yaw;
			tail01.yaw = neck01.yaw;
			tail02.yaw = tail01.yaw;
			tail03.yaw = tail02.yaw;
			tail04.yaw = tail03.yaw;
			tail05.yaw = tail04.yaw;
			tail06.yaw = tail05.yaw;
		}
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(bodyBase);
	}
}
