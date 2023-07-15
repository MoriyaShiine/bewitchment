/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BWRegistries {
	public static final RegistryKey<Registry<RitualFunction>> RITUAL_FUNCTION_KEY = RegistryKey.ofRegistry(Bewitchment.id("ritual_function"));
	public static final Registry<RitualFunction> RITUAL_FUNCTION = FabricRegistryBuilder.createSimple(RITUAL_FUNCTION_KEY).buildAndRegister();

	public static final RegistryKey<Registry<Sigil>> SIGIL_KEY = RegistryKey.ofRegistry(Bewitchment.id("sigil"));
	public static final Registry<Sigil> SIGIL = FabricRegistryBuilder.createSimple(SIGIL_KEY).buildAndRegister();

	public static final RegistryKey<Registry<Fortune>> FORTUNE_KEY = RegistryKey.ofRegistry(Bewitchment.id("fortune"));
	public static final Registry<Fortune> FORTUNE = FabricRegistryBuilder.createSimple(FORTUNE_KEY).buildAndRegister();

	public static final RegistryKey<Registry<Transformation>> TRANSFORMATION_KEY = RegistryKey.ofRegistry(Bewitchment.id("transformation"));
	public static final Registry<Transformation> TRANSFORMATION = FabricRegistryBuilder.createSimple(TRANSFORMATION_KEY).buildAndRegister();

	public static final RegistryKey<Registry<Contract>> CONTRACT_KEY = RegistryKey.ofRegistry(Bewitchment.id("contract"));
	public static final Registry<Contract> CONTRACT = FabricRegistryBuilder.createSimple(CONTRACT_KEY).buildAndRegister();

	public static final RegistryKey<Registry<Curse>> CURSE_KEY = RegistryKey.ofRegistry(Bewitchment.id("curse"));
	public static final Registry<Curse> CURSE = FabricRegistryBuilder.createSimple(CURSE_KEY).buildAndRegister();
}
