package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	@Unique
	private boolean spawnedByArachnophobia = false;

	@Shadow
	public abstract void setTarget(@Nullable LivingEntity target);

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			UUID insanityTargetUUID = BWComponents.FAKE_MOB_COMPONENT.get(this).getTarget();
			if (insanityTargetUUID != null && !target.getUuid().equals(insanityTargetUUID)) {
				return null;
			}
		}
		return target;
	}

	@Inject(method = "dropLoot", at = @At("HEAD"))
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo callbackInfo) {
		if (!world.isClient && (Object) this instanceof SpiderEntity && !spawnedByArachnophobia && source.getAttacker() instanceof LivingEntity livingAttacker && BWComponents.CURSES_COMPONENT.get(livingAttacker).hasCurse(BWCurses.ARACHNOPHOBIA)) {
			for (int i = 0; i < random.nextInt(3) + 3; i++) {
				SpiderEntity spider;
				if (random.nextFloat() < 1 / 8192f) {
					spider = EntityType.SPIDER.create(world);
				} else {
					spider = EntityType.CAVE_SPIDER.create(world);
					((MobEntityMixin) (Object) spider).spawnedByArachnophobia = true;
				}
				if (spider != null) {
					spider.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
					spider.initialize((ServerWorldAccess) world, world.getLocalDifficulty(getBlockPos()), SpawnReason.EVENT, null, null);
					spider.setTarget(livingAttacker);
					world.spawnEntity(spider);
				}
			}
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if ((Object) this instanceof CaveSpiderEntity) {
			spawnedByArachnophobia = nbt.getBoolean("SpawnedByArachnophobia");
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if ((Object) this instanceof CaveSpiderEntity) {
			nbt.putBoolean("SpawnedByArachnophobia", spawnedByArachnophobia);
		}
	}
}
