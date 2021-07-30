package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.component.MinionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUuid();
	
	@Shadow
	public World world;
	
	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && !world.isClient && (Object) this instanceof MobEntity mob) {
			Entity attacker = source.getAttacker();
			if (attacker instanceof LivingEntity) {
				if (attacker.getUuid().equals(MinionComponent.get(mob).getMaster())) {
					callbackInfo.setReturnValue(true);
					return;
				}
				if (attacker instanceof MobEntity mobAttacker && getUuid().equals(MinionComponent.get(mobAttacker).getMaster())) {
					callbackInfo.setReturnValue(true);
				}
			}
		}
	}
}
