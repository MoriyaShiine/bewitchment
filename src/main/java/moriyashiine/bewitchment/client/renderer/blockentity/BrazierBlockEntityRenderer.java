package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class BrazierBlockEntityRenderer implements BlockEntityRenderer<BrazierBlockEntity> {
	@Override
	public void render(BrazierBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			ItemStack one = entity.getStack(0);
			if (!one.isEmpty()) {
				matrices.push();
				double yOffset = entity.getCachedState().get(Properties.HANGING) ? -0.815 : 0;
				matrices.translate(0.5, 0.925 + yOffset, 0.5);
				matrices.scale(0.5f, 0.5f, 0.5f);
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
				MinecraftClient.getInstance().getItemRenderer().renderItem(one, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
				matrices.pop();
				ItemStack two = entity.getStack(1);
				if (!two.isEmpty()) {
					matrices.push();
					matrices.translate(0.5, 0.955 + yOffset, 0.5);
					matrices.scale(0.5f, 0.5f, 0.5f);
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
					MinecraftClient.getInstance().getItemRenderer().renderItem(two, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
					matrices.pop();
					ItemStack three = entity.getStack(2);
					if (!three.isEmpty()) {
						matrices.push();
						matrices.translate(0.5, 0.985 + yOffset, 0.5);
						matrices.scale(0.5f, 0.5f, 0.5f);
						matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
						matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
						MinecraftClient.getInstance().getItemRenderer().renderItem(three, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
						matrices.pop();
						ItemStack four = entity.getStack(3);
						if (!four.isEmpty()) {
							matrices.push();
							matrices.translate(0.5, 1.015 + yOffset, 0.5);
							matrices.scale(0.5f, 0.5f, 0.5f);
							matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
							matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(270));
							MinecraftClient.getInstance().getItemRenderer().renderItem(four, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
							matrices.pop();
						}
					}
				}
			}
		}
	}
}
