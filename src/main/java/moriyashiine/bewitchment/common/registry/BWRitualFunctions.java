package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWRitualFunctions {
	private static final Map<RitualFunction, Identifier> RITUAL_FUNCTIONS = new LinkedHashMap<>();
	
	public static RitualFunction TURN_TO_DAY = create("turn_to_day", new RitualFunction(ParticleTypes.FLAME, null));
	public static RitualFunction CLEANSE = create("cleanse", new RitualFunction(ParticleTypes.ITEM_SNOWBALL, null));
	public static RitualFunction PREVENT_DAMAGE = create("prevent_damage", new RitualFunction(ParticleTypes.CRIT, null));
	public static RitualFunction TURN_TO_NIGHT = create("turn_to_night", new RitualFunction(ParticleTypes.ENCHANTED_HIT, null));
	public static RitualFunction DESTROY_LIGHTS = create("destroy_lights", new RitualFunction(ParticleTypes.SMOKE, null));
	public static RitualFunction CLEAR_ENCHANTMENTS = create("clear_enchantments", new RitualFunction(ParticleTypes.ENCHANTED_HIT, null));
	public static RitualFunction START_RAIN = create("start_rain", new RitualFunction(ParticleTypes.DRIPPING_WATER, null));
	public static RitualFunction MAKE_ENTITIES_WET = create("make_entities_wet", new RitualFunction(ParticleTypes.SPLASH, null));
	public static RitualFunction ENCHANT = create("enchant", new RitualFunction(ParticleTypes.ENCHANT, null));
	public static RitualFunction STOP_RAIN = create("stop_rain", new RitualFunction(ParticleTypes.FLAME, null));
	public static RitualFunction SMELT_ITEMS = create("smelt_items", new RitualFunction(ParticleTypes.FLAME, null));
	public static RitualFunction DRAIN_WATER = create("drain_water", new RitualFunction(ParticleTypes.SMOKE, null));
	public static RitualFunction TELEPORT_ENTITIES = create("teleport_entities", new RitualFunction(ParticleTypes.PORTAL, null));
	public static RitualFunction GROW = create("grow", new RitualFunction(ParticleTypes.HAPPY_VILLAGER, null));
	public static RitualFunction SEARCH_BLOCKS = create("search_blocks", new RitualFunction(ParticleTypes.MYCELIUM, null));
	public static RitualFunction SPAWN_LIGHTNING = create("spawn_lightning", new RitualFunction(ParticleTypes.CLOUD, null));
	public static RitualFunction PUSH_MOBS = create("push_mobs", new RitualFunction(ParticleTypes.CLOUD, null));
	public static RitualFunction DESTROY_PLANTS = create("destroy_plants", new RitualFunction(ParticleTypes.CLOUD, null));
	
	public static RitualFunction SUMMON_WITHER = create("summon_wither", new RitualFunction(ParticleTypes.FLAME, null));
	public static RitualFunction SUMMON_DEMON = create("summon_demon", new RitualFunction(ParticleTypes.FLAME, livingEntity -> BWTags.HAS_BLOOD.contains(livingEntity.getType())));
	public static RitualFunction SUMMON_LEONARD = create("summon_leonard", new RitualFunction(ParticleTypes.FLAME, livingEntity -> livingEntity instanceof SheepEntity));
	public static RitualFunction SUMMON_BAPHOMET = create("summon_baphomet", new RitualFunction(ParticleTypes.FLAME, livingEntity -> livingEntity instanceof SheepEntity));
	
	private static <T extends RitualFunction> T create(String name, T ritualFunction) {
		RITUAL_FUNCTIONS.put(ritualFunction, new Identifier(Bewitchment.MODID, name));
		return ritualFunction;
	}
	
	public static void init() {
		RITUAL_FUNCTIONS.keySet().forEach(contract -> Registry.register(BWRegistries.RITUAL_FUNCTIONS, RITUAL_FUNCTIONS.get(contract), contract));
	}
}
