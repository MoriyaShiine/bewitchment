package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applySplashPotion", at = @At("HEAD"))
    private void addPolymorphSplash(List<StatusEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo ci){
        String name = getStack().getTag().getString("PolymorphName");
        if (!name.isEmpty()) {
            String uuid = getStack().getTag().getString("PolymorphUUID");
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
}
