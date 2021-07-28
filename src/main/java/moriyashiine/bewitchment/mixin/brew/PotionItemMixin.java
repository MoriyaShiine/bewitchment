package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {
	@Inject(method = "finishUsing", at = @At("HEAD"))
	private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient && user instanceof PolymorphAccessor) {
			NbtCompound tag = stack.getTag();
			if (tag != null) {
				String name = tag.getString("PolymorphName");
				if (!name.isEmpty()) {
					PolymorphAccessor polymorphAccessor = (PolymorphAccessor) user;
					polymorphAccessor.setPolymorphUUID(tag.getUuid("PolymorphUUID"));
					polymorphAccessor.setPolymorphName(name);
				}
			}
		}
	}
}
