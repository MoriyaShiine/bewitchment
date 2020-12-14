package moriyashiine.bewitchment.common;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static final ItemGroup BEWITCHMENT_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(Items.STONE));
	
	@Override
	public void onInitialize() {
		BWObjects.init();
		BWBlockEntityTypes.init();
		BWEntityTypes.init();
	}
}
