package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BWRegistries {
	public static final Registry<RitualFunction> RITUAL_FUNCTIONS = FabricRegistryBuilder.createSimple(RitualFunction.class, new Identifier(Bewitchment.MODID, "ritual_functions")).buildAndRegister();
	public static final Registry<Fortune> FORTUNES = FabricRegistryBuilder.createSimple(Fortune.class, new Identifier(Bewitchment.MODID, "fortunes")).buildAndRegister();
	public static final Registry<Contract> CONTRACTS = FabricRegistryBuilder.createSimple(Contract.class, new Identifier(Bewitchment.MODID, "contracts")).buildAndRegister();
}
