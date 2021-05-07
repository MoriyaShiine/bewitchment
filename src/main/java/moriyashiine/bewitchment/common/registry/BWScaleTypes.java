package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.TypedScaleModifier;

import java.util.Map;

public class BWScaleTypes {
	public static final ScaleModifier MODIFY_WIDTH_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, "modify_width", new TypedScaleModifier(() -> BWScaleTypes.MODIFY_WIDTH_TYPE));
	public static final ScaleModifier MODIFY_HEIGHT_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, "modify_height", new TypedScaleModifier(() -> BWScaleTypes.MODIFY_HEIGHT_TYPE));
	
	public static final ScaleType MODIFY_WIDTH_TYPE = register(ScaleRegistries.SCALE_TYPES, "modify_width", ScaleType.Builder.create().addDependentModifier(MODIFY_WIDTH_MODIFIER).affectsDimensions().build());
	public static final ScaleType MODIFY_HEIGHT_TYPE = register(ScaleRegistries.SCALE_TYPES, "modify_height", ScaleType.Builder.create().addDependentModifier(MODIFY_HEIGHT_MODIFIER).affectsDimensions().build());
	
	private static <T> T register(Map<Identifier, T> registry, String name, T entry) {
		return ScaleRegistries.register(registry, new Identifier(Bewitchment.MODID, name), entry);
	}
	
	public static void init() {
		ScaleType.WIDTH.getDefaultBaseValueModifiers().add(MODIFY_WIDTH_MODIFIER);
		ScaleType.HEIGHT.getDefaultBaseValueModifiers().add(MODIFY_HEIGHT_MODIFIER);
	}
}
