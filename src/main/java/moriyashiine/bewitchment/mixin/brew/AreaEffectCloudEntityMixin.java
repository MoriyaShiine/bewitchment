package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin implements PolymorphAccessor {
	private UUID polymorphUUID;
	private String polymorphName;
	
	@Override
	public UUID getPolymorphUUID() {
		return polymorphUUID;
	}
	
	@Override
	public void setPolymorphUUID(UUID uuid) {
		this.polymorphUUID = uuid;
	}
	
	@Override
	public String getPolymorphName() {
		return polymorphName;
	}
	
	@Override
	public void setPolymorphName(String name) {
		this.polymorphName = name;
	}
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
	private boolean addStatusEffect(LivingEntity entity, StatusEffectInstance effect, Entity source) {
		if (entity instanceof PolymorphAccessor polymorphAccessor && effect.getEffectType() == BWStatusEffects.POLYMORPH && getPolymorphUUID() != null) {
			polymorphAccessor.setPolymorphUUID(polymorphUUID);
			polymorphAccessor.setPolymorphName(polymorphName);
		}
		return entity.addStatusEffect(effect);
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("PolymorphUUID")) {
			polymorphUUID = nbt.getUuid("PolymorphUUID");
			polymorphName = nbt.getString("PolymorphName");
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		if (getPolymorphUUID() != null) {
			nbt.putUuid("PolymorphUUID", polymorphUUID);
			nbt.putString("PolymorphName", polymorphName);
		}
	}
}
