package moriyashiine.bewitchment.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	private static final Identifier BEWITCHMENT_GUI_ICONS_TEXTURE = new Identifier(Bewitchment.MODID, "textures/gui/icons.png");
	
	@Shadow
	protected abstract PlayerEntity getCameraPlayer();
	
	@Shadow
	private int scaledHeight;
	
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V"))
	private void render(MatrixStack matrices, float tickDelta, CallbackInfo callbackInfo) {
		PlayerEntity player = getCameraPlayer();
		if (player != null) {
			MagicAccessor.of(player).ifPresent(magicAccessor -> {
				if (magicAccessor.getMagicTimer() > 0) {
					client.getTextureManager().bindTexture(BEWITCHMENT_GUI_ICONS_TEXTURE);
					RenderSystem.color4f(1, 1, 1, magicAccessor.getMagicTimer() / 10f);
					drawTexture(matrices, 13, (scaledHeight - 74) / 2, 25, 0, 7, (int) (74 - (magicAccessor.getMagic() * 74f / MagicAccessor.MAX_MAGIC)));
					drawTexture(matrices, 4, (scaledHeight - 102) / 2, 0, 0, 25, 102);
					client.getTextureManager().bindTexture(GUI_ICONS_TEXTURE);
				}
			});
		}
	}
}
