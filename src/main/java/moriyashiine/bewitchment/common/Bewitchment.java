package moriyashiine.bewitchment.common;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.AthameDropEntry;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWWorldGenerators;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static BWConfig config;
	
	public static final ItemGroup BEWITCHMENT_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(Items.STONE));
	
	@Override
	public void onInitialize() {
		AutoConfig.register(BWConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BWConfig.class).getConfig();
		BWObjects.init();
		BWBlockEntityTypes.init();
		BWEntityTypes.init();
		BWWorldGenerators.init();
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, BWObjects.OAK_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD, BWObjects.OAK_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, BWObjects.OAK_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD, BWObjects.OAK_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, BWObjects.SPRUCE_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD, BWObjects.SPRUCE_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, BWObjects.BIRCH_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD, BWObjects.BIRCH_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(BWObjects.JUNIPER_LOG, BWObjects.STRIPPED_JUNIPER_LOG, BWObjects.JUNIPER_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(BWObjects.JUNIPER_WOOD, BWObjects.STRIPPED_JUNIPER_WOOD, BWObjects.JUNIPER_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(BWObjects.CYPRESS_LOG, BWObjects.STRIPPED_CYPRESS_LOG, BWObjects.CYPRESS_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(BWObjects.CYPRESS_WOOD, BWObjects.STRIPPED_CYPRESS_WOOD, BWObjects.CYPRESS_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(BWObjects.ELDER_LOG, BWObjects.STRIPPED_ELDER_LOG, BWObjects.ELDER_BARK));
		BewitchmentAPI.ATHAME_DROP_ENTRIES.add(new AthameDropEntry(BWObjects.ELDER_WOOD, BWObjects.STRIPPED_ELDER_WOOD, BWObjects.ELDER_BARK));
	}
}
