package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity {
	@Shadow
	public abstract void setTarget(@Nullable LivingEntity target);
	
	protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "isPlayerStaring", at = @At("RETURN"), cancellable = true)
	private void isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && !player.isCreative() && getTarget() == null && ((CurseAccessor) player).hasCurse(BWCurses.OUTRAGE) && distanceTo(player) <= getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE) / 4 && canSee(player)) {
			setTarget(player);
			callbackInfo.setReturnValue(true);
		}
	}
}
