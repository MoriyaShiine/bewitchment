package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class SpecterBangleModel extends EntityModel<Entity> {
	private final ModelPart spectreBangle;
	
	public SpecterBangleModel(ModelPart root) {
		spectreBangle = root.getChild("spectreBangle");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData spectreBangle = root.addChild("spectreBangle", ModelPartBuilder.create().cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		spectreBangle.addChild("eye", ModelPartBuilder.create().uv(0, 7).cuboid(-0.5F, -0.5F, -1.0F, 2.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)).uv(0, 11).cuboid(1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)).cuboid(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F, -0.2F, -0.2F)).uv(5, 11).cuboid(0.0F, 0.0F, -1.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)), ModelTransform.of(-0.5F, -0.5F, -1.5F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 16, 16);
	}
	
	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		spectreBangle.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
