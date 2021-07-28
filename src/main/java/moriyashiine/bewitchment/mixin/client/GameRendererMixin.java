package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Shadow
	@Final
	private Camera camera;
	
	@Inject(method = "renderWorld", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V"))
	private void renderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo info) {
		if (client.player.getVehicle() instanceof BroomEntity && !camera.isThirdPerson()) {
			matrix.translate(0, -(MathHelper.sin((client.player.getVehicle().age + client.player.getVehicle().getId()) / 4f) / 16f), 0);
		}
	}
}
