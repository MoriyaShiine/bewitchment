package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExplosiveProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin extends Entity {
	public ExplosiveProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "method_26958", at = @At("HEAD"), cancellable = true)
	private void method_26958(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient && ((ProjectileEntity) (Object) this).getOwner() instanceof BaphometEntity && entity instanceof FireballEntity) {
			callbackInfo.setReturnValue(false);
		}
	}
}
