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
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_JUNIPER : JUNIPER);
							return;
						}
						case LEFT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_JUNIPER_LEFT : JUNIPER_LEFT);
							return;
						}
						case RIGHT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_JUNIPER_RIGHT : JUNIPER_RIGHT);
							return;
						}
					}
				case CYPRESS:
					switch (type) {
						case SINGLE -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_CYPRESS : CYPRESS);
							return;
						}
						case LEFT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_CYPRESS_LEFT : CYPRESS_LEFT);
							return;
						}
						case RIGHT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_CYPRESS_RIGHT : CYPRESS_RIGHT);
							return;
						}
					}
				case ELDER:
					switch (type) {
						case SINGLE -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_ELDER : ELDER);
							return;
						}
						case LEFT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_ELDER_LEFT : ELDER_LEFT);
							return;
						}
						case RIGHT -> {
							callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_ELDER_RIGHT : ELDER_RIGHT);
							return;
						}
					}
				case DRAGONS_BLOOD:
					switch (type) {
						case SINGLE -> callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_DRAGONS_BLOOD : DRAGONS_BLOOD);
						case LEFT -> callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_DRAGONS_BLOOD_LEFT : DRAGONS_BLOOD_LEFT);
						case RIGHT -> callbackInfo.setReturnValue(bwChest.trapped ? TRAPPED_DRAGONS_BLOOD_RIGHT : DRAGONS_BLOOD_RIGHT);
					}
			}
		}
	}
}
