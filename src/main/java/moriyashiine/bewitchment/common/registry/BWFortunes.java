package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWFortunes {
	private static final Map<Fortune, Identifier> FORTUNES = new LinkedHashMap<>();
	
	public static Fortune POWER = create("power", new Fortune(true));
	public static Fortune WISDOM = create("wisdom", new Fortune(true));
	public static Fortune COURAGE = create("courage", new Fortune(true));
	public static Fortune TREASURE = create("treasure", new Fortune(true));
	public static Fortune HAWKEYE = create("hawkeye", new Fortune(true));
	public static Fortune MERCHANT = create("merchant", new Fortune(true));
	
	public static Fortune ILLNESS = create("illness", new Fortune(false));
	public static Fortune INFESTED = create("infested", new Fortune(false));
	public static Fortune THUNDERBOLT = create("thunderbolt", new Fortune(false));
	public static Fortune CLUMSINESS = create("clumsiness", new Fortune(false));
	public static Fortune ENCOUNTER = create("encounter", new Fortune(false));
	public static Fortune EXHAUSTED = create("exhausted", new Fortune(false));
	
	private static <T extends Fortune> T create(String name, T fortune) {
		FORTUNES.put(fortune, new Identifier(Bewitchment.MODID, name));
		return fortune;
	}
	
	public static void init() {
		FORTUNES.keySet().forEach(contract -> Registry.register(BWRegistries.FORTUNES, FORTUNES.get(contract), contract));
	}
}
