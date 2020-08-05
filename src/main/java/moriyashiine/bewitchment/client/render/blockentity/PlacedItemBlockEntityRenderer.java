package moriyashiine.bewitchment.client.render.blockentity;

import moriyashiine.bewitchment.common.block.entity.PlacedItemBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class PlacedItemBlockEntityRenderer extends BlockEntityRenderer<PlacedItemBlockEntity> {
	public PlacedItemBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public void render(PlacedItemBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			matrices.push();
			BlockPos pos = entity.getPos();
			matrices.translate(0.5, 0.0375, 0.5);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-world.getBlockState(pos).get(Properties.HORIZONTAL_FACING).asRotation()));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
			matrices.scale(0.5f, 0.5f, 0.5f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity.stack, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers);
			matrices.pop();
		}
	}
}
