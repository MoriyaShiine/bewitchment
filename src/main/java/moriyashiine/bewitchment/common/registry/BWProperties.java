/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class BWProperties {
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	public static final BooleanProperty CUT = BooleanProperty.of("cut");

	public static final BooleanProperty HAS_FRUIT = BooleanProperty.of("has_fruit");

	public static final IntProperty TYPE = IntProperty.of("type", 0, 9);

	public static final IntProperty LEVEL = IntProperty.of("level", 0, 3);
}
