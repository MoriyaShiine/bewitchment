package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWContracts {
	private static final Map<Contract, Identifier> CONTRACTS = new LinkedHashMap<>();
	
	public static Contract LUST = create("lust", new Contract());
	public static Contract GLUTTONY = create("gluttony", new Contract());
	public static Contract GREED = create("greed", new Contract());
	public static Contract SLOTH = create("sloth", new Contract());
	public static Contract WRATH = create("wrath", new Contract());
	public static Contract ENVY = create("envy", new Contract());
	public static Contract PRIDE = create("pride", new Contract());
	
	public static Contract FAMINE = create("famine", new Contract());
	public static Contract PESTILENCE = create("pestilence", new Contract());
	public static Contract WAR = create("war", new Contract());
	public static Contract DEATH = create("death", new Contract());
	
	//	public static Contract HERESY = create("heresy", new Contract());
	//	public static Contract VIOLENCE = create("violence", new Contract());
	//	public static Contract FRAUD = create("fraud", new Contract());
	//	public static Contract TREACHERY = create("treachery", new Contract());
	
	private static <T extends Contract> T create(String name, T contract) {
		CONTRACTS.put(contract, new Identifier(Bewitchment.MODID, name));
		return contract;
	}
	
	public static void init() {
		CONTRACTS.keySet().forEach(contract -> Registry.register(BWRegistries.CONTRACTS, CONTRACTS.get(contract), contract));
	}
}
