package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.CompoundTag;
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
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
	private boolean addStatusEffect(LivingEntity entity, StatusEffectInstance effect) {
		if (entity instanceof PolymorphAccessor && effect.getEffectType() == BWStatusEffects.POLYMORPH && getPolymorphUUID() != null) {
			PolymorphAccessor polymorphAccessor = (PolymorphAccessor) entity;
			polymorphAccessor.setPolymorphUUID(polymorphUUID);
			polymorphAccessor.setPolymorphName(polymorphName);
		}
		return entity.addStatusEffect(effect);
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
		if (getPolymorphUUID() != null) {
			tag.putUuid("PolymorphUUID", polymorphUUID);
			tag.putString("PolymorphName", polymorphName);
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
		if (tag.contains("PolymorphUUID")) {
			polymorphUUID = tag.getUuid("PolymorphUUID");
			polymorphName = tag.getString("PolymorphName");
		}
	}
}
