package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.interfaces.CaduceusFireballAccessor;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireballEntity.class)
public class FireballEntityMixin implements CaduceusFireballAccessor {
	private boolean fromCaduceus = false;
	
	@Override
	public boolean getFromCaduceus() {
		return fromCaduceus;
	}
	
	@Override
	public void setFromCaduceus(boolean fromCaduceus) {
		this.fromCaduceus = fromCaduceus;
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		setFromCaduceus(nbt.getBoolean("FromCaduceus"));
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putBoolean("FromCaduceus", getFromCaduceus());
	}
}
