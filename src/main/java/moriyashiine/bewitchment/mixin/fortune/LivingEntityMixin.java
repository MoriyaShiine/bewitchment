package moriyashiine.bewitchment.mixin.fortune;

import moriyashiine.bewitchment.api.interfaces.entity.FortuneAccessor;
import moriyashiine.bewitchment.common.registry.BWFortunes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient) {
			Entity trueSource = source.getAttacker();
			if (amount > 0 && trueSource instanceof FortuneAccessor && ((FortuneAccessor) trueSource).getFortune() != null && ((FortuneAccessor) trueSource).getFortune().fortune == BWFortunes.HAWKEYE && source.isProjectile()) {
				((FortuneAccessor) trueSource).getFortune().duration = 0;
				amount *= 3;
			}
		}
		return amount;
	}
}
