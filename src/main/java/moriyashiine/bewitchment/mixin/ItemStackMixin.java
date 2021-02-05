package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	private static final TranslatableText POTION = new TranslatableText("item." + Bewitchment.MODID + ".potion");
	private static final TranslatableText SPLASH_POTION = new TranslatableText("item." + Bewitchment.MODID + ".splash_potion");
	private static final TranslatableText LINGERING_POTION = new TranslatableText("item." + Bewitchment.MODID + ".lingering_potion");
	private static final TranslatableText TIPPED_ARROW = new TranslatableText("item." + Bewitchment.MODID + ".tipped_arrow");
	
	@Shadow
	@Nullable
	public abstract CompoundTag getTag();
	
	@Shadow
	public abstract Item getItem();
	
	@Shadow
	public abstract void setDamage(int damage);
	
	@Shadow
	public abstract int getDamage();
	
	@Shadow
	public abstract int getMaxDamage();
	
	@Inject(method = "getName", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void getName(CallbackInfoReturnable<Text> callbackInfo) {
		CompoundTag tag = getTag();
		if (tag != null && tag.getBoolean("BewitchmentBrew")) {
			if (getItem() == Items.POTION) {
				callbackInfo.setReturnValue(POTION);
			}
			else if (getItem() == Items.SPLASH_POTION) {
				callbackInfo.setReturnValue(SPLASH_POTION);
			}
			else if (getItem() == Items.LINGERING_POTION) {
				callbackInfo.setReturnValue(LINGERING_POTION);
			}
			else if (getItem() == Items.TIPPED_ARROW) {
				callbackInfo.setReturnValue(TIPPED_ARROW);
			}
		}
	}
	
	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"), cancellable = true)
	private <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo callbackInfo) {
		if (getDamage() == getMaxDamage()) {
			ItemStack poppet = BewitchmentAPI.getPoppet(entity.world, BWObjects.MENDING_POPPET, entity, null);
			if (!poppet.isEmpty()) {
				if (poppet.damage(entity instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) entity) == EntityType.WOLF && entity.getRandom().nextBoolean() ? 0 : 1, entity.getRandom(), null) && poppet.getDamage() == poppet.getMaxDamage()) {
					poppet.decrement(1);
				}
				setDamage(0);
				callbackInfo.cancel();
			}
		}
	}
}
