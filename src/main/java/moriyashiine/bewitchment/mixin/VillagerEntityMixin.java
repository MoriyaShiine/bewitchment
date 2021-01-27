package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.DespawnAccessor;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements DespawnAccessor {
	private int despawnTimer = 0;
	
	@Shadow
	protected abstract void sayNo();
	
	@Shadow
	protected abstract void initDataTracker();
	
	public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void setDespawnTimer(int despawnTimer) {
		this.despawnTimer = despawnTimer;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void getOffers(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (despawnTimer > 0 && --despawnTimer == 0) {
				remove();
			}
		}
	}
	
	@Inject(method = "interactMob", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/passive/VillagerEntity;getOffers()Lnet/minecraft/village/TradeOfferList;"), cancellable = true)
	private void getOffers(PlayerEntity player, Hand hand, CallbackInfoReturnable<TradeOfferList> callbackInfo) {
		if (!world.isClient && DemonEntity.rejectTrades(this)) {
			sayNo();
			callbackInfo.setReturnValue(DemonEntity.EMPTY);
		}
	}
	
	@Inject(method = "onDeath", at = @At("HEAD"))
	private void onDeath(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (attackingPlayer instanceof ContractAccessor) {
				ContractAccessor contractAccessor = (ContractAccessor) attackingPlayer;
				if (contractAccessor.hasContract(BWContracts.ENVY)) {
					VillagerEntity villager = (VillagerEntity) (Object) this;
					if (!villager.getOffers().isEmpty()) {
						for (TradeOffer offer : villager.getOffers()) {
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
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		despawnTimer = tag.getInt("DespawnTimer");
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putInt("DespawnTimer", despawnTimer);
	}
}
