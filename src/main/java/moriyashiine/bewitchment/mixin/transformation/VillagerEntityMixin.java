package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.interfaces.VillagerWerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements VillagerWerewolfAccessor {
	private CompoundTag storedWerewolf;
	
	public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void setStoredWerewolf(CompoundTag storedWerewolf) {
		this.storedWerewolf = storedWerewolf;
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && storedWerewolf != null && age % 20 == 0 && world.isNight() && BewitchmentAPI.getMoonPhase(world) == 0 && world.isSkyVisible(getBlockPos())) {
			WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(world);
			if (entity != null) {
				PlayerLookup.tracking(this).forEach(player -> SpawnSmokeParticlesPacket.send(player, this));
				world.playSound(null, getX(), getY(), getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, getSoundCategory(), getSoundVolume(), getSoundPitch());
				entity.fromTag(storedWerewolf);
				entity.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
				entity.setHealth(entity.getMaxHealth() * (getHealth() / getMaxHealth()));
				entity.setFireTicks(getFireTicks());
				entity.clearStatusEffects();
				getStatusEffects().forEach(entity::addStatusEffect);
				((CurseAccessor) entity).getCurses().clear();
				((CurseAccessor) this).getCurses().forEach(((CurseAccessor) entity)::addCurse);
				((ContractAccessor) entity).getContracts().clear();
				((ContractAccessor) this).getContracts().forEach(((ContractAccessor) entity)::addContract);
				entity.storedVillager = toTag(new CompoundTag());
				world.spawnEntity(entity);
				remove();
			}
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (tag.contains("StoredWerewolf")) {
			storedWerewolf = tag.getCompound("StoredWerewolf");
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (storedWerewolf != null) {
			tag.put("StoredWerewolf", storedWerewolf);
		}
	}
}
