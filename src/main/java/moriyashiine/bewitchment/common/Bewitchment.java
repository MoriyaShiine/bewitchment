package moriyashiine.bewitchment.common;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.CurseCallback;
import moriyashiine.bewitchment.common.network.packet.CauldronTeleportPacket;
import moriyashiine.bewitchment.common.network.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static BWConfig config;
	
	public static final ItemGroup BEWITCHMENT_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(BWObjects.ATHAME));
	
	@Override
	public void onInitialize() {
		AutoConfig.register(BWConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BWConfig.class).getConfig();
		TrinketSlots.addSlot(SlotGroups.CHEST, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
		TrinketSlots.addSlot(SlotGroups.LEGS, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));
		TrinketSlots.addSlot(SlotGroups.FEET, Slots.AGLET, new Identifier("trinkets", "textures/item/empty_trinket_slot_aglet.png"));
		ServerPlayNetworking.registerGlobalReceiver(CauldronTeleportPacket.ID, CauldronTeleportPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(TransformationAbilityPacket.ID, TransformationAbilityPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(TogglePressingForwardPacket.ID, TogglePressingForwardPacket::handle);
		BWObjects.init();
		BWBlockEntityTypes.init();
		BWEntityTypes.init();
		BWStatusEffects.init();
		BWEnchantments.init();
		BWRitualFunctions.init();
		BWFortunes.init();
		BWSigils.init();
		BWCurses.init();
		BWContracts.init();
		BWTransformations.init();
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

		CurseCallback.EVENT.register((caster, target, curse, brazier) -> {
			if (curse == BWCurses.APATHY){
				System.out.println("ur stinky now hah");
				target = caster;
				return ActionResult.SUCCESS;
			}
			return curse == BWCurses.INSANITY ? ActionResult.FAIL : ActionResult.PASS;
		});
	}
}
