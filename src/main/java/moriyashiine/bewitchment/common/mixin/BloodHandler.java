package moriyashiine.bewitchment.common.mixin;

import moriyashiine.bewitchment.common.misc.BWDataTrackers;
import moriyashiine.bewitchment.common.misc.BWUtil;
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
public abstract class BloodHandler extends Entity
{
	@Shadow
	public abstract boolean isSleeping();
	
	public BloodHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void regenerateBlood(CallbackInfo callbackInfo)
	{
		if (BWDataTrackers.hasBlood(this) && !BWUtil.isVampire(this) && BWDataTrackers.getBlood(this) < BWDataTrackers.MAX_BLOOD && this.world.random.nextFloat() < (isSleeping() ? 1/20f : 1/200f))
		{
			BWDataTrackers.fillBlood(this, 1, false);
		}
	}
}