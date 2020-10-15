package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.accessor.BloodAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class BloodHandler extends Entity {
	@Shadow
	public abstract boolean isSleeping();
	
	public BloodHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void regenerateBlood(CallbackInfo callbackInfo) {
		BloodAccessor.of(this).ifPresent(bloodAccessor -> {
			if (!BewitchmentAPI.isVampire(this) && bloodAccessor.getBlood() < BloodAccessor.MAX_BLOOD && world.random.nextFloat() < (isSleeping() ? 1 / 20f : 1 / 200f)) {
				bloodAccessor.fillBlood(1, false);
			}
		});
	}
}
