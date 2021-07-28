package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements VillagerWerewolfAccessor {
	private NbtCompound storedWerewolf;
	private int despawnTimer = 2400;
	
	public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void setStoredWerewolf(NbtCompound storedWerewolf) {
		this.storedWerewolf = storedWerewolf;
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && storedWerewolf != null) {
			if (despawnTimer > 0) {
				despawnTimer--;
				if (despawnTimer == 0) {
					remove(RemovalReason.DISCARDED);
				}
			}
			if (age % 20 == 0 && world.isNight() && BewitchmentAPI.getMoonPhase(world) == 0 && world.isSkyVisible(getBlockPos())) {
				WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(world);
				if (entity != null) {
					PlayerLookup.tracking(this).forEach(player -> SpawnSmokeParticlesPacket.send(player, this));
					world.playSound(null, getX(), getY(), getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, getSoundCategory(), getSoundVolume(), getSoundPitch());
					entity.readNbt(storedWerewolf);
					entity.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (getHealth() / getMaxHealth()));
					entity.setFireTicks(getFireTicks());
					entity.clearStatusEffects();
					getStatusEffects().forEach(entity::addStatusEffect);
					((CurseAccessor) entity).getCurses().clear();
					((CurseAccessor) this).getCurses().forEach(((CurseAccessor) entity)::addCurse);
					if (despawnTimer >= 0) {
						despawnTimer = 2400;
					}
					entity.storedVillager = writeNbt(new NbtCompound());
					world.spawnEntity(entity);
					remove(RemovalReason.DISCARDED);
				}
			}
		}
	}
	
	@Inject(method = "interactMob", at = @At("HEAD"))
	private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
		despawnTimer = -1;
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("StoredWerewolf")) {
			storedWerewolf = nbt.getCompound("StoredWerewolf");
		}
		if (nbt.contains("DespawnTimer")) {
			despawnTimer = nbt.getInt("DespawnTimer");
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (storedWerewolf != null) {
			nbt.put("StoredWerewolf", storedWerewolf);
		}
		nbt.putInt("DespawnTimer", despawnTimer);
	}
}
