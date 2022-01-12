package moriyashiine.bewitchment.api.client.model;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BroomEntityModel extends EntityModel<BroomEntity> {
	private final ModelPart broom;

	public BroomEntityModel(ModelPart root) {
		broom = root.getChild("broom");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		root.addChild("broom", ModelPartBuilder.create().cuboid(-0.5F, -1.0F, -8.0F, 1.0F, 1.0F, 16.0F).uv(0, 7).cuboid(-1.5F, -2.0F, 8.0F, 3.0F, 3.0F, 1.0F).uv(0, 17).cuboid(-2.5F, -3.0F, 9.0F, 5.0F, 5.0F, 6.0F).uv(0, 0).cuboid(-1.5F, -2.0F, 13.0F, 3.0F, 3.0F, 4.0F), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 64, 64);
	}

	@Override
	public void setAngles(BroomEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		broom.pivotY = 24 + MathHelper.sin(animationProgress / 4);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		broom.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
