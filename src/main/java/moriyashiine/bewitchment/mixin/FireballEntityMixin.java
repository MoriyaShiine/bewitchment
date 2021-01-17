package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireballEntity.class)
public class FireballEntityMixin extends AbstractFireballEntity {
	public FireballEntityMixin(EntityType<? extends AbstractFireballEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "onEntityHit", at = @At("HEAD"))
	private void onEntityHit(EntityHitResult result, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			Entity entity = result.getEntity();
			if (getOwner() instanceof BaphometEntity && entity instanceof LivingEntity) {
				((LivingEntity) entity).removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
			}
		}
	}
}
