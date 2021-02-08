package moriyashiine.bewitchment.mixin;

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

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin implements PolymorphAccessor {
    private String polymorphName;
    private String polymorphUUID;

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    private void writePolymorphData(CompoundTag tag, CallbackInfo ci){
        if (polymorphName != null) {
            tag.putString("PolymorphName", polymorphName);
            tag.putString("PolymorphUUID", polymorphUUID);
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    private void readPolymorphData(CompoundTag tag, CallbackInfo ci){
        if (tag.contains("PolymorphName")) {
            polymorphName = tag.getString("PolymorphName");
            polymorphUUID = tag.getString("PolymorphUUID");
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
    private boolean applyPolymorphData(LivingEntity entity, StatusEffectInstance effect){
        if (entity instanceof PolymorphAccessor && effect.getEffectType() == BWStatusEffects.POLYMORPH && polymorphUUID != null){
            PolymorphAccessor polymorphAccessor = (PolymorphAccessor) entity;
            polymorphAccessor.setPolymorphName(polymorphName);
            polymorphAccessor.setPolymorphUUID(polymorphUUID);
        }
        return entity.addStatusEffect(effect);
    }

    @Override
    public String getPolymorphUUID() {
        return polymorphUUID;
    }

    @Override
    public void setPolymorphUUID(String uuid) {
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
}
