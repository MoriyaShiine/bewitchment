package moriyashiine.bewitchment.client;

import moriyashiine.bewitchment.client.network.message.SmokePuffMessage;
import moriyashiine.bewitchment.client.network.message.SyncDistillingRecipeMessage;
import moriyashiine.bewitchment.client.network.message.SyncFocalChalkBlockEntityMessage;
import moriyashiine.bewitchment.client.network.message.SyncSpinningRecipeMessage;
import moriyashiine.bewitchment.client.render.entity.SilverArrowEntityRenderer;
import moriyashiine.bewitchment.client.render.entity.living.OwlEntityRenderer;
import moriyashiine.bewitchment.client.render.entity.living.RavenEntityRenderer;
import moriyashiine.bewitchment.client.render.entity.living.SnakeEntityRenderer;
import moriyashiine.bewitchment.client.render.entity.living.ToadEntityRenderer;
import moriyashiine.bewitchment.client.screen.DistilleryScreen;
import moriyashiine.bewitchment.client.screen.SpinningWheelScreen;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class BewitchmentClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(SmokePuffMessage.ID, SmokePuffMessage::handle);
		ClientSidePacketRegistry.INSTANCE.register(SyncDistillingRecipeMessage.ID, SyncDistillingRecipeMessage::handle);
		ClientSidePacketRegistry.INSTANCE.register(SyncSpinningRecipeMessage.ID, SyncSpinningRecipeMessage::handle);
		ClientSidePacketRegistry.INSTANCE.register(SyncFocalChalkBlockEntityMessage.ID, SyncFocalChalkBlockEntityMessage::handle);
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.aconite_crops, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.belladonna_crops, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.garlic_crops, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.mandrake_crops, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.potted_juniper_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.potted_cypress_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.potted_elder_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.potted_dragons_blood_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.juniper_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.cypress_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.elder_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.dragons_blood_sapling, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.juniper_door_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.cypress_door_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.elder_door_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.dragons_blood_door_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.juniper_trapdoor, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.cypress_trapdoor, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.elder_trapdoor, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.dragons_blood_trapdoor, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.focal_chalk_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.chalk_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.infernal_chalk_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.eldritch_chalk_block, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.spanish_moss, RenderLayer.getCutout());
		for (Block block : BWObjects.stone_witch_altar) {
			BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
		}
		BlockRenderLayerMap.INSTANCE.putBlock(BWObjects.distillery, RenderLayer.getTranslucent());
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.silver_arrow, (entityRenderDispatcher, context) -> new SilverArrowEntityRenderer(entityRenderDispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.owl, (entityRenderDispatcher, context) -> new OwlEntityRenderer(entityRenderDispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.raven, (entityRenderDispatcher, context) -> new RavenEntityRenderer(entityRenderDispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.snake, (entityRenderDispatcher, context) -> new SnakeEntityRenderer(entityRenderDispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.toad, (entityRenderDispatcher, context) -> new ToadEntityRenderer(entityRenderDispatcher));
		ColorProviderRegistryImpl.BLOCK.register((state, world, pos, tintIndex) -> 0xffff00, BWObjects.focal_chalk_block);
		ColorProviderRegistryImpl.BLOCK.register((state, world, pos, tintIndex) -> 0xc00000, BWObjects.infernal_chalk_block);
		ColorProviderRegistryImpl.BLOCK.register((state, world, pos, tintIndex) -> 0x8000a0, BWObjects.eldritch_chalk_block);
		ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x7f0000 : 0xffffff, BWObjects.bottle_of_blood);
		ScreenRegistry.register(BWScreenHandlerTypes.distillery, DistilleryScreen::new);
		ScreenRegistry.register(BWScreenHandlerTypes.spinning_wheel, SpinningWheelScreen::new);
	}
}