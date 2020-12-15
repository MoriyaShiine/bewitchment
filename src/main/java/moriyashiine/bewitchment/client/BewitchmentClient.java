package moriyashiine.bewitchment.client;

import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import moriyashiine.bewitchment.client.misc.SpriteIdentifiers;
import moriyashiine.bewitchment.client.network.packet.CreateNonLivingEntityPacket;
import moriyashiine.bewitchment.client.renderer.entity.SilverArrowEntityRenderer;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;

@Environment(EnvType.CLIENT)
public class BewitchmentClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(CreateNonLivingEntityPacket.ID, CreateNonLivingEntityPacket::handle);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.BW_CHEST, ChestBlockEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.JUNIPER_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.CYPRESS_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.ELDER_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.DRAGONS_BLOOD_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.SILVER_ARROW, (dispatcher, context) -> new SilverArrowEntityRenderer(dispatcher));
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.SALT_LINE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.ACONITE_CROP, BWObjects.BELLADONNA_CROP, BWObjects.GARLIC_CROP, BWObjects.MANDRAKE_CROP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.JUNIPER_SAPLING, BWObjects.POTTED_JUNIPER_SAPLING, BWObjects.JUNIPER_DOOR, BWObjects.JUNIPER_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.CYPRESS_SAPLING, BWObjects.POTTED_CYPRESS_SAPLING, BWObjects.CYPRESS_DOOR, BWObjects.CYPRESS_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.ELDER_SAPLING, BWObjects.POTTED_ELDER_SAPLING, BWObjects.ELDER_DOOR, BWObjects.ELDER_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.DRAGONS_BLOOD_SAPLING, BWObjects.POTTED_DRAGONS_BLOOD_SAPLING, BWObjects.DRAGONS_BLOOD_DOOR, BWObjects.DRAGONS_BLOOD_TRAPDOOR);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.JUNIPER);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_JUNIPER);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.JUNIPER_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_JUNIPER_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.JUNIPER_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_JUNIPER_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.CYPRESS);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_CYPRESS);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.CYPRESS_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_CYPRESS_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.CYPRESS_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_CYPRESS_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.ELDER);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_ELDER);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.ELDER_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_ELDER_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.ELDER_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_ELDER_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.DRAGONS_BLOOD);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_DRAGONS_BLOOD);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.DRAGONS_BLOOD_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_DRAGONS_BLOOD_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.DRAGONS_BLOOD_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_DRAGONS_BLOOD_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.JUNIPER_SIGN.getTexture()));
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.CYPRESS_SIGN.getTexture()));
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.ELDER_SIGN.getTexture()));
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.DRAGONS_BLOOD_SIGN.getTexture()));
		BlockEntity JUNIPER_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.JUNIPER, false);
		BlockEntity TRAPPED_JUNIPER_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.JUNIPER, true);
		BlockEntity CYPRESS_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.CYPRESS, false);
		BlockEntity TRAPPED_CYPRESS_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.CYPRESS, true);
		BlockEntity ELDER_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.ELDER, false);
		BlockEntity TRAPPED_ELDER_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.ELDER, true);
		BlockEntity DRAGONS_BLOOD_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.DRAGONS_BLOOD, false);
		BlockEntity TRAPPED_DRAGONS_BLOOD_CHEST = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.DRAGONS_BLOOD, true);
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.JUNIPER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(JUNIPER_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_JUNIPER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(TRAPPED_JUNIPER_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.CYPRESS_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(CYPRESS_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_CYPRESS_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(TRAPPED_CYPRESS_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.ELDER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(ELDER_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_ELDER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(TRAPPED_ELDER_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.DRAGONS_BLOOD_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(DRAGONS_BLOOD_CHEST, matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(TRAPPED_DRAGONS_BLOOD_CHEST, matrices, vertexConsumers, light, overlay));
	}
}
