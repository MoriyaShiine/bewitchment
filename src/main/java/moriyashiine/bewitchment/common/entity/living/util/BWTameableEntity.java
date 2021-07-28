package moriyashiine.bewitchment.common.entity.living.util;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BWTameableEntity extends TameableEntity {
	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(BWTameableEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	protected BWTameableEntity(EntityType<? extends TameableEntity> type, World world) {
		super(type, world);
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) {
			return false;
		}
		else {
			Entity entity = source.getAttacker();
			setSitting(false);
			if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof PersistentProjectileEntity)) {
				amount = (amount + 1) / 2f;
			}
			return super.damage(source, amount);
		}
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		int variants = getVariants();
		if (variants > 1) {
			if (hasShiny()) {
				if (random.nextInt(8192) == 0) {
					dataTracker.set(VARIANT, 0);
				}
				else {
					dataTracker.set(VARIANT, random.nextInt(variants - 1) + 1);
				}
			}
			else {
				dataTracker.set(VARIANT, random.nextInt(variants));
			}
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}
	
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		boolean client = world.isClient;
		ItemStack stack = player.getStackInHand(hand);
		if (isBreedingItem(stack)) {
			if (getHealth() < getMaxHealth()) {
				if (!client) {
					eat(player, hand, stack);
					heal(4);
				}
				return ActionResult.success(client);
			}
		}
		else {
			if (!isTamed()) {
				if (isTamingItem(stack)) {
					if (!client) {
						eat(player, hand, stack);
						if (random.nextInt(4) == 0) {
							setOwner(player);
							setSitting(true);
							setTarget(null);
							navigation.stop();
							world.sendEntityStatus(this, (byte) 7);
						}
						else {
							world.sendEntityStatus(this, (byte) 6);
						}
					}
					return ActionResult.success(client);
				}
			}
			else if (isOwner(player)) {
				if (!client) {
					setSitting(!isSitting());
				}
				return ActionResult.success(client);
			}
		}
		return super.interactMob(player, hand);
	}
	
	@Override
	public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
		if (target instanceof TameableEntity && ((TameableEntity) target).isTamed()) {
			return false;
		}
		if (target instanceof HorseBaseEntity && ((HorseBaseEntity) target).isTame()) {
			return false;
		}
		if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).shouldDamagePlayer((PlayerEntity) target)) {
			return false;
		}
		return !(target instanceof CreeperEntity) && !(target instanceof GhastEntity);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		if (getVariants() > 1) {
			dataTracker.startTracking(VARIANT, 1);
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (getVariants() > 1) {
			dataTracker.set(VARIANT, nbt.getInt("Variant"));
		}
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (getVariants() > 1) {
			nbt.putInt("Variant", dataTracker.get(VARIANT));
		}
	}
	
	protected abstract boolean hasShiny();
	
	public abstract int getVariants();
	
	protected abstract boolean isTamingItem(ItemStack stack);
}
