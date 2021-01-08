package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWSigils {
	private static final Map<Sigil, Identifier> SIGILS = new LinkedHashMap<>();
	
	public static final Sigil MENDING = create("mending", new Sigil(true));
	public static final Sigil CLEANSING = create("cleansing", new Sigil(true));
	public static final Sigil JUDGMENT = create("judgment", new Sigil(true));
	public static final Sigil DECAY = create("decay", new Sigil(true));
	public static final Sigil SHRIEKING = create("shrieking", new Sigil(true));
	public static final Sigil SENTINEL = create("sentinel", new Sigil(true));
	
	public static final Sigil SLIPPERY = create("slippery", new Sigil(false));
	public static final Sigil SHADOWS = create("shadows", new Sigil(false));
	public static final Sigil EXTENDING = create("extending", new Sigil(false));
	public static final Sigil SMELLY = create("smelly", new Sigil(false));
	public static final Sigil RUIN = create("ruin", new Sigil(false));
	public static final Sigil DARKNESS = create("darkness", new Sigil(false));
	
	private static <T extends Sigil> T create(String name, T sigil) {
		SIGILS.put(sigil, new Identifier(Bewitchment.MODID, name));
		return sigil;
	}
	
	public static void init() {
		SIGILS.keySet().forEach(sigil -> Registry.register(BWRegistries.SIGILS, SIGILS.get(sigil), sigil));
	}
}
