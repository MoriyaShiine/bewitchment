/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.misc.SpriteIdentifiers;
import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.client.model.entity.living.*;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.DruidBandModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.PricklyBeltModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.SpecterBangleModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.ZephyrHarnessModel;
import moriyashiine.bewitchment.client.network.packet.*;
import moriyashiine.bewitchment.client.particle.CauldronBubbleParticle;
import moriyashiine.bewitchment.client.particle.IncenseSmokeParticle;
import moriyashiine.bewitchment.client.renderer.WitchArmorRenderer;
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
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@Environment(EnvType.CLIENT)
public class BewitchmentClient implements ClientModInitializer {
	public static final KeyBinding TRANSFORMATION_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Bewitchment.MODID + ".transformation_ability", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "itemGroup." + Bewitchment.MODID + "." + Bewitchment.MODID));

	public static final EntityModelLayer CONTRIBUTOR_HORNS_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "contributor_horns"), "main");
	public static final EntityModelLayer WITCH_ARMOR_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "witch_armor"), "main");
	public static final EntityModelLayer SPECTER_BANGLE_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "specter_bangle"), "main");
	public static final EntityModelLayer PRICKLY_BELT_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "prickly_belt"), "main");
	public static final EntityModelLayer DRUID_BAND_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "druid_band"), "main");
	public static final EntityModelLayer ZEPHYR_HARNESS_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "zephyr_harness"), "main");

	public static final EntityModelLayer BROOM_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "broom"), "main");
	public static final EntityModelLayer OWL_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "owl"), "main");
	public static final EntityModelLayer RAVEN_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "raven"), "main");
	public static final EntityModelLayer SNAKE_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "snake"), "main");
	public static final EntityModelLayer TOAD_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "toad"), "main");
	public static final EntityModelLayer GHOST_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "ghost"), "main");
	public static final EntityModelLayer VAMPIRE_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "vampire"), "main");
	public static final EntityModelLayer WEREWOLF_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "werewolf"), "main");
	public static final EntityModelLayer HELLHOUND_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "hellhound"), "main");
	public static final EntityModelLayer MALE_DEMON_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "male_demon"), "main");
	public static final EntityModelLayer FEMALE_DEMON_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "female_demon"), "main");
	public static final EntityModelLayer LEONARD_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "leonard"), "main");
	public static final EntityModelLayer BAPHOMET_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "baphomet"), "main");
	public static final EntityModelLayer LILITH_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "lilith"), "main");
	public static final EntityModelLayer HERNE_MODEL_LAYER = new EntityModelLayer(new Identifier(Bewitchment.MODID, "herne"), "main");

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
		ClientPlayNetworking.registerGlobalReceiver(SyncPoppetShelfPacket.ID, SyncPoppetShelfPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncHornedSpearPacket.ID, SyncHornedSpearPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnSmokeParticlesPacket.ID, SpawnSmokeParticlesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnPortalParticlesPacket.ID, SpawnPortalParticlesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnExplosionParticlesPacket.ID, SpawnExplosionParticlesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnSpecterBangleParticlesPacket.ID, SpawnSpecterBangleParticlesPacket::handle);
		ParticleFactoryRegistry.getInstance().register(BWParticleTypes.CAULDRON_BUBBLE, CauldronBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(BWParticleTypes.INCENSE_SMOKE, IncenseSmokeParticle.Factory::new);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xffff00, BWObjects.GOLDEN_GLYPH);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xc00000, BWObjects.FIERY_GLYPH);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0x8000a0, BWObjects.ELDRITCH_GLYPH);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> tintIndex == 1 ? ((BedBlock) state.getBlock()).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN, BWObjects.ORANGE_COFFIN, BWObjects.MAGENTA_COFFIN, BWObjects.LIGHT_BLUE_COFFIN, BWObjects.YELLOW_COFFIN, BWObjects.LIME_COFFIN, BWObjects.PINK_COFFIN, BWObjects.GRAY_COFFIN, BWObjects.LIGHT_GRAY_COFFIN, BWObjects.CYAN_COFFIN, BWObjects.PURPLE_COFFIN, BWObjects.BLUE_COFFIN, BWObjects.BROWN_COFFIN, BWObjects.GREEN_COFFIN, BWObjects.RED_COFFIN, BWObjects.BLACK_COFFIN);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? ((BedBlock) Block.getBlockFromItem(stack.getItem())).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN, BWObjects.ORANGE_COFFIN, BWObjects.MAGENTA_COFFIN, BWObjects.LIGHT_BLUE_COFFIN, BWObjects.YELLOW_COFFIN, BWObjects.LIME_COFFIN, BWObjects.PINK_COFFIN, BWObjects.GRAY_COFFIN, BWObjects.LIGHT_GRAY_COFFIN, BWObjects.CYAN_COFFIN, BWObjects.PURPLE_COFFIN, BWObjects.BLUE_COFFIN, BWObjects.BROWN_COFFIN, BWObjects.GREEN_COFFIN, BWObjects.RED_COFFIN, BWObjects.BLACK_COFFIN);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x7f0000 : 0xffffff, BWObjects.BOTTLE_OF_BLOOD);
		ModelPredicateProviderRegistry.register(BWObjects.HEDGEWITCH_HAT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> stack.getName().asString().toLowerCase().contains("faith") ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.ALCHEMIST_HAT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> stack.getName().asString().toLowerCase().contains("faith") ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.BESMIRCHED_HAT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> stack.getName().asString().toLowerCase().contains("faith") ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.NAZAR, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> stack.hasNbt() && stack.getNbt().getBoolean("Worn") ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.PRICKLY_BELT, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> stack.hasNbt() && stack.getNbt().getInt("PotionUses") > 0 ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.HORNED_SPEAR, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.TAGLOCK, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> TaglockItem.hasTaglock(stack) ? 1 : 0);
		ModelPredicateProviderRegistry.register(BWObjects.WAYSTONE, new Identifier(Bewitchment.MODID, "variant"), (stack, world, entity, seed) -> stack.hasNbt() && stack.getOrCreateNbt().contains("LocationPos") ? 1 : 0);
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.BW_CHEST, ChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.WITCH_ALTAR, ctx -> new WitchAltarBlockEntityRenderer());
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.WITCH_CAULDRON, ctx -> new WitchCauldronBlockEntityRenderer());
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.BRAZIER, ctx -> new BrazierBlockEntityRenderer());
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.POPPET_SHELF, ctx -> new PoppetShelfBlockEntityRenderer());
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.JUNIPER_CHEST, ChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.ELDER_CHEST, ChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST, ChestBlockEntityRenderer::new);
		TerraformBoatClientHelper.registerModelLayer(new Identifier(Bewitchment.MODID, "juniper"));
		TerraformBoatClientHelper.registerModelLayer(new Identifier(Bewitchment.MODID, "cypress"));
		TerraformBoatClientHelper.registerModelLayer(new Identifier(Bewitchment.MODID, "elder"));
		TerraformBoatClientHelper.registerModelLayer(new Identifier(Bewitchment.MODID, "dragons_blood"));
		EntityModelLayerRegistry.registerModelLayer(CONTRIBUTOR_HORNS_MODEL_LAYER, ContributorHornsModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WITCH_ARMOR_MODEL_LAYER, WitchArmorModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SPECTER_BANGLE_MODEL_LAYER, SpecterBangleModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PRICKLY_BELT_MODEL_LAYER, PricklyBeltModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(DRUID_BAND_MODEL_LAYER, DruidBandModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ZEPHYR_HARNESS_MODEL_LAYER, ZephyrHarnessModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BROOM_MODEL_LAYER, BroomEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.JUNIPER_BROOM, JuniperBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.CYPRESS_BROOM, CypressBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.ELDER_BROOM, ElderBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.DRAGONS_BLOOD_BROOM, DragonsBloodBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.SILVER_ARROW, SilverArrowEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.HORNED_SPEAR, HornedSpearEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(OWL_MODEL_LAYER, OwlEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.OWL, OwlEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(RAVEN_MODEL_LAYER, RavenEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.RAVEN, RavenEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(SNAKE_MODEL_LAYER, SnakeEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.SNAKE, SnakeEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(TOAD_MODEL_LAYER, ToadEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.TOAD, ToadEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(GHOST_MODEL_LAYER, GhostEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.GHOST, GhostEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(VAMPIRE_MODEL_LAYER, VampireEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.VAMPIRE, VampireEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(WEREWOLF_MODEL_LAYER, WerewolfEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.WEREWOLF, WerewolfEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(HELLHOUND_MODEL_LAYER, HellhoundEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.HELLHOUND, HellhoundEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataMale);
		EntityModelLayerRegistry.registerModelLayer(FEMALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataFemale);
		EntityRendererRegistry.register(BWEntityTypes.DEMON, DemonEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(LEONARD_MODEL_LAYER, LeonardEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.LEONARD, LeonardEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(BAPHOMET_MODEL_LAYER, BaphometEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.BAPHOMET, BaphometEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(LILITH_MODEL_LAYER, LilithEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.LILITH, LilithEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(HERNE_MODEL_LAYER, HerneEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.HERNE, HerneEntityRenderer::new);
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
		HandledScreens.register(BWScreenHandlerTypes.DEMON_SCREEN_HANDLER, DemonScreen::new);
		HandledScreens.register(BWScreenHandlerTypes.BAPHOMET_SCREEN_HANDLER, DemonScreen::new);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.JUNIPER_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_JUNIPER_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.JUNIPER_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_JUNIPER_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.JUNIPER_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_JUNIPER_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.CYPRESS_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_CYPRESS_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.CYPRESS_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_CYPRESS_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.CYPRESS_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_CYPRESS_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.ELDER_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_ELDER_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.ELDER_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_ELDER_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.ELDER_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_ELDER_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.DRAGONS_BLOOD_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_DRAGONS_BLOOD_CHEST);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.DRAGONS_BLOOD_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_DRAGONS_BLOOD_CHEST_LEFT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.DRAGONS_BLOOD_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(SpriteIdentifiers.TRAPPED_DRAGONS_BLOOD_CHEST_RIGHT);
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.JUNIPER_SIGN.getTexture()));
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.CYPRESS_SIGN.getTexture()));
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.ELDER_SIGN.getTexture()));
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, BWObjects.DRAGONS_BLOOD_SIGN.getTexture()));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.JUNIPER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.JUNIPER_CHEST.getDefaultState(), BWChestBlockEntity.Type.JUNIPER, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_JUNIPER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.TRAPPED_JUNIPER_CHEST.getDefaultState(), BWChestBlockEntity.Type.JUNIPER, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.CYPRESS_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.CYPRESS_CHEST.getDefaultState(), BWChestBlockEntity.Type.CYPRESS, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_CYPRESS_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.TRAPPED_CYPRESS_CHEST.getDefaultState(), BWChestBlockEntity.Type.CYPRESS, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.ELDER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.ELDER_CHEST.getDefaultState(), BWChestBlockEntity.Type.ELDER, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_ELDER_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.TRAPPED_ELDER_CHEST.getDefaultState(), BWChestBlockEntity.Type.ELDER, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.DRAGONS_BLOOD_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.DRAGONS_BLOOD_CHEST.getDefaultState(), BWChestBlockEntity.Type.DRAGONS_BLOOD, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST, (stack, mode, matrices, vertexConsumers, light, overlay) -> MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST, BlockPos.ORIGIN, BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST.getDefaultState(), BWChestBlockEntity.Type.DRAGONS_BLOOD, true), matrices, vertexConsumers, light, overlay));
		ArmorRenderer.register(new WitchArmorRenderer(new Identifier(Bewitchment.MODID, "textures/entity/armor/hedgewitch.png"), BWObjects.HEDGEWITCH_HAT), BWObjects.HEDGEWITCH_HOOD, BWObjects.HEDGEWITCH_HAT, BWObjects.HEDGEWITCH_ROBES, BWObjects.HEDGEWITCH_PANTS);
		ArmorRenderer.register(new WitchArmorRenderer(new Identifier(Bewitchment.MODID, "textures/entity/armor/alchemist.png"), BWObjects.ALCHEMIST_HAT), BWObjects.ALCHEMIST_HOOD, BWObjects.ALCHEMIST_HAT, BWObjects.ALCHEMIST_ROBES, BWObjects.ALCHEMIST_PANTS);
		ArmorRenderer.register(new WitchArmorRenderer(new Identifier(Bewitchment.MODID, "textures/entity/armor/besmirched.png"), BWObjects.BESMIRCHED_HAT), BWObjects.BESMIRCHED_HOOD, BWObjects.BESMIRCHED_HAT, BWObjects.BESMIRCHED_ROBES, BWObjects.BESMIRCHED_PANTS);
		ArmorRenderer.register(new WitchArmorRenderer(new Identifier(Bewitchment.MODID, "textures/entity/armor/harbinger.png"), null), BWObjects.HARBINGER);
		TrinketRendererRegistry.registerRenderer(BWObjects.NAZAR, (stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) -> {
			ItemStack copy = stack.copy();
			copy.getOrCreateNbt().putBoolean("Worn", true);
			TrinketRenderer.translateToChest(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
			matrices.translate(0, -1 / 4.25f, 1 / 48f);
			matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
			MinecraftClient.getInstance().getItemRenderer().renderItem(copy, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
		});
		TrinketRendererRegistry.registerRenderer(BWObjects.SPECTER_BANGLE, new TrinketRenderer() {
			private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/specter_bangle.png");
			private static Model model;

			@Override
			public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (model == null) {
					model = new SpecterBangleModel(getPart(SPECTER_BANGLE_MODEL_LAYER));
				}
				TrinketRenderer.translateToRightLeg(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
				model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
		TrinketRendererRegistry.registerRenderer(BWObjects.PRICKLY_BELT, new TrinketRenderer() {
			private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/prickly_belt.png");
			private static Model model;

			@Override
			public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (model == null) {
					model = new PricklyBeltModel(getPart(PRICKLY_BELT_MODEL_LAYER));
				}
				TrinketRenderer.translateToChest(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
				model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
		TrinketRendererRegistry.registerRenderer(BWObjects.HELLISH_BAUBLE, (stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) -> {
			TrinketRenderer.translateToChest(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
			matrices.translate(0, -1 / 4.25f, 1 / 48f);
			matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
		});
		TrinketRendererRegistry.registerRenderer(BWObjects.DRUID_BAND, new TrinketRenderer() {
			private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/druid_band.png");
			private static Model model;

			@Override
			public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (model == null) {
					model = new DruidBandModel(getPart(DRUID_BAND_MODEL_LAYER));
				}
				TrinketRenderer.translateToLeftLeg(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
				model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
		TrinketRendererRegistry.registerRenderer(BWObjects.ZEPHYR_HARNESS, new TrinketRenderer() {
			private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/zephyr_harness.png");
			private static Model model;

			@Override
			public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (model == null) {
					model = new ZephyrHarnessModel(getPart(ZEPHYR_HARNESS_MODEL_LAYER));
				}
				TrinketRenderer.translateToChest(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
				model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
		ClientTickEvents.END_WORLD_TICK.register(new ClientTickEvents.EndWorldTick() {
			private int transformationAbilityCooldown = 0;

			@Override
			public void onEndTick(ClientWorld world) {
				PlayerEntity player = MinecraftClient.getInstance().player;
				if (player != null) {
					if (transformationAbilityCooldown > 0) {
						transformationAbilityCooldown--;
					} else if (BewitchmentClient.TRANSFORMATION_ABILITY.isPressed()) {
						transformationAbilityCooldown = 20;
						TransformationAbilityPacket.send();
					}
					if (BWComponents.BROOM_USER_COMPONENT.get(player).isPressingForward()) {
						TogglePressingForwardPacket.send(false);
					}
					if (MinecraftClient.getInstance().options.forwardKey.isPressed() && player.getVehicle() instanceof BroomEntity) {
						TogglePressingForwardPacket.send(true);
					}
				}
			}
		});
	}

	private static ModelPart getPart(EntityModelLayer layer) {
		return MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer);
	}
}
