package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.entity.CaduceusFireballAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireballEntity.class)
public class FireballEntityMixin extends AbstractFireballEntity implements CaduceusFireballAccessor {
	private boolean fromCaduceus = false;
	
	public FireballEntityMixin(EntityType<? extends AbstractFireballEntity> entityType, World world) {
		super(entityType, world);
	}
	
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
