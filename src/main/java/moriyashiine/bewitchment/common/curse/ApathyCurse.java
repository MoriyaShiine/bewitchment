package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.api.registry.Curse;
import net.minecraft.entity.LivingEntity;

public class ApathyCurse extends Curse {
	public ApathyCurse(Type type) {
		super(type);
	}
	
	@Override
	public void tick(LivingEntity target) {
		MagicAccessor.of(target).ifPresent(magicAccessor -> {
			if (magicAccessor.getMagic() > 0) {
				magicAccessor.setMagic(0);
			}
		});
	}
}
