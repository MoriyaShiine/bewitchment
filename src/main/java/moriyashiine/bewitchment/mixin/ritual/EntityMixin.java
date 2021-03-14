package moriyashiine.bewitchment.mixin.ritual;

import moriyashiine.bewitchment.common.entity.interfaces.WetAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements WetAccessor {
	private static final TrackedData<Integer> WET_TIMER = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.INTEGER);
	
	@Shadow
	public World world;
	
	@Shadow
	@Final
	protected DataTracker dataTracker;
	
	@Override
	public int getWetTimer() {
		return dataTracker.get(WET_TIMER);
	}
	
	@Override
	public void setWetTimer(int wetTimer) {
		dataTracker.set(WET_TIMER, wetTimer);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getWetTimer() > 0) {
			setWetTimer(getWetTimer() - 1);
		}
	}
	
	@Inject(method = "isWet", at = @At("RETURN"), cancellable = true)
	private void isWet(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "isTouchingWaterOrRain", at = @At("RETURN"), cancellable = true)
	private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && getWetTimer() > 0) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "fromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		setWetTimer(tag.getInt("WetTimer"));
	}
	
	@Inject(method = "toTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfoReturnable<Tag> callbackInfo) {
		tag.putInt("WetTimer", getWetTimer());
	}
	
	@Inject(method = "<init>", at = @At("RETURN"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(WET_TIMER, 0);
	}
}
