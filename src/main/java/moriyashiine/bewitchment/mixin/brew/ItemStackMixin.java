package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	private static final TranslatableText POTION = new TranslatableText("item." + Bewitchment.MODID + ".potion");
	private static final TranslatableText SPLASH_POTION = new TranslatableText("item." + Bewitchment.MODID + ".splash_potion");
	private static final TranslatableText LINGERING_POTION = new TranslatableText("item." + Bewitchment.MODID + ".lingering_potion");
	private static final TranslatableText TIPPED_ARROW = new TranslatableText("item." + Bewitchment.MODID + ".tipped_arrow");
	
	@Shadow
	@Nullable
	public abstract NbtCompound getTag();
	
	@Shadow
	public abstract Item getItem();
	
	@Inject(method = "getName", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void getName(CallbackInfoReturnable<Text> callbackInfo) {
		NbtCompound tag = getTag();
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
}
