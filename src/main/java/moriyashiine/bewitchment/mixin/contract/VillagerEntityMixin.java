package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {
	public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "onDeath", at = @At("HEAD"))
	private void onDeath(CallbackInfo callbackInfo) {
		if (!world.isClient && attackingPlayer != null) {
			if (((ContractAccessor) attackingPlayer).hasContract(BWContracts.ENVY) && !getOffers().isEmpty()) {
				for (TradeOffer offer : getOffers()) {
					if (!offer.isDisabled()) {
						ItemScatterer.spawn(world, getX() + 0.5, getY() + 0.5, getZ() + 0.5, offer.getSellItem());
					}
				}
			}
		}
	}
}
