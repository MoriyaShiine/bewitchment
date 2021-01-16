package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import org.jetbrains.annotations.Nullable;

public class BWDamageSources {
	public static final DamageSource WEDNESDAY = new WednesdayDamageSource("wednesday");
	public static final DamageSource VAMPIRE = new EmptyDamageSource("vampire");
	
	public static class MagicMob extends EntityDamageSource {
		public MagicMob(@Nullable Entity source) {
			super("mob", source);
			setUsesMagic();
			setBypassesArmor();
		}
	}
	
	public static class MagicPlayer extends EntityDamageSource {
		public MagicPlayer(@Nullable Entity source) {
			super("player", source);
			setUsesMagic();
			setBypassesArmor();
		}
	}
	
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
