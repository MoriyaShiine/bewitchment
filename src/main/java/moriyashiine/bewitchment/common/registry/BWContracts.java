package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.contract.DeathContract;
import moriyashiine.bewitchment.common.contract.PestilenceContract;
import moriyashiine.bewitchment.common.contract.SlothContract;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWContracts {
	private static final Map<Contract, Identifier> CONTRACTS = new LinkedHashMap<>();
	
	public static final Contract LUST = create("lust", new Contract());
	public static final Contract GLUTTONY = create("gluttony", new Contract());
	public static final Contract GREED = create("greed", new Contract());
	public static final Contract SLOTH = create("sloth", new SlothContract());
	public static final Contract WRATH = create("wrath", new Contract());
	public static final Contract ENVY = create("envy", new Contract());
	public static final Contract PRIDE = create("pride", new Contract());
	
	public static final Contract FAMINE = create("famine", new Contract());
	public static final Contract PESTILENCE = create("pestilence", new PestilenceContract());
	public static final Contract WAR = create("war", new Contract());
	public static final Contract DEATH = create("death", new DeathContract());
	
	public static final Contract HERESY = create("heresy", new Contract());
	public static final Contract VIOLENCE = create("violence", new Contract());
	public static final Contract FRAUD = create("fraud", new Contract());
	public static final Contract TREACHERY = create("treachery", new Contract());
	
	private static <T extends Contract> T create(String name, T contract) {
		CONTRACTS.put(contract, new Identifier(Bewitchment.MODID, name));
		return contract;
	}
	
	public static void init() {
		CONTRACTS.keySet().forEach(contract -> Registry.register(BWRegistries.CONTRACTS, CONTRACTS.get(contract), contract));
	}
}
