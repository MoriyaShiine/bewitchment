/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public class BWRegistries {
	public static final Registry<RitualFunction> RITUAL_FUNCTIONS = FabricRegistryBuilder.createSimple(RitualFunction.class, Bewitchment.id("ritual_functions")).buildAndRegister();
	public static final Registry<Sigil> SIGILS = FabricRegistryBuilder.createSimple(Sigil.class, Bewitchment.id("sigils")).buildAndRegister();
	public static final Registry<Fortune> FORTUNES = FabricRegistryBuilder.createSimple(Fortune.class, Bewitchment.id("fortunes")).buildAndRegister();
	public static final Registry<Transformation> TRANSFORMATIONS = FabricRegistryBuilder.createSimple(Transformation.class, Bewitchment.id("transformations")).buildAndRegister();
	public static final Registry<Contract> CONTRACTS = FabricRegistryBuilder.createSimple(Contract.class, Bewitchment.id("contracts")).buildAndRegister();
	public static final Registry<Curse> CURSES = FabricRegistryBuilder.createSimple(Curse.class, Bewitchment.id("curses")).buildAndRegister();
}
