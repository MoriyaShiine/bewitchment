package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
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
			ContractAccessor.of(attackingPlayer).ifPresent(contractAccessor -> {
				if (contractAccessor.hasContract(BWContracts.ENVY)) {
					VillagerEntity villager = (VillagerEntity) (Object) this;
					for (TradeOffer offer : villager.getOffers()) {
						if (!offer.isDisabled()) {
							world.spawnEntity(new ItemEntity(world, getX() + 0.5, getY() + 0.5, getZ() + 0.5, offer.getSellItem()));
						}
					}
					if (contractAccessor.hasNegativeEffects() && random.nextBoolean()) {
						for (int i = 0; i < 3; i++) {
							VindicatorEntity vindicator = EntityType.VINDICATOR.create(world);
							if (vindicator != null) {
								vindicator.refreshPositionAndAngles(getBlockPos(), 0, random.nextInt(360));
								vindicator.initialize((ServerWorldAccess) world, world.getLocalDifficulty(getBlockPos()), SpawnReason.EVENT, null, null);
								vindicator.setTarget(attackingPlayer);
								vindicator.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
								vindicator.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1));
								vindicator.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 1));
								world.spawnEntity(vindicator);
							}
						}
					}
				}
			});
		}
	}
}
