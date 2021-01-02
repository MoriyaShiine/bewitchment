package moriyashiine.bewitchment.common;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.network.packet.CauldronTeleportPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static BWConfig config;
	
	public static final ItemGroup BEWITCHMENT_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(BWObjects.ATHAME));
	
	@Override
	public void onInitialize() {
		AutoConfig.register(BWConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BWConfig.class).getConfig();
		ServerSidePacketRegistry.INSTANCE.register(CauldronTeleportPacket.ID, CauldronTeleportPacket::handle);
		BWObjects.init();
		BWBlockEntityTypes.init();
		BWEntityTypes.init();
		BWStatusEffects.init();
		BWEnchantments.init();
		BWContracts.init();
		BWSoundEvents.init();
		BWParticleTypes.init();
		BWRecipeTypes.init();
		BWWorldGenerators.init();
		CommandRegistrationCallback.EVENT.register(BWCommands::init);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.STONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.PRISMARINE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.NETHER_BRICK_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.BLACKSTONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.GOLDEN_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.END_STONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.OBSIDIAN_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.PURPUR_WITCH_ALTAR);
	}
}
