package moriyashiine.bewitchment.mixin.client;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.entity.interfaces.PressingForwardAccessor;
import moriyashiine.bewitchment.common.network.packet.CauldronTeleportPacket;
import moriyashiine.bewitchment.common.network.packet.TogglePressingForwardAccessor;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	private int transformationAbilityCooldown = 0;
	
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (transformationAbilityCooldown > 0) {
			transformationAbilityCooldown--;
		}
		else if (BewitchmentClient.TRANSFORMATION_ABILITY.isPressed()) {
			transformationAbilityCooldown = 20;
			TransformationAbilityPacket.send();
		}
		if (MinecraftClient.getInstance().options.keyForward.wasPressed() && !((PressingForwardAccessor) this).getPressingForward()) {
			TogglePressingForwardAccessor.send(true);
		}
		else if (((PressingForwardAccessor) this).getPressingForward() && !MinecraftClient.getInstance().options.keyForward.isPressed()) {
			TogglePressingForwardAccessor.send(false);
		}
	}
	
	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
	private void sendChatMessage(String message, CallbackInfo callbackInfo) {
		if (!message.startsWith("/")) {
			for (int i = 0; i < 1; i++) {
				BlockEntity blockEntity = world.getBlockEntity(getBlockPos().down(i));
				if (blockEntity instanceof WitchCauldronBlockEntity && ((WitchCauldronBlockEntity) blockEntity).mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					CauldronTeleportPacket.send(blockEntity.getPos(), message);
					callbackInfo.cancel();
					break;
				}
			}
		}
	}
}
