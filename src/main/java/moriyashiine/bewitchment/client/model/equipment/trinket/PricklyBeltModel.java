package moriyashiine.bewitchment.client.model.equipment.trinket;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class PricklyBeltModel extends EntityModel<Entity> {
	private final ModelPart armorBody;
	
	public PricklyBeltModel(ModelPart root) {
		armorBody = root.getChild("armorBody");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData armorBody = root.addChild("armorBody",
				ModelPartBuilder.create()
						.cuboid(-4.5F, 22.0F, -2.5F, 9.0F, 3.0F, 5.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
				ModelTransform.of(0.0F, -20.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		armorBody.addChild("buckle",
				ModelPartBuilder.create()
						.uv(0, 9).cuboid(-1.5F, -2.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F))
						.uv(0, 12).cuboid(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F))
						.uv(10, 10).cuboid(0.5F, -2.0F, -0.55F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F))
						.cuboid(-1.5F, -2.0F, -0.55F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
				ModelTransform.of(0.0F, 24.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 32, 32);
	}
	
	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
