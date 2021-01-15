package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.damage.DamageSource;

public class BWDamageSources {
	public static final DamageSource WEDNESDAY = new WednesdayDamageSource("wednesday");
	public static final DamageSource VAMPIRE = new EmptyDamageSource("vampire");
	
	private static class WednesdayDamageSource extends DamageSource {
		protected WednesdayDamageSource(String name) {
			super(name);
			setBypassesArmor();
			setUnblockable();
			setOutOfWorld();
		}
	}
	
	private static class EmptyDamageSource extends DamageSource {
		public EmptyDamageSource(String name) {
			super(name);
		}
	}
}
