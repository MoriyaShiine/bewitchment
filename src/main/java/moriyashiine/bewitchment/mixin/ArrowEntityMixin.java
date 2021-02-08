package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ArrowEntity.class)
public class ArrowEntityMixin implements PolymorphAccessor {
    private String polymorphUUID = "";
    private String polymorphName = "";

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        if (polymorphUUID != null) {
            tag.putString("PolymorphUUID", polymorphUUID);
            tag.putString("PolymorphName", polymorphName);
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("PolymorphUUID")) {
            polymorphUUID = tag.getString("PolymorphUUID");
            polymorphName = tag.getString("PolymorphName");
        }
    }

    @Inject(method = "initFromStack", at = @At("TAIL"))
    private void initFromStack(ItemStack stack, CallbackInfo ci) {
        if (stack.getTag().contains("PolymorphName")) {
            setPolymorphUUID(stack.getTag().getString("PolymorphUUID"));
            setPolymorphName(stack.getTag().getString("PolymorphName"));
        }
    }

    @Inject(method = "onHit", at = @At("HEAD"))
    private void onHit(LivingEntity target, CallbackInfo ci) {
        if (!getPolymorphUUID().isEmpty() && target instanceof PolymorphAccessor) {
            PolymorphAccessor polymorphAccessor = (PolymorphAccessor) target;
            polymorphAccessor.setPolymorphUUID(getPolymorphUUID());
            polymorphAccessor.setPolymorphName(getPolymorphName());
        }
    }

    @Inject(method = "asItemStack",
            at = @At(value = "RETURN", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void asItemStack(CallbackInfoReturnable<ItemStack> cir, ItemStack stack) {
        if (!getPolymorphUUID().isEmpty()){
            stack.getOrCreateTag().putString("PolymorphUUID", getPolymorphUUID());
            stack.getOrCreateTag().putString("PolymorphName", getPolymorphName());
        }
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
