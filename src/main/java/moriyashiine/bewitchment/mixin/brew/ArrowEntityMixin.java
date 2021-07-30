package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.entity.component.PolymorphComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends Entity {
	public ArrowEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "asItemStack", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void asItemStack(CallbackInfoReturnable<ItemStack> callbackInfo, ItemStack stack) {
		PolymorphComponent.maybeGet(this).ifPresent(polymorphComponent -> {
			if (polymorphComponent.getUuid() != null) {
				stack.getOrCreateNbt().putUuid("PolymorphUUID", polymorphComponent.getUuid());
				stack.getOrCreateNbt().putString("PolymorphName", polymorphComponent.getName());
			}
		});
	}
	
	@Inject(method = "onHit", at = @At("HEAD"))
	private void onHit(LivingEntity target, CallbackInfo callbackInfo) {
		PolymorphComponent.maybeGet(this).ifPresent(thisPolymorphComponent -> {
			if (thisPolymorphComponent.getUuid() != null) {
				PolymorphComponent.maybeGet(target).ifPresent(targetPolymorphComponent -> {
					targetPolymorphComponent.setUuid(thisPolymorphComponent.getUuid());
					targetPolymorphComponent.setName(thisPolymorphComponent.getName());
				});
			}
		});
	}
	
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "initFromStack", at = @At("TAIL"))
	private void initFromStack(ItemStack stack, CallbackInfo callbackInfo) {
		PolymorphComponent.maybeGet(this).ifPresent(polymorphComponent -> {
			if (stack.hasNbt() && stack.getNbt().contains("PolymorphUUID")) {
				polymorphComponent.setUuid(stack.getNbt().getUuid("PolymorphUUID"));
				polymorphComponent.setName(stack.getNbt().getString("PolymorphName"));
			}
		});
	}
}
