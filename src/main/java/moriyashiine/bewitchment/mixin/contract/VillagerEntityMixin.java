package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.ServerWorldAccess;
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
			ContractAccessor contractAccessor = (ContractAccessor) attackingPlayer;
			if (contractAccessor.hasContract(BWContracts.ENVY)) {
				if (!getOffers().isEmpty()) {
					for (TradeOffer offer : getOffers()) {
						if (!offer.isDisabled()) {
							ItemScatterer.spawn(world, getX() + 0.5, getY() + 0.5, getZ() + 0.5, offer.getSellItem());
						}
					}
					if (contractAccessor.hasNegativeEffects() && random.nextBoolean()) {
						for (int i = 0; i < 3; i++) {
							VindicatorEntity vindicator = EntityType.VINDICATOR.create(world);
							if (vindicator != null) {
								vindicator.updatePositionAndAngles(getBlockPos().getX() + 0.5, getBlockPos().getY(), getBlockPos().getZ() + 0.5, world.random.nextFloat() * 360, 0);
								vindicator.initialize((ServerWorldAccess) world, world.getLocalDifficulty(getBlockPos()), SpawnReason.EVENT, null, null);
								vindicator.setTarget(attackingPlayer);
								vindicator.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE));
								vindicator.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1));
								vindicator.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 1));
								world.spawnEntity(vindicator);
							}
						}
					}
				}
			}
		}
	}
}
