package moriyashiine.bewitchment.common.entity.living.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

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
	protected void initDataTracker() {
		super.initDataTracker();
		if (getVariants() > 1) {
			dataTracker.startTracking(VARIANT, 0);
		}
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (getVariants() > 1) {
			dataTracker.set(VARIANT, tag.getInt("Variant"));
		}
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		if (getVariants() > 1) {
			tag.putInt("Variant", dataTracker.get(VARIANT));
		}
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess serverWorldAccess, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
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
		return super.initialize(serverWorldAccess, difficulty, spawnReason, entityData, entityTag);
	}
	
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		boolean client = world.isClient;
		ItemStack stack = player.getStackInHand(hand);
		if (!isTamed() && isTamingItem(stack)) {
			if (!player.abilities.creativeMode) {
				stack.decrement(1);
			}
			if (!isSilent()) {
				world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_PARROT_EAT, getSoundCategory(), 1, 1 + (random.nextFloat() - random.nextFloat()) * 0.2f);
			}
			if (!client) {
				if (random.nextInt(4) == 0) {
					setOwner(player);
					navigation.stop();
					setTarget(null);
					setSitting(true);
					world.sendEntityStatus(this, (byte) 7);
				}
				else {
					world.sendEntityStatus(this, (byte) 6);
				}
			}
			return ActionResult.success(client);
		}
		else if (isTamed() && isOwner(player)) {
			setSitting(!isSitting());
			return ActionResult.success(client);
		}
		return super.interactMob(player, hand);
	}
	
	protected abstract boolean hasShiny();
	
	public abstract int getVariants();
	
	protected abstract boolean isTamingItem(ItemStack stack);
}
