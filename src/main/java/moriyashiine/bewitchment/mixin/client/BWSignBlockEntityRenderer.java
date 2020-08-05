package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Block;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SignBlockEntityRenderer.class)
public class BWSignBlockEntityRenderer {
	private static Map<Block, SpriteIdentifier> TEXTURES;
	
	@Inject(method = "getModelTexture", at = @At("HEAD"), cancellable = true)
	private static void getModelTexture(Block block, CallbackInfoReturnable<SpriteIdentifier> info) {
		if (TEXTURES == null) {
			TEXTURES = new HashMap<>();
			registerSpriteIdentifier(BWObjects.juniper_standing_sign, BWObjects.juniper_wall_sign, new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "block/juniper_sign")));
			registerSpriteIdentifier(BWObjects.cypress_standing_sign, BWObjects.cypress_wall_sign, new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "block/cypress_sign")));
			registerSpriteIdentifier(BWObjects.elder_standing_sign, BWObjects.elder_wall_sign, new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "block/elder_sign")));
			registerSpriteIdentifier(BWObjects.dragons_blood_standing_sign, BWObjects.dragons_blood_wall_sign, new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "block/dragons_blood_sign")));
		}
		SpriteIdentifier texture = TEXTURES.get(block);
		if (texture != null) {
			info.setReturnValue(texture);
		}
	}
	
	private static void registerSpriteIdentifier(Block sign, Block wallSign, SpriteIdentifier spriteIdentifier) {
		TEXTURES.put(sign, spriteIdentifier);
		TEXTURES.put(wallSign, spriteIdentifier);
	}
}
