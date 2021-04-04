package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.entity.EntityGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin {
	@Shadow
	@Final
	public int typeIndex;
	
	@Shadow
	public abstract float getAttackDamage(int level, EntityGroup group);
	
	@Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
	private void getAttackDamage(int level, EntityGroup group, CallbackInfoReturnable<Float> callbackInfo) {
		if (typeIndex == 1 && group == BewitchmentAPI.DEMON) {
			callbackInfo.setReturnValue(getAttackDamage(level, EntityGroup.UNDEAD));
		}
	}
}
