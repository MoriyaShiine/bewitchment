package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
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
	
	@Inject(method = "canHit", at = @At("RETURN"), cancellable = true)
	private void canHit(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && !world.isClient) {
			Entity owner = ((ProjectileEntity) (Object) this).getOwner();
			if (owner instanceof BaphometEntity && entity instanceof FireballEntity) {
				callbackInfo.setReturnValue(false);
			}
			else if (owner instanceof LilithEntity && entity instanceof WitherSkullEntity) {
				callbackInfo.setReturnValue(false);
			}
		}
	}
}
