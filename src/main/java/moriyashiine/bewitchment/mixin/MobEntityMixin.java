package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	private static final TrackedData<Boolean> AFFECTED_BY_WAR = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
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
					dataTracker.set(AFFECTED_BY_WAR, true);
					return (LivingEntity) nearest;
				}
				else if (contractAccessor.hasNegativeEffects() && dataTracker.get(AFFECTED_BY_WAR)) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1));
					addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 1));
				}
			}
		}
		return target;
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		dataTracker.set(AFFECTED_BY_WAR, tag.getBoolean("AffectedByWar"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putBoolean("AffectedByWar", dataTracker.get(AFFECTED_BY_WAR));
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(AFFECTED_BY_WAR, false);
	}
}
