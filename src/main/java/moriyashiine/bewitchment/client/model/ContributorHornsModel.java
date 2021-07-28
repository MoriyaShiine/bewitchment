// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package moriyashiine.bewitchment.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ContributorHornsModel extends Model {
	private final ModelPart armorHead;
	
	public ContributorHornsModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		armorHead = root.getChild("armorHead");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData armorHead = root.addChild("armorHead", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lUpperHorn01 = armorHead.addChild("lUpperHorn01", ModelPartBuilder.create().uv(6, 5).cuboid(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F), ModelTransform.of(2.0F, -8.7F, -0.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData lUpperHorn02 = lUpperHorn01.addChild("lUpperHorn02", ModelPartBuilder.create().uv(0, 12).cuboid(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, 0.0F, 0.9F, 0.0F, 0.0F, 0.0F));
		lUpperHorn02.addChild("lUpperHorn03", ModelPartBuilder.create().uv(8, 12).cuboid(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, 2.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData rUpperHorn01 = armorHead.addChild("rUpperHorn01", ModelPartBuilder.create().uv(6, 5).cuboid(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F), ModelTransform.of(-2.0F, -8.7F, -0.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData rUpperHorn02 = rUpperHorn01.addChild("rUpperHorn02", ModelPartBuilder.create().uv(0, 12).cuboid(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.0F, 0.0F, 0.9F, 0.0F, 0.0F, 0.0F));
		rUpperHorn02.addChild("rUpperHorn03", ModelPartBuilder.create().uv(8, 12).cuboid(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, 2.4F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn01 = armorHead.addChild("lHorn01", ModelPartBuilder.create().cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), ModelTransform.of(2.9F, -7.3F, 1.6F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn02 = lHorn01.addChild("lHorn02", ModelPartBuilder.create().uv(0, 4).cuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(-0.1F, -1.5F, -0.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn03 = lHorn02.addChild("lHorn03", ModelPartBuilder.create().uv(0, 4).cuboid(-0.8F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn04 = lHorn03.addChild("lHorn04", ModelPartBuilder.create().uv(12, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		lHorn04.addChild("lHorn05", ModelPartBuilder.create().uv(12, 0).cuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn01 = armorHead.addChild("rHorn01", ModelPartBuilder.create().cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), ModelTransform.of(-2.9F, -7.3F, 1.6F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn02 = rHorn01.addChild("rHorn02", ModelPartBuilder.create().uv(0, 4).cuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.2F, 0.2F, 0.2F)), ModelTransform.of(0.1F, -1.5F, -0.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn03 = rHorn02.addChild("rHorn03", ModelPartBuilder.create().uv(0, 4).cuboid(-0.2F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn04 = rHorn03.addChild("rHorn04", ModelPartBuilder.create().uv(12, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		rHorn04.addChild("rHorn05", ModelPartBuilder.create().uv(12, 0).cuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 16, 16);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
