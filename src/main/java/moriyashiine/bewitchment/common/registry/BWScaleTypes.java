package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.Map;

public class BWScaleTypes {
	public static final ScaleType MODIFY_WIDTH_TYPE = register(ScaleRegistries.SCALE_TYPES, "modify_width", ScaleType.Builder.create().build());
	public static final ScaleType MODIFY_HEIGHT_TYPE = register(ScaleRegistries.SCALE_TYPES, "modify_height", ScaleType.Builder.create().build());
	
	public static final ScaleModifier MODIFY_WIDTH_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, "modify_width", new ScaleModifier() {
		@Override
		public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta) {
			return MODIFY_WIDTH_TYPE.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	public static final ScaleModifier MODIFY_HEIGHT_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, "modify_height", new ScaleModifier() {
		@Override
		public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta) {
			return MODIFY_HEIGHT_TYPE.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	
	private static <T> T register(Map<Identifier, T> registry, String name, T entry) {
		return ScaleRegistries.register(registry, new Identifier(Bewitchment.MODID, name), entry);
	}
	
	public static void init() {
		MODIFY_WIDTH_TYPE.getScaleChangedEvent().register(event -> {
			Entity entity = event.getEntity();
			if (entity != null) {
				boolean onGround = entity.isOnGround();
				entity.calculateDimensions();
				entity.setOnGround(onGround);
				for (ScaleType type : ScaleRegistries.SCALE_TYPES.values()) {
					ScaleData data = type.getScaleData(entity);
					if (data.getBaseValueModifiers().contains(MODIFY_HEIGHT_MODIFIER)) {
						data.markForSync(true);
					}
				}
			}
		});
		MODIFY_HEIGHT_TYPE.getScaleChangedEvent().register(event -> {
			Entity entity = event.getEntity();
			if (entity != null) {
				boolean onGround = entity.isOnGround();
				entity.calculateDimensions();
				entity.setOnGround(onGround);
				for (ScaleType type : ScaleRegistries.SCALE_TYPES.values()) {
					ScaleData data = type.getScaleData(entity);
					if (data.getBaseValueModifiers().contains(MODIFY_HEIGHT_MODIFIER)) {
						data.markForSync(true);
					}
				}
			}
		});
		ScaleType.WIDTH.getDefaultBaseValueModifiers().add(MODIFY_WIDTH_MODIFIER);
		ScaleType.HEIGHT.getDefaultBaseValueModifiers().add(MODIFY_HEIGHT_MODIFIER);
	}
}
