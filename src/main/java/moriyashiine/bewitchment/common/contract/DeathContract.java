package moriyashiine.bewitchment.common.contract;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class DeathContract extends Contract {
	public DeathContract(boolean canBeGiven) {
		super(canBeGiven);
	}
	
	public void tick(LivingEntity target, boolean includeNegative) {
		if (target.damage(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE) && target.isDead()) {
			ContractAccessor.of(target).ifPresent(contractAccessor -> contractAccessor.removeContract(this));
		}
	}
}
