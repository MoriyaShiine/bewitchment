package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static moriyashiine.bewitchment.client.misc.SpriteIdentifiers.*;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
	@Inject(method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;", at = @At("HEAD"), cancellable = true)
	private static void getChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> callbackInfo) {
		if (blockEntity instanceof BWChestBlockEntity bwChest) {
			switch (bwChest.type) {
				case JUNIPER:
					switch (type) {
						case SINGLE -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_JUNIPER_CHEST : JUNIPER_CHEST);
							return;
						}
						case LEFT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_JUNIPER_CHEST_LEFT : JUNIPER_CHEST_LEFT);
							return;
						}
						case RIGHT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_JUNIPER_CHEST_RIGHT : JUNIPER_CHEST_RIGHT);
							return;
						}
					}
				case CYPRESS:
					switch (type) {
						case SINGLE -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_CYPRESS_CHEST : CYPRESS_CHEST);
							return;
						}
						case LEFT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_CYPRESS_CHEST_LEFT : CYPRESS_CHEST_LEFT);
							return;
						}
						case RIGHT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_CYPRESS_CHEST_RIGHT : CYPRESS_CHEST_RIGHT);
							return;
						}
					}
				case ELDER:
					switch (type) {
						case SINGLE -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_ELDER_CHEST : ELDER_CHEST);
							return;
						}
						case LEFT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_ELDER_CHEST_LEFT : ELDER_CHEST_LEFT);
							return;
						}
						case RIGHT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_ELDER_CHEST_RIGHT : ELDER_CHEST_RIGHT);
							return;
						}
					}
				case DRAGONS_BLOOD:
					switch (type) {
						case SINGLE -> callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_DRAGONS_BLOOD_CHEST : DRAGONS_BLOOD_CHEST);
						case LEFT -> callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_DRAGONS_BLOOD_CHEST_LEFT : DRAGONS_BLOOD_CHEST_LEFT);
						case RIGHT -> callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_DRAGONS_BLOOD_CHEST_RIGHT : DRAGONS_BLOOD_CHEST_RIGHT);
					}
			}
		}
	}
}
