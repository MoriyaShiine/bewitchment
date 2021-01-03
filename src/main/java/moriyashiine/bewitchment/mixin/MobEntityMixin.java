package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.MasterAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements MasterAccessor {
	private UUID masterUUID = null;
	private boolean affectByWar = false;
	
	@Override
	public UUID getMasterUUID() {
		return masterUUID;
	}
	
	@Override
	public void setMasterUUID(UUID masterUUID) {
		this.masterUUID = masterUUID;
	}
	
	@Shadow
	@Nullable
	public abstract LivingEntity getTarget();
	
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"))
	private LivingEntity setTarget(LivingEntity target) {
		if (target != null) {
			ContractAccessor contractAccessor = ContractAccessor.of(target).orElse(null);
			if (contractAccessor != null && contractAccessor.hasContract(BWContracts.WAR)) {
				Entity nearest = null;
				for (Entity entity : world.getEntitiesByType(getType(), new Box(getBlockPos()).expand(16), entity -> entity != this)) {
					if (nearest == null || entity.distanceTo(this) < nearest.distanceTo(this)) {
						nearest = entity;
					}
				}
				if (nearest != null) {
					affectByWar = true;
					return (LivingEntity) nearest;
				}
				else if (affectByWar && contractAccessor.hasNegativeEffects()) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 1));
				}
			}
		}
		return target;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getMasterUUID() != null) {
			Entity master = ((ServerWorld) world).getEntity(getMasterUUID());
			if (master instanceof HostileEntity && !((HostileEntity) master).isDead() && ((HostileEntity) master).getTarget() != null) {
				setTarget(((HostileEntity) master).getTarget());
			}
			else {
				PlayerStream.watching(this).forEach(playerEntity -> SpawnSmokeParticlesPacket.send(playerEntity, this));
				remove();
			}
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (tag.contains("MasterUUID")) {
			setMasterUUID(tag.getUuid("MasterUUID"));
		}
		affectByWar = tag.getBoolean("AffectedByWar");
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (getMasterUUID() != null) {
			tag.putUuid("MasterUUID", getMasterUUID());
		}
		tag.putBoolean("AffectedByWar", affectByWar);
	}
}
