package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.PolymorphAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {
	@Inject(method = "finishUsing", at = @At("HEAD"))
	private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> callbackInfo) {
		try {
			if (user instanceof PlayerEntity) {
				CompoundTag tag = stack.getTag();
				if (tag != null) {
					String name = tag.getString("PolymorphName");
					if (!name.isEmpty()) {
						PolymorphAccessor.of((PlayerEntity) user).ifPresent(polymorphAccessor -> {
							polymorphAccessor.setPolymorphUUID(Optional.of(UUID.fromString(tag.getString("PolymorphUUID"))));
							polymorphAccessor.setPolymorphName(name);
						});
					}
				}
			}
		} catch (Exception ignored) {
		}
	}
}
