package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.damage.DamageSource;

public class BWDamageSources {
	public static final DamageSource WEDNESDAY = new WednesdayDamageSource("wednesday");
	
	private static class WednesdayDamageSource extends DamageSource {
		protected WednesdayDamageSource(String name) {
			super(name);
			setBypassesArmor();
			setUnblockable();
			setOutOfWorld();
		}
	}
}
