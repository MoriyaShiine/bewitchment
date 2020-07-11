package moriyashiine.bewitchment.common.block;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class BWProperties {
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	public static final BooleanProperty FILLED = BooleanProperty.of("filled");
	public static final BooleanProperty CUT = BooleanProperty.of("cut");
	public static final IntProperty CHALK_VARIANT = IntProperty.of("chalk_variant", 0, 5);
	public static final BooleanProperty ALTAR_CORE = BooleanProperty.of("altar_core");
}