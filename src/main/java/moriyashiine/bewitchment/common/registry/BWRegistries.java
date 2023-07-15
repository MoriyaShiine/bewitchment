/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BWRegistries {
	public static final Registry<RitualFunction> RITUAL_FUNCTIONS = FabricRegistryBuilder.createSimple(RitualFunction.class, new Identifier(Bewitchment.MOD_ID, "ritual_functions")).buildAndRegister();
	public static final Registry<Sigil> SIGILS = FabricRegistryBuilder.createSimple(Sigil.class, new Identifier(Bewitchment.MOD_ID, "sigils")).buildAndRegister();
	public static final Registry<Fortune> FORTUNES = FabricRegistryBuilder.createSimple(Fortune.class, new Identifier(Bewitchment.MOD_ID, "fortunes")).buildAndRegister();
	public static final Registry<Transformation> TRANSFORMATIONS = FabricRegistryBuilder.createSimple(Transformation.class, new Identifier(Bewitchment.MOD_ID, "transformations")).buildAndRegister();
	public static final Registry<Contract> CONTRACTS = FabricRegistryBuilder.createSimple(Contract.class, new Identifier(Bewitchment.MOD_ID, "contracts")).buildAndRegister();
	public static final Registry<Curse> CURSES = FabricRegistryBuilder.createSimple(Curse.class, new Identifier(Bewitchment.MOD_ID, "curses")).buildAndRegister();
}
