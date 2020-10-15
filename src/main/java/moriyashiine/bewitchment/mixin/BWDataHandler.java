package moriyashiine.bewitchment.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.accessor.BloodAccessor;
import moriyashiine.bewitchment.api.accessor.MagicAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class BWDataHandler extends Entity implements MagicAccessor, BloodAccessor {
	private static final TrackedData<Integer> MAGIC = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> BLOOD = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	public BWDataHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public int getMagic() {
		return dataTracker.get(MAGIC);
	}
	
	@Override
	public void setMagic(int magic) {
		dataTracker.set(MAGIC, magic);
	}
	
	@Override
	public int getBlood() {
		return dataTracker.get(BLOOD);
	}
	
	@Override
	public void setBlood(int blood) {
		dataTracker.set(BLOOD, blood);
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		MagicAccessor.of(this).ifPresent(magicAccessor -> magicAccessor.setMagic(tag.getInt("Magic")));
		BloodAccessor.of(this).ifPresent(bloodAccessor -> bloodAccessor.setBlood(tag.getInt("Blood")));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		MagicAccessor.of(this).ifPresent(magicAccessor -> tag.putInt("Magic", magicAccessor.getMagic()));
		BloodAccessor.of(this).ifPresent(bloodAccessor -> tag.putInt("Blood", bloodAccessor.getBlood()));
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		MagicAccessor.of(this).ifPresent(magicAccessor -> dataTracker.startTracking(MAGIC, 0));
		BloodAccessor.of(this).ifPresent(bloodAccessor -> dataTracker.startTracking(BLOOD, MAX_BLOOD));
	}
	
	@Mixin(ServerPlayerEntity.class)
	private static abstract class Server extends PlayerEntity {
		public Server(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
			super(world, blockPos, f, gameProfile);
		}
		
		@Inject(method = "copyFrom", at = @At("TAIL"))
		private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
			MagicAccessor.of(this).ifPresent(magicAccessor -> MagicAccessor.of(oldPlayer).ifPresent(oldMagicAccessor -> magicAccessor.setMagic(oldMagicAccessor.getMagic())));
			BloodAccessor.of(this).ifPresent(bloodAccessor -> BloodAccessor.of(oldPlayer).ifPresent(oldBloodAccessor -> bloodAccessor.setBlood(BewitchmentAPI.isVampire(this) ? 30 : oldBloodAccessor.getBlood())));
		}
	}
}
