package moriyashiine.bewitchment.common.mixin;

import moriyashiine.bewitchment.common.misc.BWDataTrackers;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public class BWTrackers
{
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		Object obj = this;
		if (obj instanceof LivingEntity) {
			LivingEntity thisObj = (LivingEntity) obj;
			boolean player = thisObj instanceof PlayerEntity;
			if (player || BWDataTrackers.hasBlood(thisObj))
			{
				BWDataTrackers.setBlood(thisObj, tag.getInt("Blood"));
			}
			if (player)
			{
				BWDataTrackers.setMagic(thisObj, tag.getInt("Magic"));
			}
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		Object obj = this;
		if (obj instanceof LivingEntity) {
			LivingEntity thisObj = (LivingEntity) obj;
			boolean player = thisObj instanceof PlayerEntity;
			if (player || BWDataTrackers.hasBlood(thisObj))
			{
				tag.putInt("Blood", BWDataTrackers.getBlood(thisObj));
			}
			if (player)
			{
				tag.putInt("Magic", BWDataTrackers.getMagic(thisObj));
			}
		}
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initDataTracker(CallbackInfo callbackInfo) {
		Object obj = this;
		if (obj instanceof LivingEntity) {
			LivingEntity thisObj = (LivingEntity) obj;
			boolean player = thisObj instanceof PlayerEntity;
			if (player || BWDataTrackers.hasBlood(thisObj))
			{
				thisObj.getDataTracker().startTracking(BWDataTrackers.BLOOD, BWDataTrackers.MAX_BLOOD);
			}
			if (player)
			{
				thisObj.getDataTracker().startTracking(BWDataTrackers.MAGIC, 0);
			}
		}
	}
	
	@Mixin(ServerPlayerEntity.class)
	private static class Server {
		@Inject(method = "copyFrom", at = @At("TAIL"))
		public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
			Object obj = this;
			if (obj instanceof ServerPlayerEntity) {
				ServerPlayerEntity thisObj = (ServerPlayerEntity) obj;
				BWDataTrackers.setBlood(thisObj, BWUtil.isVampire(thisObj) ? 30 : BWDataTrackers.getBlood(oldPlayer));
				BWDataTrackers.setMagic(thisObj, BWDataTrackers.getMagic(oldPlayer));
			}
		}
	}
}