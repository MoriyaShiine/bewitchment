package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.interfaces.CaduceusFireballAccessor;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundTag;
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
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		setFromCaduceus(tag.getBoolean("FromCaduceus"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putBoolean("FromCaduceus", getFromCaduceus());
	}
}
