package moriyashiine.bewitchment.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("ConstantConditions")
@Mixin(ItemStackArgument.class)
public class ItemStackArgumentMixin {
	@Inject(method = "createStack", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void createStack(int amount, boolean checkOverstack, CallbackInfoReturnable<ItemStack> callbackInfo, ItemStack stack) {
		if (stack.getItem() instanceof TaglockItem && stack.hasNbt() && stack.getNbt().contains("OwnerName") && !stack.getNbt().contains("OwnerUUID")) {
			SkullBlockEntity.loadProperties(new GameProfile(null, stack.getNbt().getString("OwnerName")), gameProfile -> {
				stack.getNbt().putUuid("OwnerUUID", gameProfile.getId());
				stack.getNbt().putBoolean("FromPlayer", true);
			});
		}
	}
}
