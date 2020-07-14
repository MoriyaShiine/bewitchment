package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.entry.RitualFunction;
import moriyashiine.bewitchment.common.ritualfunction.MakeDayRitualFunction;
import moriyashiine.bewitchment.common.ritualfunction.MakeNightRitualFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWRitualTypes {
	private static final Map<RitualFunction, Identifier> RITUAL_FUNCTIONS = new LinkedHashMap<>();
	
	public static final RitualFunction crafting = create("crafting", new RitualFunction());
	public static final RitualFunction make_day = create("make_day", new MakeDayRitualFunction());
	public static final RitualFunction make_night = create("make_night", new MakeNightRitualFunction());
	
	private static RitualFunction create(String name, RitualFunction function) {
		RITUAL_FUNCTIONS.put(function, new Identifier(Bewitchment.MODID, name));
		return function;
	}
	
	public static void init() {
		RITUAL_FUNCTIONS.keySet().forEach(function -> Registry.register(BWRegistries.ritual_function, RITUAL_FUNCTIONS.get(function), function));
	}
}