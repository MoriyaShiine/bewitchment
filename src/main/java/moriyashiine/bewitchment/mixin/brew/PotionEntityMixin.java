package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.entity.component.PolymorphComponent;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
	public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(method = "applyLingeringPotion", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
	private Iterator<StatusEffectInstance> modifyIterator(Iterator<StatusEffectInstance> iterator, ItemStack stack) {
		if (stack.getNbt().getBoolean("BewitchmentBrew")) {
			List<StatusEffectInstance> statusEffects = new ArrayList<>();
			iterator.forEachRemaining(effect -> statusEffects.add(new StatusEffectInstance(effect.getEffectType(), effect.getDuration() / 4, effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon())));
			return statusEffects.iterator();
		}
		return iterator;
	}
	
	@Inject(method = "applySplashPotion", at = @At("HEAD"))
	private void applySplashPotion(List<StatusEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo callbackInfo) {
		if (getItem().hasNbt() && getItem().getNbt().contains("PolymorphUUID")) {
			UUID uuid = getItem().getNbt().getUuid("PolymorphUUID");
			String name = getItem().getNbt().getString("PolymorphName");
			for (LivingEntity livingEntity : world.getNonSpectatingEntities(LivingEntity.class, getBoundingBox().expand(4, 2, 4))) {
				PolymorphComponent.maybeGet(livingEntity).ifPresent(polymorphComponent -> {
					polymorphComponent.setUuid(uuid);
					polymorphComponent.setName(name);
				});
			}
		}
	}
	
	@Inject(method = "applyLingeringPotion", at = @At(value = "INVOKE", target = "net/minecraft/entity/AreaEffectCloudEntity.setPotion(Lnet/minecraft/potion/Potion;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void applyLingeringPotion(ItemStack stack, Potion potion, CallbackInfo callbackInfo, AreaEffectCloudEntity cloud) {
		if (stack.hasNbt() && stack.getNbt().contains("PolymorphUUID")) {
			PolymorphComponent.maybeGet(cloud).ifPresent(polymorphComponent -> {
				polymorphComponent.setUuid(stack.getNbt().getUuid("PolymorphUUID"));
				polymorphComponent.setName(stack.getNbt().getString("PolymorphName"));
			});
		}
	}
}
