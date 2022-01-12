/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class DruidBandModel extends EntityModel<Entity> {
	private final ModelPart druidBand;

	public DruidBandModel(ModelPart root) {
		druidBand = root.getChild("druidBand");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData druidBand = root.addChild("druidBand", ModelPartBuilder.create().cuboid(0.0F, 11.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.1F, 0.1F, 0.1F)).uv(0, 6).cuboid(1.0F, 11.0F, -2.5F, 2.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(-2.0F, -15.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData woodDetails = druidBand.addChild("woodDetails", ModelPartBuilder.create().uv(7, 6).cuboid(3.5F, -5.5F, -2.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)).uv(2, 11).cuboid(3.25F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)).mirrored(true).cuboid(-1.25F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)).uv(9, 12).cuboid(-1.25F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)).mirrored(false).cuboid(3.25F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)).uv(8, 6).cuboid(-0.5F, -5.5F, -2.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)).uv(9, 6).cuboid(-0.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)).uv(8, 6).cuboid(3.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)).uv(7, 6).cuboid(1.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)).cuboid(-0.5F, -5.5F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)).cuboid(3.5F, -5.5F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.25F, -0.25F, -0.25F)), ModelTransform.of(0.0F, 15.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		woodDetails.addChild("leaf04_r1", ModelPartBuilder.create().uv(1, 12).cuboid(-0.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)), ModelTransform.of(3.75F, -4.0F, 1.75F, 0.0F, -0.3054F, 0.0F));
		woodDetails.addChild("leaf02_r1", ModelPartBuilder.create().uv(9, 12).mirrored(true).cuboid(-1.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)), ModelTransform.of(0.25F, -4.0F, -2.0F, 0.0F, -0.3054F, 0.0F));
		woodDetails.addChild("leaf03_r1", ModelPartBuilder.create().uv(1, 12).mirrored(true).cuboid(-1.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)), ModelTransform.of(0.25F, -4.0F, 1.75F, 0.0F, 0.3054F, 0.0F));
		woodDetails.addChild("leaf01_r1", ModelPartBuilder.create().uv(9, 12).cuboid(-0.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(-0.4F, -0.4F, -0.4F)), ModelTransform.of(3.75F, -4.0F, -2.0F, 0.0F, 0.3054F, 0.0F));
		return TexturedModelData.of(data, 16, 16);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		druidBand.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
