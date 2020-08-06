package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class VampireHandler extends Entity {
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	public VampireHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isVampire(this) && source.isFire()) {
			callbackInfo.setReturnValue(damage(BWDamageSources.SUN, amount * 2));
		}
	}
	
	@Mixin(Entity.class)
	private static class CancelFireImmunity {
		@Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
		private void isFireImmune(CallbackInfoReturnable<Boolean> callbackInfo) {
			Object obj = this;
			//noinspection ConstantConditions
			if (BewitchmentAPI.isVampire((Entity) obj)) {
				callbackInfo.setReturnValue(false);
			}
		}
	}
}
