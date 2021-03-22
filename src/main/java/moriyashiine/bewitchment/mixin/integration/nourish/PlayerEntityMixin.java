package moriyashiine.bewitchment.mixin.integration.nourish;

import moriyashiine.bewitchment.common.integration.nourish.BWNourishRuntimeJank;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		BWNourishRuntimeJank.doVampireLogic((PlayerEntity) (Object) this);
	}
}
