package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BWRegistries {
	public static final Registry<Contract> CONTRACTS = FabricRegistryBuilder.createSimple(Contract.class, new Identifier(Bewitchment.MODID, "contracts")).buildAndRegister();
}
