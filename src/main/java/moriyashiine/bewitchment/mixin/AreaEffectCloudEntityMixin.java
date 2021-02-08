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
    private String polymorphUUID;
    private String polymorphName;

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci){
        if (polymorphUUID != null) {
            tag.putString("PolymorphUUID", polymorphUUID);
            tag.putString("PolymorphName", polymorphName);
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci){
        if (tag.contains("PolymorphUUID")) {
            polymorphUUID = tag.getString("PolymorphUUID");
            polymorphName = tag.getString("PolymorphName");
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
    private boolean tick(LivingEntity entity, StatusEffectInstance effect){
        //todo replace redirect with local inject
        if (entity instanceof PolymorphAccessor && effect.getEffectType() == BWStatusEffects.POLYMORPH && polymorphUUID != null){
            PolymorphAccessor polymorphAccessor = (PolymorphAccessor) entity;
            polymorphAccessor.setPolymorphUUID(polymorphUUID);
            polymorphAccessor.setPolymorphName(polymorphName);
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
