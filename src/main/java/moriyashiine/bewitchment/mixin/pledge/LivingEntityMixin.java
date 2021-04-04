package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.interfaces.PledgeAccessor;
import moriyashiine.bewitchment.common.registry.BWPledges;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<EntityGroup> callbackInfo) {
		if (this instanceof PledgeAccessor && !((PledgeAccessor) this).getPledge().equals(BWPledges.NONE) && !BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(BewitchmentAPI.DEMON);
		}
	}
}
