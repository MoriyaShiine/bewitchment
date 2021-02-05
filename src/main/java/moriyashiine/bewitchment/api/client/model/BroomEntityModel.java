package moriyashiine.bewitchment.api.client.model;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BroomEntityModel extends EntityModel<BroomEntity> {
	private final ModelPart broom;
	
	public BroomEntityModel() {
		textureWidth = 64;
		textureHeight = 64;
		broom = new ModelPart(this);
		broom.setPivot(0, 24, 0);
		broom.setTextureOffset(0, 0).addCuboid(-0.5f, -1, -8, 1, 1, 16, 0, false);
		broom.setTextureOffset(0, 7).addCuboid(-1.5f, -2, 8, 3, 3, 1, 0, false);
		broom.setTextureOffset(0, 17).addCuboid(-2.5f, -3, 9, 5, 5, 6, 0, false);
		broom.setTextureOffset(0, 0).addCuboid(-1.5f, -2, 13, 3, 3, 4, 0, false);
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
