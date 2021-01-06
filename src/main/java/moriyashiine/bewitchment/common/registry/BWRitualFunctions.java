package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWRitualFunctions {
	private static final Map<RitualFunction, Identifier> RITUAL_FUNCTIONS = new LinkedHashMap<>();
	
	public static RitualFunction TURN_TO_DAY = create("turn_to_day", new RitualFunction(ParticleTypes.FLAME));
	public static RitualFunction CLEANSE = create("cleanse", new RitualFunction(ParticleTypes.ITEM_SNOWBALL));
	public static RitualFunction PREVENT_DAMAGE = create("prevent_damage", new RitualFunction(ParticleTypes.CRIT));
	public static RitualFunction TURN_TO_NIGHT = create("turn_to_night", new RitualFunction(ParticleTypes.ENCHANTED_HIT));
	public static RitualFunction DESTROY_LIGHTS = create("destroy_lights", new RitualFunction(ParticleTypes.SMOKE));
	public static RitualFunction CLEAR_ENCHANTMENTS = create("clear_enchantments", new RitualFunction(ParticleTypes.ENCHANTED_HIT));
	public static RitualFunction START_RAIN = create("start_rain", new RitualFunction(ParticleTypes.DRIPPING_WATER));
	public static RitualFunction MAKE_ENTITIES_WET = create("make_entities_wet", new RitualFunction(ParticleTypes.SPLASH));
	public static RitualFunction ENCHANT = create("enchant", new RitualFunction(ParticleTypes.ENCHANT));
	public static RitualFunction STOP_RAIN = create("stop_rain", new RitualFunction(ParticleTypes.FLAME));
	public static RitualFunction SMELT_ITEMS = create("smelt_items", new RitualFunction(ParticleTypes.FLAME));
	public static RitualFunction DRAIN_WATER = create("drain_water", new RitualFunction(ParticleTypes.SMOKE));
	public static RitualFunction TELEPORT_ENTITIES = create("teleport_entities", new RitualFunction(ParticleTypes.PORTAL));
	public static RitualFunction GROW = create("grow", new RitualFunction(ParticleTypes.HAPPY_VILLAGER));
	public static RitualFunction SEARCH_BLOCKS = create("search_blocks", new RitualFunction(ParticleTypes.MYCELIUM));
	public static RitualFunction SPAWN_LIGHTNING = create("spawn_lightning", new RitualFunction(ParticleTypes.CLOUD));
	public static RitualFunction PUSH_MOBS = create("push_mobs", new RitualFunction(ParticleTypes.CLOUD));
	public static RitualFunction DESTROY_PLANTS = create("destroy_plants", new RitualFunction(ParticleTypes.CLOUD));
	
	private static <T extends RitualFunction> T create(String name, T ritualFunction) {
		RITUAL_FUNCTIONS.put(ritualFunction, new Identifier(Bewitchment.MODID, name));
		return ritualFunction;
	}
	
	public static void init() {
		RITUAL_FUNCTIONS.keySet().forEach(contract -> Registry.register(BWRegistries.RITUAL_FUNCTIONS, RITUAL_FUNCTIONS.get(contract), contract));
	}
}
