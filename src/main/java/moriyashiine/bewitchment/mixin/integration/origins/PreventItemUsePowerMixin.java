/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.integration.origins;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "io.github.apace100.apoli.power.PreventItemUsePower", remap = false)
public class PreventItemUsePowerMixin extends Power {
	@Unique
	private static final Identifier VEGETARIAN = new Identifier("origins", "vegetarian");

	public PreventItemUsePowerMixin(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}

	@Inject(method = "doesPrevent", at = @At("RETURN"), cancellable = true)
	private void doesPrevent(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && BewitchmentAPI.isWerewolf(entity, true) && getType().getIdentifier().equals(VEGETARIAN)) {
			callbackInfo.setReturnValue(false);
		}
	}
}
