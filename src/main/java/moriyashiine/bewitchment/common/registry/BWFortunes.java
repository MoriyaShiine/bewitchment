package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWFortunes {
	private static final Map<Fortune, Identifier> FORTUNES = new LinkedHashMap<>();
	
	public static final Fortune POWER = create("power", new Fortune(true));
	public static final Fortune WISDOM = create("wisdom", new Fortune(true));
	public static final Fortune COURAGE = create("courage", new Fortune(true));
	public static final Fortune TREASURE = create("treasure", new Fortune(true));
	public static final Fortune HAWKEYE = create("hawkeye", new Fortune(true));
	public static final Fortune MERCHANT = create("merchant", new Fortune(true));
	
	public static final Fortune ILLNESS = create("illness", new Fortune(false));
	public static final Fortune INFESTED = create("infested", new Fortune(false));
	public static final Fortune THUNDERBOLT = create("thunderbolt", new Fortune(false));
	public static final Fortune CLUMSINESS = create("clumsiness", new Fortune(false));
	public static final Fortune ENCOUNTER = create("encounter", new Fortune(false));
	public static final Fortune EXHAUSTED = create("exhausted", new Fortune(false));
	
	private static <T extends Fortune> T create(String name, T fortune) {
		FORTUNES.put(fortune, new Identifier(Bewitchment.MODID, name));
		return fortune;
	}
	
	public static void init() {
		FORTUNES.keySet().forEach(contract -> Registry.register(BWRegistries.FORTUNES, FORTUNES.get(contract), contract));
	}
}
