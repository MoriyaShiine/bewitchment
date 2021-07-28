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
			NbtCompound nbt = stack.getNbt();
			if (nbt != null) {
				String name = nbt.getString("PolymorphName");
				if (!name.isEmpty()) {
					PolymorphAccessor polymorphAccessor = (PolymorphAccessor) user;
					polymorphAccessor.setPolymorphUUID(nbt.getUuid("PolymorphUUID"));
					polymorphAccessor.setPolymorphName(name);
				}
			}
		}
	}
}
