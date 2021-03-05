package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class, LivingEntity.class, PlayerEntity.class, ServerPlayerEntity.class})
public class VampireHackMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private DamageSource modifyDamage(DamageSource source) {
		if (source.isFire() && BewitchmentAPI.isVampire((Entity) (Object) this, true)) {
			return BWDamageSources.SUN;
		}
		return source;
	}
	
	@Mixin(LivingEntity.class)
	static class LivingEntityMixin {
		@ModifyVariable(method = "damage", at = @At("HEAD"))
		private float modifyDamage(float amount, DamageSource source) {
			if (source == BWDamageSources.SUN) {
				amount *= 2;
			}
			return amount;
		}
	}
	
	@Mixin(PlayerEntity.class)
	static abstract class PlayerEntityMixin extends LivingEntity {
		@Shadow
		public abstract SoundCategory getSoundCategory();
		
		protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
			super(entityType, world);
		}
		
		@Inject(method = "getHurtSound", at = @At("HEAD"))
		private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> callbackInfo) {
			if (source == BWDamageSources.SUN) {
				world.playSound(null, getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, getSoundCategory(), getSoundVolume(), getSoundPitch());
			}
		}
	}
}
