package moriyashiine.bewitchment.mixin.integration.origins;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "io.github.apace100.origins.power.PreventItemUsePower", remap = false)
public class PreventItemUsePowerMixin extends Power {
	private static final Identifier VEGETARIAN = new Identifier("origins", "vegetarian");
	
	public PreventItemUsePowerMixin(PowerType<?> type, PlayerEntity player) {
		super(type, player);
	}
	
	@Inject(method = "doesPrevent", at = @At("RETURN"), cancellable = true)
	private void doesPrevent(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && BewitchmentAPI.isWerewolf(player, true) && getType().getIdentifier().equals(VEGETARIAN)) {
			callbackInfo.setReturnValue(false);
		}
	}
}
