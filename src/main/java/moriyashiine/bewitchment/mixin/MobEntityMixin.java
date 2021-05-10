package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

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
			if (target instanceof GhostEntity) {
				return null;
			}
			if (target instanceof MasterAccessor && getUuid().equals(((MasterAccessor) target).getMasterUUID())) {
				return null;
			}
			if (isUndead() && !BWUtil.getBlockPoses(target.getBlockPos(), 2, foundPos -> BWTags.UNDEAD_MASK.contains(world.getBlockState(foundPos).getBlock())).isEmpty()) {
				return null;
			}
		}
		return target;
	}
	
	@Inject(method = "method_29506", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void method_29506(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfoReturnable, ItemStack heldStack) {
		if (heldStack.getItem() instanceof TaglockItem) {
			callbackInfoReturnable.setReturnValue(TaglockItem.useTaglock(player, this, hand, true, false));
		}
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
