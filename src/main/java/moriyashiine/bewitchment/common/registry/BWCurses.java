package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.curse.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWCurses {
	private static final Map<Curse, Identifier> CURSES = new LinkedHashMap<>();
	
	public static final Curse LIGHTNING_ROD = create("lightning_rod", new Curse(Curse.Type.LESSER));
	public static final Curse UNLUCKY = create("unlucky", new Curse(Curse.Type.LESSER));
	public static final Curse FORESTS_WRATH = create("forests_wrath", new Curse(Curse.Type.LESSER));
	public static final Curse OUTRAGE = create("outrage", new Curse(Curse.Type.LESSER));
	public static final Curse MISFORTUNE = create("misfortune", new MisfortuneCurse(Curse.Type.LESSER));
	public static final Curse WEAK_LUNGS = create("weak_lungs", new WeakLungsCurse(Curse.Type.LESSER));
	public static final Curse SOLAR_HATRED = create("solar_hatred", new SolarHatredCurse(Curse.Type.LESSER));
	public static final Curse ARMY_OF_WORMS = create("army_of_worms", new Curse(Curse.Type.LESSER));
	public static final Curse COMPROMISED = create("compromised", new Curse(Curse.Type.LESSER));
	public static final Curse ARACHNOPHOBIA = create("arachnophobia", new Curse(Curse.Type.LESSER));
	
	public static final Curse INSANITY = create("insanity", new Curse(Curse.Type.GREATER));
	public static final Curse SUSCEPTIBILITY = create("susceptibility", new SusceptibilityCurse(Curse.Type.GREATER));
	public static final Curse APATHY = create("apathy", new ApathyCurse(Curse.Type.GREATER));
	
	private static <T extends Curse> T create(String name, T curse) {
		CURSES.put(curse, new Identifier(Bewitchment.MODID, name));
		return curse;
	}
	
	public static void init() {
		CURSES.keySet().forEach(curse -> Registry.register(BWRegistries.CURSES, CURSES.get(curse), curse));
	}
}
