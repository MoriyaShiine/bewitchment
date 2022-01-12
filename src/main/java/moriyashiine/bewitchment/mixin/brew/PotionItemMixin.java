/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.registry.BWComponents;
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
		if (!world.isClient) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(user).ifPresent(polymorphComponent -> {
				NbtCompound compound = stack.getNbt();
				if (compound != null) {
					String name = compound.getString("PolymorphName");
					if (!name.isEmpty()) {
						polymorphComponent.setUuid(compound.getUuid("PolymorphUUID"));
						polymorphComponent.setName(name);
					}
				}
			});
		}
	}
}
