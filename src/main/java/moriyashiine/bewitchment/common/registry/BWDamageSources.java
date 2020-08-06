package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;

public class BWDamageSources {
	public static final DamageSource SUN = new DamageSource("sun") {
		@Override
		public Text getDeathMessage(LivingEntity entity) {
			return DamageSource.ON_FIRE.getDeathMessage(entity);
		}
	}.setBypassesArmor();
}
