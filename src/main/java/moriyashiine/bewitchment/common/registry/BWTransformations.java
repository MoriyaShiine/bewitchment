package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.transformation.VampireTransformation;
import moriyashiine.bewitchment.common.transformation.WerewolfTransformation;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWTransformations {
	private static final Map<Transformation, Identifier> TRANSFORMATIONS = new LinkedHashMap<>();
	
	public static final Transformation HUMAN = create("human", new Transformation());
	public static final Transformation VAMPIRE = create("vampire", new VampireTransformation());
	public static final Transformation WEREWOLF = create("werewolf", new WerewolfTransformation());
	
	private static <T extends Transformation> T create(String name, T transformation) {
		TRANSFORMATIONS.put(transformation, new Identifier(Bewitchment.MODID, name));
		return transformation;
	}
	
	public static void init() {
		TRANSFORMATIONS.keySet().forEach(contract -> Registry.register(BWRegistries.TRANSFORMATIONS, TRANSFORMATIONS.get(contract), contract));
	}
}
