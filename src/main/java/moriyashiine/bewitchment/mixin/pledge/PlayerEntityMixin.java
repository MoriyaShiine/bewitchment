package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.common.entity.interfaces.PledgeAccessor;
import moriyashiine.bewitchment.common.registry.BWPledges;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PledgeAccessor {
	private static final TrackedData<String> PLEDGE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public String getPledge() {
		return dataTracker.get(PLEDGE);
	}
	
	@Override
	public void setPledge(String pledge) {
		dataTracker.set(PLEDGE, pledge);
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		String pledge = nbt.getString("Pledge");
		if (pledge.isEmpty()) {
			pledge = BWPledges.NONE;
		}
		setPledge(pledge);
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putString("Pledge", getPledge());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(PLEDGE, BWPledges.NONE);
	}
}
