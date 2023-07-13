/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.fortune.*;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWFortunes {
	private static final Map<Fortune, Identifier> FORTUNES = new LinkedHashMap<>();

	public static final Fortune POWER = create("power", new PowerFortune(true));
	public static final Fortune WISDOM = create("wisdom", new WisdomFortune(true));
	public static final Fortune COURAGE = create("courage", new CourageFortune(true));
	public static final Fortune TREASURE = create("treasure", new Fortune(true));
	public static final Fortune HAWKEYE = create("hawkeye", new Fortune(true));
	public static final Fortune MERCHANT = create("merchant", new MerchantFortune(true));

	public static final Fortune ILLNESS = create("illness", new IllnessFortune(false));
	public static final Fortune INFESTED = create("infested", new Fortune(false));
	public static final Fortune THUNDERBOLT = create("thunderbolt", new ThunderboltFortune(false));
	public static final Fortune CLUMSINESS = create("clumsiness", new ClumsinessFortune(false));
	public static final Fortune ENCOUNTER = create("encounter", new EncounterFortune(false));
	public static final Fortune EXHAUSTED = create("exhausted", new ExhaustedFortune(false));

	private static <T extends Fortune> T create(String name, T fortune) {
		FORTUNES.put(fortune, new Identifier(Bewitchment.MODID, name));
		return fortune;
	}

	public static void init() {
		FORTUNES.keySet().forEach(contract -> Registry.register(BWRegistries.FORTUNES, FORTUNES.get(contract), contract));
	}
}
