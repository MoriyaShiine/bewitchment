package moriyashiine.bewitchment.mixin.client;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.network.packet.CauldronTeleportPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
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
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
	private void sendChatMessage(String message, CallbackInfo callbackInfo) {
		if (!message.startsWith("/")) {
			for (int i = 0; i < 1; i++) {
				BlockEntity blockEntity = world.getBlockEntity(getBlockPos().down(i));
				if (blockEntity instanceof WitchCauldronBlockEntity && ((WitchCauldronBlockEntity) blockEntity).mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					CauldronTeleportPacket.send(blockEntity.getPos(), message);
					callbackInfo.cancel();
					return;
				}
			}
		}
	}
}
