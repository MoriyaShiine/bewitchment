package moriyashiine.bewitchment.client;

import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.misc.SpriteIdentifiers;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.client.network.packet.*;
import moriyashiine.bewitchment.client.particle.CauldronBubbleParticle;
import moriyashiine.bewitchment.client.particle.IncenseSmokeParticle;
import moriyashiine.bewitchment.client.renderer.blockentity.BrazierBlockEntityRenderer;
import moriyashiine.bewitchment.client.renderer.blockentity.PoppetShelfBlockEntityRenderer;
import moriyashiine.bewitchment.client.renderer.blockentity.WitchAltarBlockEntityRenderer;
import moriyashiine.bewitchment.client.renderer.blockentity.WitchCauldronBlockEntityRenderer;
import moriyashiine.bewitchment.client.renderer.entity.*;
import moriyashiine.bewitchment.client.renderer.entity.living.*;
import moriyashiine.bewitchment.client.screen.DemonScreen;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.common.entity.interfaces.BroomUserAccessor;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.network.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BewitchmentClient implements ClientModInitializer {
	public static final KeyBinding TRANSFORMATION_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Bewitchment.MODID + ".transformation_ability", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "itemGroup." + Bewitchment.MODID + "." + Bewitchment.MODID));
	
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SyncContractsPacket.ID, SyncContractsPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncDemonTradesPacket.ID, (client, network, buf, sender) -> {
			int syncId = buf.readInt();
			List<DemonEntity.DemonTradeOffer> offers = DemonEntity.DemonTradeOffer.fromPacket(buf);
			int traderId = buf.readInt();
			boolean discount = buf.readBoolean();
			client.execute(() -> {
				if (client.player != null) {
					ScreenHandler screenHandler = client.player.currentScreenHandler;
					if (syncId == screenHandler.syncId && screenHandler instanceof DemonScreenHandler) {
						((DemonScreenHandler) screenHandler).demonMerchant.setCurrentCustomer(client.player);
						((DemonScreenHandler) screenHandler).demonMerchant.setOffersClientside(offers);
						((DemonScreenHandler) screenHandler).demonMerchant.setDemonTraderClientside((LivingEntity) client.world.getEntityById(traderId));
						((DemonScreenHandler) screenHandler).demonMerchant.setDiscountClientside(discount);
					}
				}
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(SyncHornedSpearEntity.ID, SyncHornedSpearEntity::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncClientSerializableBlockEntity.ID, SyncClientSerializableBlockEntity::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncWitchAltarBlockEntity.ID, SyncWitchAltarBlockEntity::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncBrazierBlockEntity.ID, SyncBrazierBlockEntity::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncPoppetShelfBlockEntity.ID, SyncPoppetShelfBlockEntity::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncTaglockHolderBlockEntity.ID, SyncTaglockHolderBlockEntity::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnSmokeParticlesPacket.ID, SpawnSmokeParticlesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnPortalParticlesPacket.ID, SpawnPortalParticlesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnExplosionParticlesPacket.ID, SpawnExplosionParticlesPacket::handle);
		ParticleFactoryRegistry.getInstance().register(BWParticleTypes.CAULDRON_BUBBLE, CauldronBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(BWParticleTypes.INCENSE_SMOKE, IncenseSmokeParticle.Factory::new);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xffff00, BWObjects.GOLDEN_GLYPH);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xc00000, BWObjects.FIERY_GLYPH);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0x8000a0, BWObjects.ELDRITCH_GLYPH);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> tintIndex == 1 ? ((BedBlock) state.getBlock()).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN, BWObjects.ORANGE_COFFIN, BWObjects.MAGENTA_COFFIN, BWObjects.LIGHT_BLUE_COFFIN, BWObjects.YELLOW_COFFIN, BWObjects.LIME_COFFIN, BWObjects.PINK_COFFIN, BWObjects.GRAY_COFFIN, BWObjects.LIGHT_GRAY_COFFIN, BWObjects.CYAN_COFFIN, BWObjects.PURPLE_COFFIN, BWObjects.BLUE_COFFIN, BWObjects.BROWN_COFFIN, BWObjects.GREEN_COFFIN, BWObjects.RED_COFFIN, BWObjects.BLACK_COFFIN);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? ((BedBlock) Block.getBlockFromItem(stack.getItem())).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN, BWObjects.ORANGE_COFFIN, BWObjects.MAGENTA_COFFIN, BWObjects.LIGHT_BLUE_COFFIN, BWObjects.YELLOW_COFFIN, BWObjects.LIME_COFFIN, BWObjects.PINK_COFFIN, BWObjects.GRAY_COFFIN, BWObjects.LIGHT_GRAY_COFFIN, BWObjects.CYAN_COFFIN, BWObjects.PURPLE_COFFIN, BWObjects.BLUE_COFFIN, BWObjects.BROWN_COFFIN, BWObjects.GREEN_COFFIN, BWObjects.RED_COFFIN, BWObjects.BLACK_COFFIN);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x7f0000 : 0xffffff, BWObjects.BOTTLE_OF_BLOOD);
		FabricModelPredicateProviderRegistry.register(BWObjects.HEDGEWITCH_HAT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> stack.getName().asString().toLowerCase().contains("faith") ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.ALCHEMIST_HAT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> stack.getName().asString().toLowerCase().contains("faith") ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.BESMIRCHED_HAT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> stack.getName().asString().toLowerCase().contains("faith") ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.NAZAR, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> stack.hasTag() && stack.getTag().getBoolean("Worn") ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.PRICKLY_BELT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> stack.hasTag() && stack.getTag().getInt("PotionUses") > 0 ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.HORNED_SPEAR, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.TAGLOCK, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> TaglockItem.hasTaglock(stack) ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(BWObjects.WAYSTONE, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity) -> stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") ? 1 : 0);
		ArmorRenderingRegistry.registerModel((livingEntity, itemStack, equipmentSlot, bipedEntityModel) -> new WitchArmorModel<>(equipmentSlot, itemStack.getItem() == BWObjects.HEDGEWITCH_HOOD || itemStack.getItem() == BWObjects.ALCHEMIST_HOOD || itemStack.getItem() == BWObjects.BESMIRCHED_HOOD, !livingEntity.getEquippedStack(EquipmentSlot.FEET).isEmpty()), BWObjects.HEDGEWITCH_HOOD, BWObjects.HEDGEWITCH_HAT, BWObjects.HEDGEWITCH_ROBES, BWObjects.HEDGEWITCH_PANTS, BWObjects.ALCHEMIST_HOOD, BWObjects.ALCHEMIST_HAT, BWObjects.ALCHEMIST_ROBES, BWObjects.ALCHEMIST_PANTS, BWObjects.BESMIRCHED_HOOD, BWObjects.BESMIRCHED_HAT, BWObjects.BESMIRCHED_ROBES, BWObjects.BESMIRCHED_PANTS, BWObjects.HARBINGER);
		Identifier WITCH_HAT_VARIANT = new Identifier(Bewitchment.MODID, "textures/entity/armor/witch_hat_variant.png");
		ArmorRenderingRegistry.registerTexture((livingEntity, itemStack, equipmentSlot, b, s, identifier) -> itemStack.getItem() == BWObjects.HEDGEWITCH_HAT && itemStack.getName().asString().toLowerCase().contains("faith") ? WITCH_HAT_VARIANT : new Identifier(Bewitchment.MODID, "textures/entity/armor/hedgewitch.png"), BWObjects.HEDGEWITCH_HOOD, BWObjects.HEDGEWITCH_HAT, BWObjects.HEDGEWITCH_ROBES, BWObjects.HEDGEWITCH_PANTS);
		ArmorRenderingRegistry.registerTexture((livingEntity, itemStack, equipmentSlot, b, s, identifier) -> itemStack.getItem() == BWObjects.ALCHEMIST_HAT && itemStack.getName().asString().toLowerCase().contains("faith") ? WITCH_HAT_VARIANT : new Identifier(Bewitchment.MODID, "textures/entity/armor/alchemist.png"), BWObjects.ALCHEMIST_HOOD, BWObjects.ALCHEMIST_HAT, BWObjects.ALCHEMIST_ROBES, BWObjects.ALCHEMIST_PANTS);
		ArmorRenderingRegistry.registerTexture((livingEntity, itemStack, equipmentSlot, b, s, identifier) -> itemStack.getItem() == BWObjects.BESMIRCHED_HAT && itemStack.getName().asString().toLowerCase().contains("faith") ? WITCH_HAT_VARIANT : new Identifier(Bewitchment.MODID, "textures/entity/armor/besmirched.png"), BWObjects.BESMIRCHED_HOOD, BWObjects.BESMIRCHED_HAT, BWObjects.BESMIRCHED_ROBES, BWObjects.BESMIRCHED_PANTS);
		ArmorRenderingRegistry.registerTexture((livingEntity, itemStack, equipmentSlot, b, s, identifier) -> new Identifier(Bewitchment.MODID, "textures/entity/armor/harbinger.png"), BWObjects.HARBINGER);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.BW_CHEST, ChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.WITCH_ALTAR, WitchAltarBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.WITCH_CAULDRON, WitchCauldronBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.BRAZIER, BrazierBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.POPPET_SHELF, PoppetShelfBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.JUNIPER_CHEST, ChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.ELDER_CHEST, ChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST, ChestBlockEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.JUNIPER_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.CYPRESS_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.ELDER_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.DRAGONS_BLOOD_BOAT, (dispatcher, context) -> new BoatEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.JUNIPER_BROOM, (dispatcher, context) -> new JuniperBroomEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.CYPRESS_BROOM, (dispatcher, context) -> new CypressBroomEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.ELDER_BROOM, (dispatcher, context) -> new ElderBroomEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.DRAGONS_BLOOD_BROOM, (dispatcher, context) -> new DragonsBloodBroomEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.SILVER_ARROW, (dispatcher, context) -> new SilverArrowEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.HORNED_SPEAR, (dispatcher, context) -> new HornedSpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.OWL, (dispatcher, context) -> new OwlEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.RAVEN, (dispatcher, context) -> new RavenEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.SNAKE, (dispatcher, context) -> new SnakeEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.TOAD, (dispatcher, context) -> new ToadEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.GHOST, (dispatcher, context) -> new GhostEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.VAMPIRE, (dispatcher, context) -> new VampireEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.WEREWOLF, (dispatcher, context) -> new WerewolfEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.HELLHOUND, (dispatcher, context) -> new HellhoundEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.DEMON, (dispatcher, context) -> new DemonEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.LEONARD, (dispatcher, context) -> new LeonardEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.BAPHOMET, (dispatcher, context) -> new BaphometEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.LILITH, (dispatcher, context) -> new LilithEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(BWEntityTypes.HERNE, (dispatcher, context) -> new HerneEntityRenderer(dispatcher));
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.SALT_LINE, BWObjects.TEMPORARY_COBWEB, BWObjects.GLYPH, BWObjects.GOLDEN_GLYPH, BWObjects.FIERY_GLYPH, BWObjects.ELDRITCH_GLYPH, BWObjects.SIGIL);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.ACONITE_CROP, BWObjects.BELLADONNA_CROP, BWObjects.GARLIC_CROP, BWObjects.MANDRAKE_CROP);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.JUNIPER_SAPLING, BWObjects.POTTED_JUNIPER_SAPLING, BWObjects.JUNIPER_DOOR, BWObjects.JUNIPER_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.CYPRESS_SAPLING, BWObjects.POTTED_CYPRESS_SAPLING, BWObjects.CYPRESS_DOOR, BWObjects.CYPRESS_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.ELDER_SAPLING, BWObjects.POTTED_ELDER_SAPLING, BWObjects.ELDER_DOOR, BWObjects.ELDER_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.DRAGONS_BLOOD_SAPLING, BWObjects.POTTED_DRAGONS_BLOOD_SAPLING, BWObjects.DRAGONS_BLOOD_DOOR, BWObjects.DRAGONS_BLOOD_TRAPDOOR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.GLOWING_BRAMBLE, BWObjects.ENDER_BRAMBLE, BWObjects.FRUITING_BRAMBLE, BWObjects.SCORCHED_BRAMBLE, BWObjects.THICK_BRAMBLE, BWObjects.FLEETING_BRAMBLE);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.STONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.PRISMARINE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.NETHER_BRICK_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.BLACKSTONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.GOLDEN_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.END_STONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.OBSIDIAN_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.PURPUR_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), BWObjects.CRYSTAL_BALL);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BWObjects.BRAZIER);
		ScreenRegistry.register(BWScreenHandlers.DEMON_SCREEN_HANDLER, DemonScreen::new);
		ScreenRegistry.register(BWScreenHandlers.BAPHOMET_SCREEN_HANDLER, DemonScreen::new);
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
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.JUNIPER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.JUNIPER, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_JUNIPER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.JUNIPER, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.CYPRESS_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.CYPRESS, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_CYPRESS_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.CYPRESS, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.ELDER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.ELDER, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_ELDER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.ELDER, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.DRAGONS_BLOOD_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.DRAGONS_BLOOD, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> BlockEntityRenderDispatcher.INSTANCE.renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BWChestBlockEntity.Type.DRAGONS_BLOOD, true), matrices, vertexConsumers, light, overlay));
		ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
			private int transformationAbilityCooldown = 0;
			
			@Override
			public void onEndTick(MinecraftClient minecraftClient) {
				if (minecraftClient.player != null) {
					if (transformationAbilityCooldown > 0) {
						transformationAbilityCooldown--;
					}
					else if (BewitchmentClient.TRANSFORMATION_ABILITY.isPressed()) {
						transformationAbilityCooldown = 20;
						TransformationAbilityPacket.send();
					}
					if (((BroomUserAccessor) minecraftClient.player).getPressingForward()) {
						TogglePressingForwardPacket.send(false);
					}
					if (MinecraftClient.getInstance().options.keyForward.isPressed() && minecraftClient.player.getVehicle() instanceof BroomEntity) {
						TogglePressingForwardPacket.send(true);
					}
				}
			}
		});
	}
}
