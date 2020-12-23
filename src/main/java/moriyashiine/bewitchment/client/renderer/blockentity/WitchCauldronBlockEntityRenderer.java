package moriyashiine.bewitchment.client.renderer.blockentity;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WitchCauldronBlockEntityRenderer extends BlockEntityRenderer<WitchCauldronBlockEntity> {
	private static final Sprite WATER_SPRITE = MinecraftClient.getInstance().getBlockRenderManager().getModel(Blocks.WATER.getDefaultState()).getSprite();
	
	public WitchCauldronBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public void render(WitchCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			BlockPos pos = entity.getPos();
			if (entity.hasCustomName()) {
				if (pos.getSquaredDistance(dispatcher.camera.getPos(), true) <= 256) {
					matrices.push();
					matrices.translate(0.5, 1, 0.5);
					matrices.multiply(dispatcher.camera.getRotation());
					matrices.scale(-0.025f, -0.025f, 0.025f);
					//noinspection ConstantConditions
					String string = entity.getCustomName().getString();
					Matrix4f model = matrices.peek().getModel();
					int x = -dispatcher.getTextRenderer().getWidth(string) / 2;
					dispatcher.getTextRenderer().draw(string, x, 0, 0x20ffff, false, model, vertexConsumers, false, (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255) << 24, light);
					dispatcher.getTextRenderer().draw(string, x, 0, -1, false, model, vertexConsumers, false, 0, light);
					matrices.pop();
				}
			}
		}
	}
}
