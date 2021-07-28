package moriyashiine.bewitchment.mixin.brew;

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
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
	public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "applySplashPotion", at = @At("HEAD"))
	private void applySplashPotion(List<StatusEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo callbackInfo) {
		if (getItem().hasNbt() && getItem().getNbt().contains("PolymorphUUID")) {
			UUID uuid = getItem().getNbt().getUuid("PolymorphUUID");
			String name = getItem().getNbt().getString("PolymorphName");
			Box box = getBoundingBox().expand(4, 2, 4);
			List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, box);
			for (LivingEntity livingEntity : list) {
				if (livingEntity instanceof PolymorphAccessor polymorphAccessor) {
					polymorphAccessor.setPolymorphUUID(uuid);
					polymorphAccessor.setPolymorphName(name);
				}
			}
		}
	}
	
	@Inject(method = "applyLingeringPotion", at = @At(value = "INVOKE", target = "net/minecraft/entity/AreaEffectCloudEntity.setPotion(Lnet/minecraft/potion/Potion;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void applyLingeringPotion(ItemStack stack, Potion potion, CallbackInfo callbackInfo, AreaEffectCloudEntity cloud) {
		if (stack.hasNbt() && stack.getNbt().contains("PolymorphUUID")) {
			((PolymorphAccessor) cloud).setPolymorphUUID(stack.getNbt().getUuid("PolymorphUUID"));
			((PolymorphAccessor) cloud).setPolymorphName(stack.getNbt().getString("PolymorphName"));
		}
	}
}
