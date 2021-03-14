package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements MasterAccessor {
	private UUID masterUUID = null;
	
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
	
	@Shadow
	public abstract void setTarget(@Nullable LivingEntity target);
	
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getMasterUUID() != null) {
			Entity master = ((ServerWorld) world).getEntity(getMasterUUID());
			if (master instanceof MobEntity && !((MobEntity) master).isDead() && ((MobEntity) master).getTarget() != null) {
				setTarget(((MobEntity) master).getTarget());
			}
			else {
				PlayerLookup.tracking(this).forEach(playerEntity -> SpawnSmokeParticlesPacket.send(playerEntity, this));
				remove();
			}
		}
	}
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"))
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			if (this instanceof Pledgeable) {
				if (target instanceof MasterAccessor && getUuid().equals(((MasterAccessor) target).getMasterUUID())) {
					return null;
				}
				Pledgeable pledgeable = (Pledgeable) this;
				if (BewitchmentAPI.isPledged(world, pledgeable.getPledgeID(), target.getUuid())) {
					BewitchmentAPI.unpledge(world, pledgeable.getPledgeID(), target.getUuid());
				}
				pledgeable.summonMinions((MobEntity) (Object) this);
			}
			if (isUndead() && !BWUtil.getBlockPoses(target.getBlockPos(), 2, foundPos -> BWTags.UNDEAD_MASK.contains(world.getBlockState(foundPos).getBlock())).isEmpty()) {
				return null;
			}
		}
		return target;
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (tag.contains("MasterUUID")) {
			setMasterUUID(tag.getUuid("MasterUUID"));
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (getMasterUUID() != null) {
			tag.putUuid("MasterUUID", getMasterUUID());
		}
	}
}
