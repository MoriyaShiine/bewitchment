package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.*;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWContracts;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements MasterAccessor, InsanityTargetAccessor {
	private static final TrackedData<Optional<UUID>> INSANITY_TARGET_UUID = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	
	private UUID masterUUID = null;
	private boolean affectByWar = false;
	private boolean spawnedByArachnophobia = false;
	
	@Override
	public UUID getMasterUUID() {
		return masterUUID;
	}
	
	@Override
	public void setMasterUUID(UUID masterUUID) {
		this.masterUUID = masterUUID;
	}
	
	@Override
	public Optional<UUID> getInsanityTargetUUID() {
		return dataTracker.get(INSANITY_TARGET_UUID);
	}
	
	@Override
	public void setInsanityTargetUUID(Optional<UUID> insanityTargetUUID) {
		dataTracker.set(INSANITY_TARGET_UUID, insanityTargetUUID);
	}
	
	@Shadow
	@Nullable
	public abstract LivingEntity getTarget();
	
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"))
	private LivingEntity setTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			UUID insanityTargetUUID = getInsanityTargetUUID().orElse(null);
			if (insanityTargetUUID != null && !target.getUuid().equals(insanityTargetUUID)) {
				return null;
			}
			if (this instanceof Pledgeable) {
				if (target instanceof MasterAccessor && getUuid().equals(((MasterAccessor) target).getMasterUUID())) {
					return null;
				}
				Pledgeable pledgeable = (Pledgeable) this;
				if (BewitchmentAPI.isPledged(world, pledgeable.getPledgeUUID(), target.getUuid())) {
					BewitchmentAPI.unpledge(world, pledgeable.getPledgeUUID(), target.getUuid());
				}
				pledgeable.summonMinions((MobEntity) (Object) this);
			}
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
	
	@Inject(method = "dropLoot", at = @At("HEAD"))
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo callbackInfo) {
		if (!world.isClient && (Object) this instanceof SpiderEntity && !spawnedByArachnophobia) {
			Entity attacker = source.getAttacker();
			CurseAccessor.of(attacker).ifPresent(curseAccessor -> {
				if (curseAccessor.hasCurse(BWCurses.ARACHNOPHOBIA)) {
					for (int i = 0; i < random.nextInt(3) + 3; i++) {
						SpiderEntity spider;
						if (random.nextFloat() < 1 / 8192f) {
							spider = EntityType.SPIDER.create(world);
						}
						else {
							spider = EntityType.CAVE_SPIDER.create(world);
							((MobEntityMixin) (Object) spider).spawnedByArachnophobia = true;
						}
						if (spider != null) {
							spider.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
							spider.initialize((ServerWorldAccess) world, world.getLocalDifficulty(getBlockPos()), SpawnReason.EVENT, null, null);
							spider.setTarget((LivingEntity) attacker);
							world.spawnEntity(spider);
						}
					}
				}
			});
		}
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (getMasterUUID() != null) {
				Entity master = ((ServerWorld) world).getEntity(getMasterUUID());
				if (master instanceof HostileEntity && !((HostileEntity) master).isDead() && ((HostileEntity) master).getTarget() != null) {
					setTarget(((HostileEntity) master).getTarget());
				}
				else {
					PlayerLookup.tracking(this).forEach(playerEntity -> SpawnSmokeParticlesPacket.send(playerEntity, this));
					remove();
				}
			}
			getInsanityTargetUUID().ifPresent(uuid -> {
				LivingEntity entity = (LivingEntity) ((ServerWorld) world).getEntity(uuid);
				if (getTarget() == null || !getTarget().getUuid().equals(uuid)) {
					setTarget(entity);
				}
				if (age % 20 == 0) {
					boolean remove = false;
					if (random.nextFloat() < 1 / 100f) {
						remove = true;
					}
					else {
						CurseAccessor curseAccessor = CurseAccessor.of(entity).orElse(null);
						if (curseAccessor != null && !curseAccessor.hasCurse(BWCurses.INSANITY)) {
							remove = true;
						}
					}
					if (remove) {
						remove();
					}
				}
			});
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (tag.contains("MasterUUID")) {
			setMasterUUID(tag.getUuid("MasterUUID"));
		}
		affectByWar = tag.getBoolean("AffectedByWar");
		if ((Object) this instanceof CaveSpiderEntity) {
			spawnedByArachnophobia = tag.getBoolean("SpawnedByArachnophobia");
		}
		setInsanityTargetUUID(tag.getString("InsanityTargetUUID").isEmpty() ? Optional.empty() : Optional.of(UUID.fromString(tag.getString("InsanityTargetUUID"))));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (getMasterUUID() != null) {
			tag.putUuid("MasterUUID", getMasterUUID());
		}
		tag.putBoolean("AffectedByWar", affectByWar);
		if ((Object) this instanceof CaveSpiderEntity) {
			tag.putBoolean("SpawnedByArachnophobia", spawnedByArachnophobia);
		}
		tag.putString("InsanityTargetUUID", getInsanityTargetUUID().isPresent() ? getInsanityTargetUUID().get().toString() : "");
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(INSANITY_TARGET_UUID, Optional.empty());
	}
}
