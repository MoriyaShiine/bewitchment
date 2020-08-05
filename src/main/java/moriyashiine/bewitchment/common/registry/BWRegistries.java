package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.entry.RitualFunction;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BWRegistries {
	public static final Registry<RitualFunction> ritual_function = FabricRegistryBuilder.createSimple(RitualFunction.class, new Identifier(Bewitchment.MODID, "ritual_function")).buildAndRegister();
}
