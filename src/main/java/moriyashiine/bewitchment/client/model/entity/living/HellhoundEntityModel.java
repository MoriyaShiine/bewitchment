package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HellhoundEntityModel<T extends HellhoundEntity> extends EntityModel<T> {
	private final ModelPart body;
	private final ModelPart lForeleg;
	private final ModelPart tail00;
	private final ModelPart lHindleg;
	private final ModelPart rHindleg;
	private final ModelPart rForeleg;
	private final ModelPart neck;
	
	public HellhoundEntityModel(ModelPart root) {
		body = root.getChild("body");
		lForeleg = root.getChild("lForeleg");
		tail00 = body.getChild("tail00");
		lHindleg = root.getChild("lHindleg");
		rHindleg = root.getChild("rHindleg");
		rForeleg = root.getChild("rForeleg");
		neck = root.getChild("neck");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		root.addChild("lHindleg", ModelPartBuilder.create().uv(0, 18).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), ModelTransform.of(1.5F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(18, 14).cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F), ModelTransform.of(0.0F, 14.0F, 1.0F, 1.5708F, 0.0F, 0.0F));
		ModelPartData mane00 = body.addChild("mane00", ModelPartBuilder.create().uv(21, 0).cuboid(-4.0F, -3.5F, -3.01F, 8.0F, 7.0F, 7.0F), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		mane00.addChild("mane02", ModelPartBuilder.create().uv(28, 48).cuboid(-3.5F, -1.0F, 0.0F, 7.0F, 2.0F, 7.0F), ModelTransform.of(0.0F, -1.0F, 2.7F, -1.2915F, 0.0F, 0.0F));
		ModelPartData tail00 = body.addChild("tail00", ModelPartBuilder.create().uv(20, 33).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, 6.8F, 2.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData tail01 = tail00.addChild("tail01", ModelPartBuilder.create().uv(26, 33).cuboid(-0.5F, -0.1F, -0.5F, 1.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, 3.3F, 0.0F, 0.2731F, 0.0F, 0.0F));
		ModelPartData tail02 = tail01.addChild("tail02", ModelPartBuilder.create().uv(32, 33).cuboid(-0.5F, -0.1F, -0.5F, 1.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, 4.4F, 0.0F, 0.2094F, 0.0F, 0.0F));
		ModelPartData tail03 = tail02.addChild("tail03", ModelPartBuilder.create().uv(37, 33).cuboid(-1.0F, -0.1F, -0.5F, 2.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, 4.3F, 0.0F, 0.2094F, 0.0F, 0.0F));
		tail03.addChild("tail04", ModelPartBuilder.create().uv(44, 33).cuboid(-1.5F, -0.5F, -0.59F, 2.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, 1.3F, 0.1F, 0.0F, 0.0F, -0.7854F));
		body.addChild("mane01", ModelPartBuilder.create().uv(0, 48).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 7.0F), ModelTransform.of(0.0F, -8.2F, 2.5F, -1.1781F, 0.0F, 0.0F));
		root.addChild("lForeleg", ModelPartBuilder.create().uv(0, 18).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), ModelTransform.of(1.5F, 16.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		root.addChild("rHindleg", ModelPartBuilder.create().uv(0, 18).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), ModelTransform.of(-1.5F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
		root.addChild("rForeleg", ModelPartBuilder.create().uv(0, 18).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), ModelTransform.of(-1.5F, 16.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData neck = root.addChild("neck", ModelPartBuilder.create().uv(0, 32).cuboid(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 4.0F), ModelTransform.of(0.0F, 13.6F, -4.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData head = neck.addChild("head", ModelPartBuilder.create().cuboid(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, -2.9F, 0.0F, 0.0F, 0.0F));
		head.addChild("leftEar", ModelPartBuilder.create().uv(16, 14).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F), ModelTransform.of(-2.0F, -3.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		head.addChild("rightEar", ModelPartBuilder.create().uv(16, 14).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F), ModelTransform.of(2.0F, -3.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		head.addChild("muzzle", ModelPartBuilder.create().uv(0, 10).cuboid(-1.5F, -1.0F, -3.0F, 3.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, 0.7F, -3.9F, 0.0F, 0.0F, 0.0F));
		head.addChild("lowerJaw", ModelPartBuilder.create().uv(0, 43).cuboid(-1.5F, -0.3F, -3.0F, 3.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 2.0F, -3.8F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn01 = head.addChild("rHorn01", ModelPartBuilder.create().uv(46, 0).mirrored(true).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(-1.3F, -2.1F, -1.4F, -1.0016F, -0.4554F, 0.0F));
		ModelPartData rHorn02 = rHorn01.addChild("rHorn02", ModelPartBuilder.create().uv(55, 0).mirrored(true).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, -2.5F, -0.1F, -0.5236F, -0.1745F, 0.0F));
		ModelPartData rHorn03 = rHorn02.addChild("rHorn03", ModelPartBuilder.create().uv(52, 7).mirrored(true).cuboid(-0.8F, -2.0F, -0.8F, 1.0F, 2.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, -1.5F, -0.1F, -0.6981F, -0.2094F, 0.0F));
		ModelPartData rHorn04 = rHorn03.addChild("rHorn04", ModelPartBuilder.create().uv(57, 6).mirrored(true).cuboid(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)), ModelTransform.of(0.0F, -1.5F, -0.1F, -0.6981F, 0.0F, 0.0F));
		ModelPartData rHorn05 = rHorn04.addChild("rHorn05", ModelPartBuilder.create().uv(52, 12).mirrored(true).cuboid(-0.5F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -2.5F, -0.1F, -0.6981F, -0.3491F, 0.0F));
		rHorn05.addChild("rHorn06", ModelPartBuilder.create().uv(58, 13).mirrored(true).cuboid(-0.51F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.6283F, 0.0F, 0.0F));
		ModelPartData lHorn01 = head.addChild("lHorn01", ModelPartBuilder.create().uv(46, 0).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(1.3F, -2.1F, -1.4F, -1.0016F, 0.4554F, 0.0F));
		ModelPartData lHorn02 = lHorn01.addChild("lHorn02", ModelPartBuilder.create().uv(55, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, -2.5F, -0.1F, -0.5236F, 0.1745F, 0.0F));
		ModelPartData lHorn03 = lHorn02.addChild("lHorn03", ModelPartBuilder.create().uv(52, 7).cuboid(-0.2F, -2.0F, -0.8F, 1.0F, 2.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, -1.5F, -0.1F, -0.6981F, 0.2094F, 0.0F));
		ModelPartData lHorn04 = lHorn03.addChild("lHorn04", ModelPartBuilder.create().uv(57, 6).cuboid(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)), ModelTransform.of(0.0F, -1.5F, -0.1F, -0.6981F, 0.0F, 0.0F));
		ModelPartData lHorn05 = lHorn04.addChild("lHorn05", ModelPartBuilder.create().uv(52, 12).cuboid(-0.5F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -2.5F, -0.1F, -0.6981F, 0.3491F, 0.0F));
		lHorn05.addChild("lHorn06", ModelPartBuilder.create().uv(58, 13).cuboid(-0.49F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.6283F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 64, 64);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		neck.pitch = (float) (headPitch * (Math.PI / 180f));
		neck.yaw = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		lForeleg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		rForeleg.pitch = -lForeleg.pitch;
		rHindleg.pitch = MathHelper.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		lHindleg.pitch = -rHindleg.pitch;
		tail00.roll = (MathHelper.cos(limbAngle * 2 / 3f) * limbDistance) + MathHelper.sin((animationProgress + entity.getId()) * 1 / 8f) * 0.25f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
