package moriyashiine.bewitchment.common.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.misc.interfaces.BloodAccessor;
import moriyashiine.bewitchment.common.misc.interfaces.MagicAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class BWDataHandler extends Entity implements MagicAccessor, BloodAccessor {
	private int magic = 0, blood = 100;
	
	public BWDataHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public int getMagic() {
		return magic;
	}
	
	@Override
	public void setMagic(int magic) {
		this.magic = magic;
	}
	
	@Override
	public int getBlood() {
		return blood;
	}
	
	@Override
	public void setBlood(int blood) {
		this.blood = blood;
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		Object obj = this;
		if (obj instanceof PlayerEntity) {
			setMagic(tag.getInt("Magic"));
		}
		if (hasBlood(this)) {
			setBlood(tag.getInt("Blood"));
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		Object obj = this;
		if (obj instanceof PlayerEntity) {
			tag.putInt("Magic", getMagic());
		}
		if (hasBlood(this)) {
			tag.putInt("Blood", getBlood());
		}
	}
	
	@Mixin(ServerPlayerEntity.class)
	private static abstract class Server extends PlayerEntity implements MagicAccessor, BloodAccessor {
		public Server(World world, BlockPos blockPos, GameProfile gameProfile) {
			super(world, blockPos, gameProfile);
		}
		
		@Inject(method = "copyFrom", at = @At("TAIL"))
		private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
			setMagic(((MagicAccessor) oldPlayer).getMagic());
			setBlood(BWUtil.isVampire(this) ? 30 : ((BloodAccessor) oldPlayer).getBlood());
		}
	}
}