package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.InsanityTargetAccessor;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
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
public abstract class MobEntityMixin extends LivingEntity implements InsanityTargetAccessor {
	private static final TrackedData<Optional<UUID>> INSANITY_TARGET_UUID = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	
	private boolean spawnedByArachnophobia = false;
	
	@Shadow
	@Nullable
	public abstract LivingEntity getTarget();
	
	@Shadow
	public abstract void setTarget(@Nullable LivingEntity target);
	
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public Optional<UUID> getInsanityTargetUUID() {
		return dataTracker.get(INSANITY_TARGET_UUID);
	}
	
	@Override
	public void setInsanityTargetUUID(Optional<UUID> insanityTargetUUID) {
		dataTracker.set(INSANITY_TARGET_UUID, insanityTargetUUID);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			getInsanityTargetUUID().ifPresent(uuid -> {
				LivingEntity entity = (LivingEntity) ((ServerWorld) world).getEntity(uuid);
				if (getTarget() == null || !getTarget().getUuid().equals(uuid)) {
					setTarget(entity);
				}
				if (age % 20 == 0 && (random.nextFloat() < 1 / 100f || (entity instanceof CurseAccessor && !((CurseAccessor) entity).hasCurse(BWCurses.INSANITY)))) {
					remove(RemovalReason.DISCARDED);
				}
			});
		}
	}
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"))
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			UUID insanityTargetUUID = getInsanityTargetUUID().orElse(null);
			if (insanityTargetUUID != null && !target.getUuid().equals(insanityTargetUUID)) {
				return null;
			}
		}
		return target;
	}
	
	@Inject(method = "dropLoot", at = @At("HEAD"))
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo callbackInfo) {
		if (!world.isClient && (Object) this instanceof SpiderEntity && !spawnedByArachnophobia) {
			Entity attacker = source.getAttacker();
			if (attacker instanceof CurseAccessor && ((CurseAccessor) attacker).hasCurse(BWCurses.ARACHNOPHOBIA)) {
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
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if ((Object) this instanceof CaveSpiderEntity) {
			spawnedByArachnophobia = nbt.getBoolean("SpawnedByArachnophobia");
		}
		setInsanityTargetUUID(nbt.getString("InsanityTargetUUID").isEmpty() ? Optional.empty() : Optional.of(UUID.fromString(nbt.getString("InsanityTargetUUID"))));
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if ((Object) this instanceof CaveSpiderEntity) {
			nbt.putBoolean("SpawnedByArachnophobia", spawnedByArachnophobia);
		}
		nbt.putString("InsanityTargetUUID", getInsanityTargetUUID().isPresent() ? getInsanityTargetUUID().get().toString() : "");
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(INSANITY_TARGET_UUID, Optional.empty());
	}
}
