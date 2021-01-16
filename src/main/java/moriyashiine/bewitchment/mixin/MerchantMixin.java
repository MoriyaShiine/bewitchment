package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.CurseAccessor;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.util.math.Box;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin({MerchantEntity.class, DemonEntity.class})
public abstract class MerchantMixin extends LivingEntity {
	private static final TradeOfferList EMPTY = new TradeOfferList();
	
	protected MerchantMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "getOffers", at = @At("HEAD"), cancellable = true)
	private void getOffers(CallbackInfoReturnable<TradeOfferList> callbackInfo) {
		if (!world.isClient && !world.getEntitiesByClass(LivingEntity.class, new Box(getBlockPos()).expand(8), entity -> canSee(entity) && entity.isAlive() && CurseAccessor.of(entity).orElse(null).hasCurse(BWCurses.APATHY)).isEmpty()) {
			callbackInfo.setReturnValue(EMPTY);
		}
	}
}
