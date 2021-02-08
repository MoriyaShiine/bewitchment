package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applySplashPotion", at = @At("HEAD"))
    private void applySplashPotion(List<StatusEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo ci){
        if (getStack().getTag().contains("PolymorphName")) {
            String uuid = getStack().getTag().getString("PolymorphUUID");
            String name = getStack().getTag().getString("PolymorphName");
            Box box = this.getBoundingBox().expand(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, box);
            for (LivingEntity livingEntity : list) {
                if (livingEntity instanceof PolymorphAccessor){
                    PolymorphAccessor polymorphAccessor = (PolymorphAccessor) livingEntity;
                    polymorphAccessor.setPolymorphUUID(uuid);
                    polymorphAccessor.setPolymorphName(name);
                }
            }
        }
    }

    @Inject(method = "applyLingeringPotion",
            at = @At(value = "INVOKE", target = "net/minecraft/entity/AreaEffectCloudEntity.setPotion(Lnet/minecraft/potion/Potion;)V"),
    locals = LocalCapture.CAPTURE_FAILSOFT)
    private void applyLingeringPotion(ItemStack stack, Potion potion, CallbackInfo ci, AreaEffectCloudEntity cloud){
        if (stack.getTag().contains("PolymorphName")) {
            ((PolymorphAccessor) cloud).setPolymorphUUID(stack.getTag().getString("PolymorphUUID"));
            ((PolymorphAccessor) cloud).setPolymorphName(stack.getTag().getString("PolymorphName"));
        }
    }
}
