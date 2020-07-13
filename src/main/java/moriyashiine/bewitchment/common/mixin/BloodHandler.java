package moriyashiine.bewitchment.common.mixin;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.misc.interfaces.BloodAccessor;
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
public abstract class BloodHandler extends Entity implements BloodAccessor {
	@Shadow
	public abstract boolean isSleeping();
	
	public BloodHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void regenerateBlood(CallbackInfo callbackInfo) {
		if (hasBlood(this) && !BWUtil.isVampire(this) && getBlood() < MAX_BLOOD && world.random.nextFloat() < (isSleeping() ? 1 / 20f : 1 / 200f)) {
			fillBlood(1, false);
		}
	}
}